import React from 'react';
import {render, waitFor, screen, fireEvent} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {ShoppingLists} from './index';
import authAxios from '../../utils/authAxios';
import MockedFunction = jest.MockedFunction;
import {API_URL} from "../../config";
import mock = jest.mock;

jest.mock('../../utils/authAxios'); // Mock the authAxios module

describe('ShoppingLists Component', () => {

    const mockedAxios: MockedFunction<any> = authAxios;
    const mockShoppingListsResponse = {
        data: [
            {
                id: 1,
                name: 'Test Shopping List',
                completed: false,
            }
        ],
        numberOfElements: 1,
        numberOfPages: 1,
    };

    beforeEach(() => {
        mockedAxios.get = jest.fn(() => Promise.resolve({data: mockShoppingListsResponse}));
    })


    it('render create new button and shopping lists welcome board', async () => {
        // Arrange
        // Act
        render(<ShoppingLists/>);

        // Assert
        expect(mockedAxios.get).toHaveBeenCalledWith(`${API_URL}/v1/shopping-lists?page=0`);
        await waitFor(() => {
            expect(screen.getByText('Collection of shopping lists')).toBeInTheDocument();
            expect(screen.getByText('Create new')).toBeInTheDocument();
        });

    });

    test('opens and closes the create new shopping list modal', async () => {
        // Arrange
        render(<ShoppingLists/>);


        // Assert
        await waitFor(() => {
            // Act - Open modal
            userEvent.click(screen.getByText('Create new'));
            expect(screen.getByText('Create new shopping list')).toBeInTheDocument();
        })

        // Act - Close modal
        userEvent.click(screen.getByTestId('close-button'));

        // Assert
        await waitFor(() => {
            expect(screen.queryByText('Create new shopping list')).not.toBeInTheDocument();
        });
    });

    test('displays shopping list items', async () => {

        // Act
        render(<ShoppingLists/>);

        // Assert
        await waitFor(() => {
            mockShoppingListsResponse.data.forEach((item) => {
                expect(screen.getByText(item.name)).toBeInTheDocument();
            });
        });
    });

});
