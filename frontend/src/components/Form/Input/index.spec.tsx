import {render, screen} from "@testing-library/react";
import {Input} from "./index";

const mockRegister = jest.fn();

describe('Input component', () => {

    test('renders Input component check if input attributes are properly set for text', () => {
        // Arrange
        const label = 'Username';
        const isRequired = true;
        const placeholder = "Enter username";
        const name = "username";

        // Act
        const {getByLabelText} = render(<Input
            name={name}
            label={label}
            required={isRequired}
            type={"text"}
            register={mockRegister}
            placeholder={placeholder}
        />);

        // Assert
        const inputElement = getByLabelText(label) as HTMLInputElement;
        expect(inputElement.name).toBe(name);
        expect(inputElement.required).toBe(isRequired);
        expect(inputElement.placeholder).toBe(placeholder);
    });


    test('renders Input component with default value', () => {
        // Arrange
        const label = 'Username';
        const isRequired = true;
        const placeholder = "Enter username";
        const name = "username";
        const defaultValue = '25';

        // Act
        const {getByLabelText} = render(<Input
            name={name}
            label={label}
            required={isRequired}
            type={"text"}
            register={mockRegister}
            placeholder={placeholder}
            defaultValue={defaultValue}
        />);

        // Assert
        const inputElement = getByLabelText(label) as HTMLInputElement;
        expect(inputElement.value).toBe(defaultValue);
    });

    test('renders Input component with step attribute for number type', () => {
        // Arrange
        const label = 'Number';
        const isRequired = true;
        const placeholder = "Number";
        const name = "number";
        const step = 2;

        // Act
        const {getByLabelText} = render(<Input
            name={name}
            label={label}
            required={isRequired}
            type={"number"}
            register={mockRegister}
            placeholder={placeholder}
            step={step}
        />);
        // Assert
        const inputElement = getByLabelText(label) as HTMLInputElement;
        expect(inputElement.step).toBe(step.toString());
    });

});