import {SubExceptionType} from "../types";

export const findPropertyViolation = (violations: SubExceptionType[], property: string): string | undefined => {
    const violation = violations.find((violation: any) => violation.field === property);
    return  violation ? `${violation.field} ${violation.message}` : undefined;
};