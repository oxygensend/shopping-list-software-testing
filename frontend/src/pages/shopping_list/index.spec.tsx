import React from 'react';
import {render, waitFor, screen, fireEvent} from '@testing-library/react';
import {ShoppingList} from './index';
import authAxios from '../../utils/authAxios';

jest.mock('../../utils/authAxios');

describe('ShoppingList Component', () => {
    const mockAuthAxios = authAxios as jest.Mocked<typeof authAxios>;
    const shoppingListData = {
        id: '1',
        name: 'Test Shopping List',
        completed: false,
        updatedAt: new Date(),
        createdAt: new Date(),
        dateOfExecution: new Date(),
        products: [
            {
                id: '1',
                product: 'Test Product',
                grammar: 'S',
                quantity: 1,
                completed: false
            }
        ],
        imageAttachmentFilename: 'test-image.jpg',
    };

    beforeEach(() => {
        mockAuthAxios.get.mockResolvedValueOnce({data: shoppingListData});
    })

    it('renders shopping list details correctly', async () => {

        // Act
        render(<ShoppingList/>);

        // Assert
        await waitFor(() => {
            expect(screen.getByText('Name:')).toBeInTheDocument();
            expect(screen.getByText('Status:')).toBeInTheDocument();
            expect(screen.getByText('Created at:')).toBeInTheDocument();
            expect(screen.getByText('Updated at:')).toBeInTheDocument();
            expect(screen.getByText('Product')).toBeInTheDocument();
            expect(screen.getByText('Quantity')).toBeInTheDocument();
            expect(screen.getByText('Grammar')).toBeInTheDocument();
            expect(screen.getByText('Waiting')).toBeInTheDocument();
            expect(screen.getByText('Edit')).toBeInTheDocument();
            expect(screen.getByText('Delete')).toBeInTheDocument();
            expect(screen.getByAltText('Test Shopping List')).toBeInTheDocument();
            expect(screen.getByText('Test Product')).toBeInTheDocument();
            expect(screen.getByText('S')).toBeInTheDocument();
            expect(screen.getByText('1')).toBeInTheDocument();
        });
    });

    // it('calls delete endpoint when delete button is clicked', async () => {
    //     // Arrange
    //     mockAuthAxios.delete.mockResolvedValueOnce({});
    //
    //     const originalAlert = window.alert;
    //     const alertMock = jest.fn();
    //     window.alert = alertMock;
    //
    //     render(<ShoppingList/>);
    //
    //     // Act
    //     await waitFor(() => {
    //         fireEvent.click(screen.getByText('Delete'));
    //     });
    //
    //     // Assert
    //     await waitFor(() => {
    //         expect(alertMock).toHaveBeenCalledWith('Are you sure you want to delete this shopping list?');
    //     })
    // });
    //
    // it('opens edit modal when edit button is clicked', async () => {
    //
    //     // Act
    //     render(<ShoppingList/>);
    //     await waitFor(() => {
    //         fireEvent.click(screen.getByText('Edit'));
    //     });
    //
    //     // Assert
    //     await waitFor(() => {
    //         expect(screen.getByText('Create new shopping list')).toBeInTheDocument();
    //     })
    // });
});
