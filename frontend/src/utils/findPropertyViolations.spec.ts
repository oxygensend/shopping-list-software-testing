import {findPropertyViolation} from "./findPropertyViolations";
import {SubExceptionType} from "../types";

describe('findPropertyViolation', () => {
    test('returns violation message when property is found in the violations array', () => {
        // Arrange
        const violations: SubExceptionType[] = [
            { field: 'name', message: 'Name is required' },
            { field: 'email', message: 'Email is invalid' },
        ];
        const property = 'name';

        // Act
        const result = findPropertyViolation(violations, property);

        // Assert
        expect(result).toBe('name Name is required');
    });

    test('returns undefined when property is not found in the violations array', () => {
        // Arrange
        const violations: SubExceptionType[] = [
            { field: 'email', message: 'Email is invalid' },
            { field: 'password', message: 'Password is too short' },
        ];
        const property = 'name';

        // Act
        const result = findPropertyViolation(violations, property);

        // Assert
        expect(result).toBeUndefined();
    });

    test('returns undefined when violations array is empty', () => {
        // Arrange
        const violations:any[] = [];
        const property = 'name';

        // Act
        const result = findPropertyViolation(violations, property);

        // Assert
        expect(result).toBeUndefined();
    });

});
