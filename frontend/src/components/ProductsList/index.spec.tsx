import React from 'react';
import {render} from '@testing-library/react';
import {ProductsList} from "./index";
import {Product} from "../../types";

describe('ProductsList component', () => {
    it('renders products correctly', () => {
        // Arrange
        const products: Product[] = [
            {id: "1", product: 'Apple', quantity: 3, grammar: 'kg', completed: true},
            {id: "2", product: 'Banana', quantity: 5, grammar: 'pieces', completed: true},
            {id: "3", product: 'Orange', quantity: 2, grammar: 'l', completed: true},
        ];

        // Act
        const {getByText} = render(<ProductsList products={products}/>);

        // Assert
        products.forEach(({product, quantity, grammar}) => {
            const productElement = getByText(product);
            const quantityElement = getByText(quantity.toString());
            const grammarElement = getByText(grammar);
            expect(productElement).toBeInTheDocument();
            expect(quantityElement).toBeInTheDocument();
            expect(grammarElement).toBeInTheDocument();
        });
    });

    it('renders products with correct table headers', () => {
        // Arrange
        const products :Product[]= [
            {id: "1", product: 'Apple', quantity: 3, grammar: 'pieces', completed: true},
            {id: "2", product: 'Banana', quantity: 5, grammar: 'pieces', completed: true},
        ];

        // Act
        const {getByText} = render(<ProductsList products={products}/>);

        // Assert
        expect(getByText('Product')).toBeInTheDocument();
        expect(getByText('Quantity')).toBeInTheDocument();
        expect(getByText('Grammar')).toBeInTheDocument();
    });
});
