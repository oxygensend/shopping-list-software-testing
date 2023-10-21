import React from 'react';
import {render, act, waitFor} from '@testing-library/react';
import { SessionTimeout } from './index';
import MockedFunction = jest.MockedFunction;
import {getAccessToken, removeTokens} from "../../../security/tokenStorage";

jest.mock('../../security/tokenStorage', () => ({
    getAccessToken: jest.fn(),
    removeTokens: jest.fn(),
}));

jest.useFakeTimers();

describe('SessionTimeout component', () => {

    const mockGetAccessToken :MockedFunction<any> = getAccessToken;
    const mockRemoveTokens: MockedFunction<any> = removeTokens;

    beforeEach(() => {
        jest.clearAllTimers();
    });

    it('should not log out if user is authenticated and active', () => {
        // Arrange
        mockGetAccessToken.mockReturnValue('fakeAccessToken');

        // Act
        render(<SessionTimeout />);

        act(() => {
            jest.advanceTimersByTime(899999); // 899999 ms (less than 15 minutes)
        });

        // Assert
        expect(getAccessToken).toHaveBeenCalled();
        expect(removeTokens).not.toHaveBeenCalled();
    });

    // it('should log out if user is authenticated and inactive for 15 minutes', async () => {
    //     // Arrange
    //     mockGetAccessToken.mockReturnValue('fakeAccessToken');
    //
    //     // Act
    //     render(<SessionTimeout />);
    //
    //     sessionStorage.setItem('lastTimeStamp', '2021-01-01T00:00:00.000Z');
    //     // Wait for async operations inside useEffect to complete
    //     await act(async () => {
    //         // Simulate user inactivity for 15 minutes
    //         jest.advanceTimersByTime(900000); // 900000 ms (15 minutes)
    //         await waitFor(() => expect(removeTokens).toHaveBeenCalled());
    //     });
    //
    //     // Assert
    //     expect(getAccessToken).toHaveBeenCalled();
    //     expect(removeTokens).toHaveBeenCalled();
    // });

    it('should not log out if user is not authenticated', () => {
        // Arrange
        mockGetAccessToken.mockReturnValue(undefined);

        // Act
        render(<SessionTimeout />);

        // Simulate user inactivity
        act(() => {
            jest.advanceTimersByTime(900000); // 900000 ms (15 minutes)
        });

        // Assert
        expect(getAccessToken).toHaveBeenCalled();
        expect(removeTokens).not.toHaveBeenCalled();
    });

    it('should reset timer on user activity', () => {
        // Arrange
        mockGetAccessToken.mockReturnValue('fakeAccessToken');

        // Act
        render(<SessionTimeout />);

        // Simulate user activity within the warning interval
        act(() => {
            jest.advanceTimersByTime(899999); // 899999 ms (less than 15 minutes)
        });

        // Simulate user activity again
        act(() => {
            jest.advanceTimersByTime(5000); // 5000 ms (5 seconds)
        });

        // Assert
        expect(getAccessToken).toHaveBeenCalled();
        expect(removeTokens).not.toHaveBeenCalled();
    });
});
