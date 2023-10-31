import React from 'react';
import {render, waitFor} from '@testing-library/react';
import {Layout} from "../../../components/Global/Layout";
import userEvent from "@testing-library/user-event";

describe('Layout Component', () => {

    describe('Navbar Component Integration', () => {

        it('renders main page link when user is not authenticated', () => {
            const {getByText} = render(<Layout>
                <div></div>
            </Layout>);
            const mainPageLink = getByText('Main page');
            expect(mainPageLink).toBeInTheDocument();
        });

        it('renders logout button when user is authenticated', () => {
            Object.defineProperty(window, 'localStorage', {
                value: {
                    getItem: jest.fn(() => 'mockAccessToken'), // Simulate an authenticated session
                },
                writable: true,
            });
            const {getByText} = render(<Layout>
                <div></div>
            </Layout>);
            const logoutButton = getByText('Logout');
            expect(logoutButton).toBeInTheDocument();
        });

        it('calls removeTokens and redirects to login page when logout button is clicked', async () => {
            const originalLocation = window.location;

            // @ts-ignore
            delete window.location;
            window.location = {
                ...originalLocation,
                href: '',
                assign: jest.fn(),
            };

            Object.defineProperty(window, 'localStorage', {
                value: {
                    getItem: jest.fn(() => 'mockAccessToken'),
                    removeItem: jest.fn(),
                },
                writable: true,
            });

            const {getByText} = render(<Layout>
                <div></div>
            </Layout>);
            const logoutButton = getByText('Logout');
            userEvent.click(logoutButton);

            await waitFor(() => {
                expect(window.localStorage.removeItem).toHaveBeenCalledWith('accessToken');
                expect(window.location.href).toBe('/login');
            });

            window.location = originalLocation;
        });

        it('does not call removeTokens or redirect when user is not authenticated', () => {
            const originalLocation = window.location;

            // @ts-ignore
            delete window.location;
            window.location = {
                ...originalLocation,
                href: '',
                assign: jest.fn(),
            };

            const {getByText} = render(<Layout>
                <div></div>
            </Layout>);
            const mainPageLink = getByText('Main page');
            userEvent.click(mainPageLink);
            expect(window.location.href).toBe('');

            window.location = originalLocation;
        });
    });
});
