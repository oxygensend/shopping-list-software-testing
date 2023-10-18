import {render} from "@testing-library/react";
import {Error} from "./index";

describe("Error component", () => {

    test('renders Error component with string error message', () => {
        // Arrange
        const errorMessage = 'This is an error message';

        // Act
        const {getByText} = render(<Error error={errorMessage}/>);

        // Assert
        expect(getByText(errorMessage)).toBeInTheDocument();
    });

    test('renders Error component with object error messages', () => {
        // Arrange
        const errorObject = {
            field1: 'Error 1',
            field2: 'Error 2',
        };

        // Act
        const {getByText} = render(<Error error={errorObject}/>);

        // Assert
        Object.values(errorObject).forEach((errorMessage) => {
            expect(getByText(errorMessage)).toBeInTheDocument();
        });
    });

})