import React from 'react';
import {render, screen} from '@testing-library/react';
import {MemoryRouter, Route} from 'react-router-dom';
import {ProtectedRoute} from './index';

describe('ProtectedRoute Component', () => {
    it('renders children when user is authorized', () => {
        render(
            <MemoryRouter initialEntries={['/protected']}>
                <ProtectedRoute isAuthorized={true} redirect={'/test'}>
                    <div data-testid="child-content">Child Content</div>
                </ProtectedRoute>
            </MemoryRouter>
        );

        const childContent = screen.getByTestId('child-content');
        expect(childContent).toBeInTheDocument();
    });


    it('redirects to default route when user is not authorized', () => {
        render(
            <MemoryRouter initialEntries={['/protected']}>
                <ProtectedRoute isAuthorized={false} redirect={'/'}/>
            </MemoryRouter>
        );

        expect(window.location.pathname).toBe('/');
    });


});
