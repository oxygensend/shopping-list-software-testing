import React from 'react';
import {render, screen, fireEvent} from '@testing-library/react';
import MockedFunction = jest.MockedFunction;
import {ProductForm} from "../../../components/Form/ProductForm";

describe('ProductForm', () => {
    let mockSaveProduct: MockedFunction<any>;
    let mockRemoveProduct: MockedFunction<any>;

    beforeEach(() => {
        mockSaveProduct = jest.fn();
        mockRemoveProduct = jest.fn();
    });

    test('should render minusPlus component value', () => {
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

        // Assert
        expect(screen.getByTestId('minus-plus')).toBeInTheDocument();
        expect(screen.getByText('-')).toBeInTheDocument();
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
