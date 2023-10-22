import {Product, ProductDto} from '../types';
import {mapListProductToListProductDto, mapProductToProductDto} from "./mapProductToProductDto";

describe('mapProductToProductDto', () => {
    test('it maps a Product object to ProductDto', () => {
        // Arrange
        const product: Product = {
            id: '1',
            product: 'Product 1',
            quantity: 5,
            grammar: 'KG',
            completed: false,
        };

        // Act
        const result: ProductDto = mapProductToProductDto(product);

        // Assert
        expect(result).toEqual({
            name: 'Product 1',
            quantity: 5,
            grammar: 'KG',
        });
    });
});

describe('mapListProductToListProductDto', () => {
    test('it maps a list of Product objects to ProductDto objects', () => {
        // Arrange
        const products: Product[] = [
            {
                id: '1',
                product: 'Product 1',
                quantity: 5,
                grammar: 'KG',
                completed: false,
            },
            {
                id: '1',
                product: 'Product 2',
                quantity: 3,
                grammar: 'L',
                completed: false,
            },
        ];

        // Act
        const result: ProductDto[] = mapListProductToListProductDto(products);

        // Assert
        expect(result).toEqual([
            {
                name: 'Product 1',
                quantity: 5,
                grammar: 'KG',
            },
            {
                name: 'Product 2',
                quantity: 3,
                grammar: 'L',
            },
        ]);
    });
});
