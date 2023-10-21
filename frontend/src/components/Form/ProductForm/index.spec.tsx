import React from 'react';
import {render, screen, fireEvent} from '@testing-library/react';
import {ProductForm} from './index';
import MockedFunction = jest.MockedFunction;

describe('ProductForm', () => {
    let mockSaveProduct: MockedFunction<any>;
    let mockRemoveProduct: MockedFunction<any>;

    beforeEach(() => {
        mockSaveProduct = jest.fn();
        mockRemoveProduct = jest.fn();
    });

    test('renders ProductForm component with initial values', () => {
        // Arrange
        const props = {
            products: ['Product1', 'Product2'],
            grammarNames: ['L', 'XL'],
            saveProduct: mockSaveProduct,
            removeProduct: mockRemoveProduct,
            index: 0,
            productVal: 'Product1',
            quantityVal: 2,
            grammarVal: 'L',
        };

        // Act
        render(<ProductForm {...props} />);

        // Assert
        const productInput = screen.getByPlaceholderText('Product') as HTMLInputElement;
        const quantityInput = screen.getByPlaceholderText('Quantity') as HTMLInputElement;
        const grammarSelect = screen.getByRole('combobox') as HTMLSelectElement;

        expect(productInput.value).toBe('Product1');
        expect(quantityInput.value).toBe('2');
        expect(grammarSelect.value).toBe('L');
    });

    test('triggers saveProduct callback on user input changes', () => {
        // Arrange
        const props = {
            products: ['Product1', 'Product2'],
            grammarNames: ['L', 'XL'],
            saveProduct: mockSaveProduct,
            removeProduct: mockRemoveProduct,
            index: 0,
        };

        // Act
        render(<ProductForm {...props} />);

        // Assert - Initial state check
        expect(mockSaveProduct).not.toHaveBeenCalled();

        // Act - User interactions
        const productInput = screen.getByPlaceholderText('Product');
        const quantityInput = screen.getByPlaceholderText('Quantity');
        const grammarSelect = screen.getByRole('combobox');
        fireEvent.change(productInput, {target: {value: 'NewProduct'}});
        fireEvent.change(quantityInput, {target: {value: '5'}});
        fireEvent.change(grammarSelect, {target: {value: 'XL'}});

        // Assert - saveProduct callback should be called with updated values
        expect(mockSaveProduct).toHaveBeenCalledTimes(3);
    });

    test('triggers removeProduct callback on minus button click', () => {
        // Arrange
        const props = {
            products: ['Product1', 'Product2'],
            grammarNames: ['L', 'XL'],
            saveProduct: mockSaveProduct,
            removeProduct: mockRemoveProduct,
            index: 0,
        };

        // Act
        render(<ProductForm {...props} />);

        // Assert - Initial state check
        expect(mockRemoveProduct).not.toHaveBeenCalled();

        // Act - User clicks remove button
        const minusButton = screen.getByText('-');
        fireEvent.click(minusButton);

        // Assert - removeProduct callback should be called
        expect(mockRemoveProduct).toHaveBeenCalledWith(0);
    });


});
