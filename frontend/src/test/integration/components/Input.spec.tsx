import {render, screen} from "@testing-library/react";
import {Input} from "../../../components/Form/Input";

const mockRegister = jest.fn();

describe('Input component integration', () => {

    test('render Error component if error prop is provided as string', () => {
        // Arrange
        const label = 'Username';
        const isRequired = true;
        const placeholder = "Enter username";
        const name = "username";
        const error = "Invalid field"

        // Act
        render(<Input
            name={name}
            label={label}
            required={isRequired}
            type={"text"}
            register={mockRegister}
            placeholder={placeholder}
            error={error}
        />);

        // Assert
        const inputElement = screen.getByLabelText(label) as HTMLInputElement;
        expect(inputElement.name).toBe(name);
        expect(inputElement.required).toBe(isRequired);
        expect(inputElement.placeholder).toBe(placeholder);
        expect(screen.getByText(error)).toBeInTheDocument();
    })

    test('render Error component if error prop is provided as errorType', () => {
        // Arrange
        const label = 'Username';
        const isRequired = true;
        const placeholder = "Enter username";
        const name = "username";
        const error = {
            field1: "Invalid password field",
            field2: "Invalid username field",
        }

        // Act
        render(<Input
            name={name}
            label={label}
            required={isRequired}
            type={"text"}
            register={mockRegister}
            placeholder={placeholder}
            error={error}
        />);

        // Assert
        const inputElement = screen.getByLabelText(label) as HTMLInputElement;
        expect(inputElement.name).toBe(name);
        expect(inputElement.required).toBe(isRequired);
        expect(inputElement.placeholder).toBe(placeholder);
        expect(screen.getByText(error.field1)).toBeInTheDocument();
        expect(screen.getByText(error.field2)).toBeInTheDocument();
    })
})