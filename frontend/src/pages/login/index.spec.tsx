import {render} from "@testing-library/react";
import {Login} from "./index";

describe('Login page ', () => {
    test('renders Login component', () => {
        // Act
        const {getByRole} = render(<Login/>);

        // Assert
        const loginFormElement = getByRole('textbox');
        expect(loginFormElement).toBeInTheDocument();
    });
});