import React from 'react';
import {render, fireEvent} from '@testing-library/react';
import {Button, ButtonProps} from './index';

describe('Button component', () => {
    let onClickMock: jest.Mock;

    beforeEach(() => {
        onClickMock = jest.fn();
    });

    it('renders button with correct name and additional class', () => {
        // Arrange
        const buttonProps: ButtonProps = {
            name: 'Click Me',
            color: 'bg-blue-500',
            hoverColor: 'bg-blue-600',
            type: "button",
            additionalClass: 'custom-class',
            onClick: onClickMock,
        };

        // Act
        const {getByText} = render(<Button {...buttonProps} />);

        // Assert
        const buttonElement = getByText('Click Me');
        expect(buttonElement).toBeInTheDocument();
        expect(buttonElement).toHaveClass('bg-blue-500');
        expect(buttonElement).toHaveClass('hover:bg-blue-600');
        expect(buttonElement).toHaveClass('custom-class');
    });

    it('calls onClick function when button is clicked', () => {
        // Arrange
        const buttonProps: ButtonProps = {
            name: 'Click Me',
            color: 'bg-blue-500',
            hoverColor: 'hover:bg-blue-600',
            type: 'button',
            onClick: onClickMock,
        };

        // Act
        const {getByText} = render(<Button {...buttonProps} />);
        const buttonElement = getByText('Click Me');
        fireEvent.click(buttonElement);

        // Assert
        expect(onClickMock).toHaveBeenCalledTimes(1);
    });

    it('renders button with correct type', () => {
        // Arrange
        const buttonProps: ButtonProps = {
            name: 'Click Me',
            color: 'bg-blue-500',
            hoverColor: 'hover:bg-blue-600',
            type: 'submit',
            onClick: onClickMock,
        };

        // Act
        const {getByText} = render(<Button {...buttonProps} />);
        const buttonElement = getByText('Click Me');

        // Assert
        expect(buttonElement).toHaveAttribute('type', 'submit');
    });
});
