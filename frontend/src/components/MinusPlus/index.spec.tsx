import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import { MinusPlus } from './index';

describe('MinusPlus Component', () => {
    test('renders correctly with default props', () => {
        const { getByText } = render(<MinusPlus onMinusClick={() => {}} position="horizontal" />);
        expect(getByText('-')).toBeInTheDocument();
    });

    test('renders correctly with plus button', () => {
        const { getByText } = render(
            <MinusPlus onMinusClick={() => {}} onPlusClick={() => {}} position="horizontal" />
        );
        expect(getByText('+')).toBeInTheDocument();
    });

    test('calls onMinusClick when minus button is clicked', () => {
        const mockMinusClick = jest.fn();
        const { getByText } = render(<MinusPlus onMinusClick={mockMinusClick} position="horizontal" />);

        fireEvent.click(getByText('-'));
        expect(mockMinusClick).toHaveBeenCalled();
    });

    test('calls onPlusClick when plus button is clicked', () => {
        const mockPlusClick = jest.fn();
        const { getByText } = render(
            <MinusPlus onMinusClick={() => {}} onPlusClick={mockPlusClick} position="horizontal" />
        );

        fireEvent.click(getByText('+'));
        expect(mockPlusClick).toHaveBeenCalled();
    });

    test('does not render plus button when onPlusClick is not provided', () => {
        const { queryByText } = render(<MinusPlus onMinusClick={() => {}} position="horizontal" />);
        expect(queryByText('+')).toBeNull();
    });

    test('renders vertically when position prop is set to vertical', () => {
        const { container } = render(<MinusPlus onMinusClick={() => {}} position="vertical" />);
        expect(container.firstChild).toHaveClass('flex-col');
    });
});
