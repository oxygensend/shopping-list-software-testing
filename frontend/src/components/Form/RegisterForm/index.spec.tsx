import {setAccessToken, setRefreshToken} from "../../../security/tokenStorage";
import axios from "axios/index";
import {fireEvent, render, waitFor} from "@testing-library/react";
import React from "react";
import {RegisterForm} from "./index";
import {API_URL} from "../../../config";

jest.mock('axios');
jest.mock("../../../security/tokenStorage", () => ({
    setAccessToken: jest.fn(),
    setRefreshToken: jest.fn(),
}));


describe('RegisterForm', () => {
    let mockSetAccessToken = setAccessToken;
    let mockSetRefreshToken = setRefreshToken;
    const mockedAxios = axios as jest.Mocked<typeof axios>;


    const mockedError = (field: string, message: string) => {
        mockedAxios.post.mockRejectedValueOnce({
            response: {
                status: 400,
                data: {
                    message: 'error',
                    status: 'BAD_REQUEST',
                    subExceptions: [
                        {
                            field: field,
                            message: message,
                        }
                    ]
                }
            }
        });

    }
    test('renders form fields and submit button', () => {
        // Act
        const {getByTestId, getByText} = render(<RegisterForm/>);

        // Assert
        expect(getByTestId('email')).toBeInTheDocument();
        expect(getByTestId('firstName')).toBeInTheDocument();
        expect(getByTestId('lastName')).toBeInTheDocument();
        expect(getByTestId('password')).toBeInTheDocument();
        expect(getByText('Sign up')).toBeInTheDocument();
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
        const {getByLabelText, getByText} = render(<RegisterForm/>);

        fireEvent.change(getByLabelText('Email'), {target: {value: 'test@example.com'}});
        fireEvent.change(getByLabelText('Firstname'), {target: {value: 'John'}});
        fireEvent.change(getByLabelText('Lastname'), {target: {value: 'Doe'}});
        fireEvent.change(getByLabelText('Password'), {target: {value: 'password123'}});
        fireEvent.submit(getByText('Sign up'));

        await waitFor(() => {
            expect(mockedAxios.post).toHaveBeenCalledWith(`${API_URL}/v1/auth/register`, {
                email: 'test@example.com',
                firstName: 'John',
                lastName: 'Doe',
                password: 'password123',
            });
            expect(mockSetAccessToken).toHaveBeenCalledWith(mockAccessToken);
            expect(mockSetRefreshToken).toHaveBeenCalledWith(mockRefreshToken);
        });
    });

    test('displays error message for invalid email format', async () => {
        // Arrange
        mockedError("email", "invalid")

        // Act
        const {getByLabelText, getByText, findByText} = render(<RegisterForm/>);
        fireEvent.change(getByLabelText('Email'), {target: {value: 'invalidemail'}});
        fireEvent.submit(getByText('Sign up'));

        // Assert
        const errorMessage = await findByText('email invalid');

        expect(errorMessage).toBeInTheDocument();
    });

    test('displays error message for empty password', async () => {
        // Arrange
        mockedError("password", "size must be between 2 and 64")

        // Act
        const {getByLabelText, getByText, findByText} = render(<RegisterForm/>);
        fireEvent.change(getByLabelText('Password'), {target: {value: ''}});
        fireEvent.submit(getByText('Sign up'));


        // Assert
        const errorMessage = await findByText('password size must be between 2 and 64');
        expect(errorMessage).toBeInTheDocument();
    });
    test('displays error message for empty lastname', async () => {
        // Arrange
        mockedError("lastName", "size must be between 2 and 64")

        // Act
        const {getByLabelText, getByText, findByText} = render(<RegisterForm/>);
        fireEvent.change(getByLabelText('Lastname'), {target: {value: ''}});
        fireEvent.submit(getByText('Sign up'));


        // Assert
        const errorMessage = await findByText('lastName size must be between 2 and 64');
        expect(errorMessage).toBeInTheDocument();
    });
});

