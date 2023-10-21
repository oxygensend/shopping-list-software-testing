import React from 'react';
import { render } from '@testing-library/react';
import { ShoppingListInfo } from './index';

describe('ShoppingListInfo component', () => {
    it('renders shopping list info correctly', () => {
        // Arrange
        const shoppingListInfoProps = {
            field: 'Name',
            text: 'Groceries',
        };

        // Act
        const { getByText } = render(<ShoppingListInfo {...shoppingListInfoProps} />);

        // Assert
        const fieldElement = getByText('Name:');
        const textElement = getByText('Groceries');
        expect(fieldElement).toBeInTheDocument();
        expect(textElement).toBeInTheDocument();
    });

});
