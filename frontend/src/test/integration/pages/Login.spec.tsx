import {fireEvent, render, screen, waitFor} from "@testing-library/react";
import {Login} from "../../../pages/login";
import {LoginForm} from "../../../components/Form/LoginForm";
import React from "react";
import axios from "axios/index";

jest.mock('axios'); // Mock the axios module

describe("Login Page", () => {

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

    test("renders Login component as form", () => {
        render(<Login/>);
        const loginFormElement = document.querySelector("form");
        expect(loginFormElement).toBeInTheDocument();
    });

    test("renders all form fields are displayed", () => {
        render(<Login/>);

        expect(screen.getByLabelText('Email')).toBeInTheDocument();
        expect(screen.getByLabelText('Password')).toBeInTheDocument();
        expect(screen.getByText('Sign in')).toBeInTheDocument();
    });


    test('submits LoginForm component with valid data', async () => {
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


    test('submits LoginForm component with valid data and redirect to home', async () => {
        const mockAccessToken = 'mockAccessToken';
        const mockRefreshToken = 'mockRefreshToken';
        mockedAxios.post.mockResolvedValueOnce({
            data: {
                accessToken: mockAccessToken,
                refreshToken: mockRefreshToken,
            },
        })

        render(<Login/>);

        fireEvent.change(screen.getByLabelText('Email'), {target: {value: 'test@test.com'}});
        fireEvent.change(screen.getByLabelText('Password'), {target: {value: 'test'}});
        fireEvent.submit(screen.getByText('Sign in'));

        await waitFor(() => {
            expect(window.location.href).toBe('/');
        });

    });

    test('Submit LoginForm with invalid data should display Invalid Credentials error', async () => {
        mockedAxios.post.mockRejectedValueOnce({
            response: {
                status: 401,
                data: {
                    message: 'Invalid credentials',
                },
            },
        });

        render(<Login/>);

        fireEvent.change(screen.getByLabelText('Email'), {target: {value: 'test@example.com'}});
        fireEvent.change(screen.getByLabelText('Password'), {target: {value: 'wrongpassword'}});
        fireEvent.submit(screen.getByText('Sign in'));

        await waitFor(() => {
            expect(screen.getByText('Invalid credentials')).toBeInTheDocument();
        });
    });

});