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

    test('should render minusPlus component', () => {
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


    it('should update product state on input change', () => {
        const saveProductMock = jest.fn();
        render(
            <ProductForm
                products={['Product1', 'Product2']}
                grammarNames={['L', 'M']}
                saveProduct={saveProductMock}
                removeProduct={jest.fn()}
                index={0}
            />
        );

        const productInput = screen.getByPlaceholderText('Product') as HTMLInputElement;
        fireEvent.change(productInput, {target: {value: 'Product1'}});

        expect(productInput.value).toBe('product1');
        expect(saveProductMock).toHaveBeenCalledWith(0, 'product1', 0, 'L');
    });

    it('should display search results on input change', async () => {
        render(
            <ProductForm
                products={['Product1', 'Product2']}
                grammarNames={['L', 'M']}
                saveProduct={jest.fn()}
                removeProduct={jest.fn()}
                index={0}
            />
        );

        const productInput = screen.getByPlaceholderText('Product');
        fireEvent.change(productInput, {target: {value: 'Product'}});

        const searchResults = await screen.findAllByRole('listitem');
        expect(searchResults).toHaveLength(2);
    });

    it('should update quantity state on input change', () => {
        const saveProductMock = jest.fn();
        render(
            <ProductForm
                products={['Product1', 'Product2']}
                grammarNames={['L', 'M']}
                saveProduct={saveProductMock}
                removeProduct={jest.fn()}
                index={0}
            />
        );

        const quantityInput = screen.getByPlaceholderText('Quantity') as HTMLInputElement;
        fireEvent.change(quantityInput, {target: {value: 10}});

        expect(quantityInput.value).toBe("10");
        expect(saveProductMock).toHaveBeenCalledWith(0, '', "10", 'L');
    });
    //

    it('should update grammar state on select change', () => {
        const saveProductMock = jest.fn();
        render(
            <ProductForm
                products={['Product1', 'Product2']}
                grammarNames={['L', 'M']}
                saveProduct={saveProductMock}
                removeProduct={jest.fn()}
                index={0}
            />
        );

        const grammarSelect = screen.getByRole('combobox') as HTMLSelectElement;
        fireEvent.change(grammarSelect, {target: {value: 'M'}});

        expect(grammarSelect.value).toBe('M');
        expect(saveProductMock).toHaveBeenCalledWith(0, '', 0, 'M');
    });


});
