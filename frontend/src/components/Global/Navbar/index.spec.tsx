import React from 'react';
import {render, screen, fireEvent} from '@testing-library/react';
import {Navbar} from "./index";
import {getAccessToken, removeTokens} from "../../../security/tokenStorage";
import MockedFunction = jest.MockedFunction;

jest.mock('../../../security/tokenStorage', () => ({
    getAccessToken: jest.fn(),
    removeTokens: jest.fn(),
}));

describe('Navbar Component', () => {
    const mockGetAccessToken: MockedFunction<any> = getAccessToken;

    it('renders main page link', () => {
        // Act
        render(<Navbar/>);
        const mainPageLink = screen.getByText(/Main page/i);
        // Assert
        expect(mainPageLink).toBeInTheDocument();
    });

    it('renders logout link when authorized', () => {
        // Arrange
        mockGetAccessToken.mockReturnValue(true);

        // Act

        render(<Navbar/>);
        const logoutLink = screen.getByText(/Logout/i);
        // Assert
        expect(logoutLink).toBeInTheDocument();
    });

    it('calls onClickLogoutEvent function when logout link is clicked', () => {
        // Arrange
        mockGetAccessToken.mockReturnValue(true);

        const originalLocation = window.location;
        // @ts-ignore
        delete window.location;
        window.location = {
            ...originalLocation,
            href: '',
            assign: jest.fn(),
        };

        // Act
        render(<Navbar/>);
        const logoutLink = screen.getByText(/Logout/i);
        fireEvent.click(logoutLink);

        // Assert
        expect(removeTokens).toHaveBeenCalled();
        expect(window.location.href).toBe('/login');
    });

    it('does not render logout link when not authorized', () => {
        // Arrange
        mockGetAccessToken.mockReturnValue(false);


        // Act
        render(<Navbar/>);
        const logoutLink = screen.queryByText(/Logout/i);

        // Assert
        expect(logoutLink).toBeNull();
    });
});
