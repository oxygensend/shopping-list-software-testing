import { render} from "@testing-library/react";
import {Register} from "./index";
import axios from "axios";
import React from "react";

jest.mock('axios');

describe('Register page ', () => {

    test('renders Register component', () => {
        // Act
        const {getByLabelText} = render(<Register/>);

        // Assert
        const loginFormElement = getByLabelText('Email');
        expect(loginFormElement).toBeInTheDocument();
    });

});