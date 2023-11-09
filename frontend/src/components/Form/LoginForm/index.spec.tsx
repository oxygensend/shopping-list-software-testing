import {setAccessToken, setRefreshToken} from "../../../security/tokenStorage";
import {act, fireEvent, render, waitFor, screen} from "@testing-library/react";
import axios from "axios";
import {API_URL} from "../../../config";
import React from "react";
import {LoginForm} from "./index";

jest.mock('axios'); // Mock the axios module
jest.mock("../../../security/tokenStorage", () => ({
    setAccessToken: jest.fn(),
    setRefreshToken: jest.fn(),
}));

describe('LoginForm', () => {
    let mockSetAccessToken = setAccessToken;
    let mockSetRefreshToken = setRefreshToken;
    const mockedAxios = axios as jest.Mocked<typeof axios>;


    test('renders form fields and submit button', () => {

        // Act
        const {getByLabelText, getByText} = render(<LoginForm/>);

        // Assert
        expect(getByLabelText('Email')).toBeInTheDocument();
        expect(getByLabelText('Password')).toBeInTheDocument();
        expect(getByText('Sign in')).toBeInTheDocument();
    });

    test('submits form with valid data and sets tokens', async () => {
        // Arrange
        const mockAccessToken = 'mockAccessToken';
        const mockRefreshToken = 'mockRefreshToken';
        mockedAxios.post.mockResolvedValueOnce({
            data: {
                accessToken: mockAccessToken,
                refreshToken: mockRefreshToken,
            },
        });

        // Act
        const {getByLabelText, getByText} = render(<LoginForm/>);

        fireEvent.change(getByLabelText('Email'), {target: {value: 'test@example.com'}});
        fireEvent.change(getByLabelText('Password'), {target: {value: 'password123'}});
        fireEvent.submit(getByText('Sign in'));

        // Assert
        await waitFor(() => {
            expect(mockedAxios.post).toHaveBeenCalledWith(`${API_URL}/v1/auth/access_token`, {
                email: 'test@example.com',
                password: 'password123',
            });
            expect(mockSetAccessToken).toHaveBeenCalledWith(mockAccessToken);
            expect(mockSetRefreshToken).toHaveBeenCalledWith(mockRefreshToken);
        });
    });

    it('should display error message on failed login attempt', async () => {
        const errorMessage = 'Invalid credentials';
        const axiosPostSpy = jest.spyOn(axios, 'post').mockRejectedValueOnce({
            response: {
                status: 401,
                data: {
                    message: errorMessage,
                },
            },
        });

        render(<LoginForm/>)

        fireEvent.change(screen.getByLabelText('Email'), {target: {value: 'test@example.com'}});
        fireEvent.change(screen.getByLabelText('Password'), {target: {value: 'password123'}});

        act(() => {
            fireEvent.submit(screen.getByRole('button', {name: /Sign in/i}));
        });

        await waitFor(() => {
            expect(axiosPostSpy).toHaveBeenCalledWith(expect.any(String), {
                email: 'test@example.com',
                password: 'password123',
            });
            expect(screen.getByText(errorMessage)).toBeInTheDocument();
        });
    });

});