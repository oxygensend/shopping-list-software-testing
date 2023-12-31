/* istanbul ignore file */
export type  ErrorType = {
    [value: string]: string;
}

export type ExceptionType = {
    debugMessage?: string;
    message: string;
    status: string;
    timestamp?: string
    subExceptions: SubExceptionType[]
}

export type SubExceptionType = {
    field: string;
    message: string;
    object?: string;
    rejectedValue?: string;
}

export type ShoppingListPreview = {
    id: string;
    name: string;
    completed: boolean;
}

export type ShoppingList = {
    id: string;
    name: string;
    completed: boolean;
    imageAttachmentFilename: string|null;
    products: Product[];
    dateOfExecution: Date;
    createdAt: Date;
    updatedAt: Date;
}

export type Product = {
    id: string;
    product: string;
    grammar: string;
    quantity: number;
    completed: boolean
}

export type ProductDto = {
    name: string|null;
    grammar: string;
    quantity: number;
}
export type AccessTokenPayload = {
    username: string;
    email: string;
    name: string;
    iat: number;
    exp: number;
    type: 'auth';
};
