import React from 'react';
import {render, screen, fireEvent} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {ShoppingListForm} from './index';
import authAxios from '../../../utils/authAxios';
import {API_URL} from '../../../config';
import MockedFunction = jest.MockedFunction;

jest.mock('../../../utils/authAxios');

describe('ShoppingListForm component', () => {
    const mockRequest = jest.fn();
    const mockedAxios: MockedFunction<any> = authAxios;

    beforeEach(() => {
        mockedAxios.get = jest.fn(() => Promise.resolve({data: products}));
    });

    const products = {
        names: ['Product 1', 'Product 2'],
        grammarNames: ['S', 'M', 'L', 'XL'],
    }
    test('fetches product names and grammar names on mount', async () => {
        // Arrange
        render(<ShoppingListForm request={mockRequest}/>);

        // Assert
        expect(authAxios.get).toHaveBeenCalledWith(`${API_URL}/v1/products`);
        await screen.findByText('Products:');
    });

    test('renders and submits form with valid data', async () => {
        // Arrange
        render(<ShoppingListForm request={mockRequest}/>);
        const nameInput = screen.getByLabelText('Name');
        const dateInput = screen.getByLabelText('Date of execution');
        const fileInput = screen.getByLabelText('Attachment Image');
        const saveButton = screen.getByText('Save');

        // Act
        fireEvent.change(nameInput, {target: {value: 'Test Shopping List'}});
        fireEvent.change(dateInput, {target: {value: '2023-12-31T12:00'}});
        const file = new File(['(⌐□_□)'], 'test.png', {type: 'image/png'});
        fireEvent.change(fileInput, {target: {files: [file]}});
        fireEvent.click(saveButton);

        // Assert
        expect(mockRequest).toHaveBeenCalled();
    });

    test('renders error message for invalid form data', async () => {
        // Arrange
        render(<ShoppingListForm request={mockRequest}/>);
        const saveButton = screen.getByText('Save');

        // Act
        const nameInput = screen.getByLabelText('Name');
        fireEvent.change(nameInput, {target: {value: ''}});
        fireEvent.click(saveButton);

        // Assert
        await screen.findByText('Invalid product name');
        expect(mockRequest).not.toHaveBeenCalled();
    });

    test('renders product form and saves product when adding new product', async () => {
        // Arrange
        render(<ShoppingListForm request={mockRequest}/>);
        const addButton = screen.getByText('+');

        // Act
        fireEvent.click(addButton);
        const productNameInput = await screen.findByPlaceholderText('Product');
        userEvent.type(productNameInput, 'New Product');
        const quantityInput = screen.getByPlaceholderText('Quantity');
        userEvent.type(quantityInput, '5');
        const grammarSelect = screen.getByRole('combobox');
        userEvent.selectOptions(grammarSelect, 'L');
        const saveButton = screen.getByText('Save');

        fireEvent.click(saveButton);

        // Assert
        expect(mockRequest).toHaveBeenCalled();
        expect(mockRequest.mock.calls[0][0].products[0].name).toBe('New Product');
        expect(mockRequest.mock.calls[0][0].products[0].quantity).toBe(5);
        expect(mockRequest.mock.calls[0][0].products[0].grammar).toBe('XL');
    });
});