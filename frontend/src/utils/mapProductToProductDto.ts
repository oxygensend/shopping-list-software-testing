import {Product, ProductDto} from "../types";

export const mapProductToProductDto = (product: Product): ProductDto => {
    return {
        name: product.product,
        quantity: product.quantity,
        grammar: product.grammar,
    }
}
export const mapListProductToListProductDto = (products: Product[]): ProductDto[] => {
    return products.map((product: Product) => mapProductToProductDto(product));
}