import {fireEvent, render} from "@testing-library/react";
import {ListElement} from "./index";

describe('ListElement component', () => {
    // Arrange
    const listElementProps = {
        id: '1',
        title: 'Test List',
        completed: false,
    };

    it('renders list element with correct title', () => {
        // Act
        const {getByText} = render(<ListElement {...listElementProps} />);

        // Assert
        expect(getByText('Test List')).toBeInTheDocument();
    });

    it('calls onClickEventHandler when list element is clicked', () => {

        // Arrange
        const originalLocation = window.location;
        // @ts-ignore
        delete window.location;
        window.location = {
            ...originalLocation,
            href: '',
            assign: jest.fn(),
        };

        // Act
        const { getByText } = render(<ListElement {...listElementProps} />);
        const listElement = getByText('Test List');
        fireEvent.click(listElement);

        // Assert
        expect(window.location.href).toBe('/shopping-lists/1');

        // Restore original window.location
        window.location = originalLocation;
    });


    it('renders black bar when completed is false', () => {
        // Act
        const {container} = render(<ListElement {...listElementProps} completed={false}/>);
        const blackBar = container.querySelector('.bg-black');

        // Assert
        expect(blackBar).toBeInTheDocument();
    });

    it('does not render black bar when completed is true', () => {
        // Act
        const {container} = render(<ListElement {...listElementProps} completed={true}/>);
        const blackBar = container.querySelector('.bg-black');

        // Assert
        expect(blackBar).toBeNull();
    });
});