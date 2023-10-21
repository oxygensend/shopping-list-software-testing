import React from 'react';
import {render} from '@testing-library/react';
import {Layout} from "./index";

describe('Layout Component', () => {
    it('renders children components correctly', () => {
        // Arrange
        const sampleChildComponent = <div data-testid="child-component">Sample Child Component</div>;

        // Act
        const {getByTestId} = render(<Layout>{sampleChildComponent}</Layout>);

        // Assert
        const childComponent = getByTestId('child-component');
        expect(childComponent).toBeInTheDocument();
        expect(childComponent).toHaveTextContent('Sample Child Component');
    });

});
