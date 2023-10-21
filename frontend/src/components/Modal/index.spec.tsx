import React from 'react';
import {render, fireEvent} from '@testing-library/react';
import {Modal} from './index';

describe('Modal component', () => {
    it('renders modal when isOpen is true', () => {
        // Arrange
        const mockOnClose = jest.fn();
        const modalProps = {
            isOpen: true,
            onClose: mockOnClose,
            title: 'Test Modal',
            order: '50',
        };

        // Act
        const {getByText} = render(<Modal {...modalProps} >
            <div>Modal Content</div>
        </Modal>);

        // Assert
        expect(getByText('Test Modal')).toBeInTheDocument();
        expect(getByText('Modal Content')).toBeInTheDocument();
    });

    it('does not render modal when isOpen is false', () => {
        // Arrange
        const mockOnClose = jest.fn();
        const modalProps = {
            isOpen: false,
            onClose: mockOnClose,
            title: 'Test Modal',
            order: '50',
        };

        // Act
        const {queryByText} = render(<Modal {...modalProps} >
            <div>Modal Content</div>
        </Modal>);
        // Assert
        expect(queryByText('Test Modal')).toBeNull();
        expect(queryByText('Modal Content')).toBeNull();
    });

    it('calls onClose when X icon is clicked', () => {
        // Arrange
        const mockOnClose = jest.fn();
        const modalProps = {
                isOpen: true,
                onClose: mockOnClose,
                title: 'Test Modal',
                order: '50',
            }
        ;

        // Act
        const {getByTestId} = render(<Modal {...modalProps} >
            <div>Modal Content</div>
        </Modal>);
        fireEvent.click(getByTestId('close-button'));

        // Assert
        expect(mockOnClose).toHaveBeenCalled();
    });
});
