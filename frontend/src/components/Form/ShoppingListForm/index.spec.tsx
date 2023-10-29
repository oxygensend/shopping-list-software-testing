import React from 'react';
import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import {ShoppingListForm} from './index';
import authAxios from '../../../utils/authAxios';
import {API_URL} from '../../../config';
import MockedFunction = jest.MockedFunction;
import axios from "axios";

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
        const saveButton = screen.getByText('Save');

        const body = {
            name: 'Test Shopping List',
            dateOfExecution: '2023-12-31T12:00'
        }

        // Act
        fireEvent.change(nameInput, {target: {value: body.name}});
        fireEvent.change(dateInput, {target: {value: body.dateOfExecution}});
        fireEvent.submit(saveButton);

        // Assert
        await waitFor(() => {
            expect(mockRequest).toHaveBeenCalled();
        });
    });
    it('should render form fields and products', async () => {

        render(<ShoppingListForm request={jest.fn()}/>);

        await waitFor(() => {
            expect(authAxios.get).toHaveBeenCalledWith(`${API_URL}/v1/products`);
        });

        // Assert form fields are rendered
        expect(screen.getByLabelText('Name')).toBeInTheDocument();
        expect(screen.getByLabelText('Date of execution')).toBeInTheDocument();
        expect(screen.getByLabelText('Attachment Image')).toBeInTheDocument();

        // Assert products are rendered
        expect(screen.getByText('Products:')).toBeInTheDocument();
        expect(screen.getByTestId('minus')).toBeInTheDocument();
        expect(screen.getByTestId('plus')).toBeInTheDocument();
    });

    it('should render completed field in form if shopList is added as prop', async () => {

        render(<ShoppingListForm request={jest.fn()} shoppingList={
            {
                id: '1',
                name: 'Test Shopping List',
                completed: false,
                imageAttachmentFilename: null,
                products: [],
                dateOfExecution: new Date(),
                createdAt: new Date(),
                updatedAt: new Date(),
            }
        }/>);

        expect(screen.getByLabelText('Completed')).toBeInTheDocument();
    });


});