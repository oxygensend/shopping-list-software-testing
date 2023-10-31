import React from "react";
import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import {Register} from "../../../pages/register";
import {API_URL} from "../../../config";
import axios from "axios/index";

jest.mock('axios');
describe('Register page', () => {

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
    };


    test('renders Register component', () => {
        render(<Register/>);

        expect(screen.getByLabelText('Email')).toBeInTheDocument();
        expect(screen.getByLabelText('Firstname')).toBeInTheDocument();
        expect(screen.getByLabelText('Lastname')).toBeInTheDocument();
        expect(screen.getByLabelText('Password')).toBeInTheDocument();
        expect(screen.getByText('Sign up')).toBeInTheDocument();
    })
    test('submits RegisterForm with valid data should register user and save tokens to session', async () => {
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
        const {getByLabelText, getByText} = render(<Register/>);

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

            expect(window.localStorage.getItem('accessToken')).toBe(mockAccessToken);
            expect(window.localStorage.getItem('refreshToken')).toBe(mockRefreshToken);
        });
    });

    test('submits RegisterForm with invalid email data should display an email', async () => {
        // Arrange
        mockedError("email", "invalid")

        // Act
        const {getByLabelText, getByText, findByText} = render(<Register/>);
        fireEvent.change(getByLabelText('Email'), {target: {value: 'invalidemail'}});
        fireEvent.submit(getByText('Sign up'));

        // Assert
        const errorMessage = await findByText('email invalid');

        expect(errorMessage).toBeInTheDocument();
    });

    test('submits RegisterForm with invalid password data should display an error', async () => {
        // Arrange
        mockedError("password", "size must be between 2 and 64")

        // Act
        const {getByLabelText, getByText, findByText} = render(<Register/>);
        fireEvent.change(getByLabelText('Password'), {target: {value: ''}});
        fireEvent.submit(getByText('Sign up'));


        // Assert
        const errorMessage = await findByText('password size must be between 2 and 64');
        expect(errorMessage).toBeInTheDocument();
    });
    test('submits RegisterForm with invalid lastName data should display and error', async () => {
        // Arrange
        mockedError("lastName", "size must be between 2 and 64")

        // Act
        const {getByLabelText, getByText, findByText} = render(<Register/>);
        fireEvent.change(getByLabelText('Lastname'), {target: {value: ''}});
        fireEvent.submit(getByText('Sign up'));


        // Assert
        const errorMessage = await findByText('lastName size must be between 2 and 64');
        expect(errorMessage).toBeInTheDocument();
    });

    // test('submits FormData should with invalid firstName data should display an error', async () => {
    //     // Arrange
    //     mockedError("firstName", "size must be between 2 and 64")
    //
    //     // Act
    //     const {getByLabelText, getByText, findByText} = render(<Register/>);
    //     fireEvent.change(getByLabelText('Firstname'), {target: {value: ''}});
    //     fireEvent.submit(getByText('Sign up'));
    //
    //
    //     // Assert
    //     const errorMessage = await findByText('fistName');
    //     expect(errorMessage).toBeInTheDocument();
    // });
});