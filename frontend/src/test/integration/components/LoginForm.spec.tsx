import {fireEvent, render, waitFor, screen} from "@testing-library/react";
import React from "react";
import {LoginForm} from "../../../components/Form/LoginForm";
import axios from "axios";

jest.mock('axios'); // Mock the axios module

describe('LoginForm', () => {

    const mockedAxios = axios as jest.Mocked<typeof axios>;

    beforeEach(() => {
        const originalLocation = window.location;

        // @ts-ignore
        delete window.location;
        window.location = {
            ...originalLocation,
            href: '',
            assign: jest.fn(),
        }

    })

    test('submits form with valid data and sets tokens', async () => {
        const mockAccessToken = 'mockAccessToken';
        const mockRefreshToken = 'mockRefreshToken';
        mockedAxios.post.mockResolvedValueOnce({
            data: {
                accessToken: mockAccessToken,
                refreshToken: mockRefreshToken,
            },
        });

        const {getByLabelText, getByText} = render(<LoginForm/>);

        fireEvent.change(getByLabelText('Email'), {target: {value: 'test@test.com'}});
        fireEvent.change(getByLabelText('Password'), {target: {value: 'test'}});
        fireEvent.submit(getByText('Sign in'));

        await waitFor(() => {
            expect(window.localStorage.getItem('accessToken')).toBe(mockAccessToken);
            expect(window.localStorage.getItem('refreshToken')).toBe(mockRefreshToken);
        });
    });


    test('submits form with valid data and redirect to home', async () => {
        const mockAccessToken = 'mockAccessToken';
        const mockRefreshToken = 'mockRefreshToken';
        mockedAxios.post.mockResolvedValueOnce({
            data: {
                accessToken: mockAccessToken,
                refreshToken: mockRefreshToken,
            },
        })

        const {getByLabelText, getByText} = render(<LoginForm/>);

        fireEvent.change(getByLabelText('Email'), {target: {value: 'test@test.com'}});
        fireEvent.change(getByLabelText('Password'), {target: {value: 'test'}});
        fireEvent.submit(getByText('Sign in'));

        await waitFor(() => {
            expect(window.location.href).toBe('/');
        });

    });

    test('displays error message for invalid credentials', async () => {
        mockedAxios.post.mockRejectedValueOnce({
            response: {
                status: 401,
                data: {
                    message: 'Invalid credentials',
                },
            },
        });
        const {getByLabelText, getByText, findByText} = render(<LoginForm/>);

        fireEvent.change(getByLabelText('Email'), {target: {value: 'test@example.com'}});
        fireEvent.change(getByLabelText('Password'), {target: {value: 'wrongpassword'}});
        fireEvent.submit(getByText('Sign in'));

        await waitFor(() => {
            expect(screen.getByText('Invalid credentials')).toBeInTheDocument();
        });
    });

});