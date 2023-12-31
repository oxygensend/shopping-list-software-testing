import React from 'react';
import { render } from '@testing-library/react';
import { ShoppingListPreview } from '../../types';
import {List} from "./index";

describe('List Component', () => {

    const mockShoppingLists: ShoppingListPreview[] = [
        { id: '1', name: 'Shopping List 1', completed: false },
        { id: '2', name: 'Shopping List 2', completed: true },
    ];

    it('renders ElementList components properly', () => {
        // Arrange
        const parentClass = 'custom-parent-class';

        // Act
        const { getAllByTestId } = render(<List shoppingLists={mockShoppingLists} parentClass={parentClass} />);

        // Assert
        const listElements = getAllByTestId('list-element');
        expect(listElements).toHaveLength(mockShoppingLists.length);

    });

    it('renders nothing when shoppingLists prop is empty', () => {
        // Act
        const { queryByTestId } = render(<List shoppingLists={[]} />);

        // Assert
        const listElement = queryByTestId('list-element');
        expect(listElement).toBeNull();
    });

});
