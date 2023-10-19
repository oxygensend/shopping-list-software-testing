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