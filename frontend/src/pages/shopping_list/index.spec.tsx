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

    it('fetches shopping list data and renders correctly', async () => {

        render(<ShoppingList/>);

        await waitFor(() => {
            expect(screen.getByText(shoppingListData.name)).toBeInTheDocument();
        });
    });


    it('calls window confirm when delete button is clicked', async () => {
        // Arrange
        mockAuthAxios.delete.mockResolvedValueOnce({});

        const alertMock = jest.spyOn(window, 'confirm').mockImplementation();
        render(<ShoppingList/>);

        // Act
        await waitFor(() => {
            fireEvent.click(screen.getByText('Delete'));
        });

        // Assert
        await waitFor(() => {
            expect(alertMock).toHaveBeenCalledTimes(1);
        })
    });

    it('calls delete endpoint when delete button is clicked', async () => {
        // Arrange
        mockAuthAxios.delete.mockResolvedValueOnce({});

        const alertMock = jest.spyOn(window, 'confirm').mockReturnValueOnce(true);
        render(<ShoppingList/>);

        // Act
        await waitFor(() => {
            fireEvent.click(screen.getByText('Delete'));
        });

        // Assert
        await waitFor(() => {
            expect(mockAuthAxios.delete).toHaveBeenCalledTimes(1);
        })
    });

    it('opens edit modal when edit button is clicked', async () => {

        // Act
        render(
            <ShoppingList/>
        );

        await waitFor(() => {
            expect(screen.getByTestId('edit-button')).toBeInTheDocument();
            const button = screen.getByTestId('edit-button');
            fireEvent.doubleClick(button);
        })

        // Assert
        await waitFor(() => {
            expect(screen.getByTestId('t').children.length).toEqual(3)

        })
    });

});
