import React from 'react';
import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import {SubmitButton} from "./index";

describe('SubmitButton component', () => {

    test('renders SubmitButton component with correct text and default class', () => {
        // Arrange
        const buttonText = 'Submit';
        render(<SubmitButton value={buttonText}/>);

        // Act
        const submitButton = screen.getByRole('button');

        // Assert
        expect(submitButton).toHaveTextContent(buttonText)
    });

    test('renders SubmitButton component with custom class', () => {
        // Arrange
        const customClass = 'custom-class';
        render(<SubmitButton value="Submit"
                             className={customClass}
        />);

        // Act
        const submitButton = screen.getByRole('button');

        // Assert
        expect(submitButton).toHaveClass(customClass);
    });

    test('renders SubmitButton component with default class if no className is provided', () => {
        // Arrange
        render(<SubmitButton value="Submit"/>);

        // Act
        const submitButton = screen.getByRole('button');

        // Assert
        expect(submitButton).toHaveClass('bg-blue-600');
    });
});

