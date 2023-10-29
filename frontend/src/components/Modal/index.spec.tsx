import React from 'react';
import {render, fireEvent, screen, waitFor} from '@testing-library/react';
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

    it('should set the zIndex correctly', async () => {
        const onCloseMock = jest.fn();
        render(<Modal isOpen={true} onClose={onCloseMock} title="Test Modal" order="50">
            <div>Test Content</div>
        </Modal>);
        const modalWrapper = screen.getByTestId('modal-wrapper');
        expect(modalWrapper).toHaveClass('z-50');
    });

    it('should call onClose when close button is clicked', () => {
        const onCloseMock = jest.fn();
        render(<Modal isOpen={true} onClose={onCloseMock} title="Test Modal">
            <div>Test Content</div>
        </Modal>);
        const closeButton = screen.getByTestId('close-button');
        fireEvent.click(closeButton);
        expect(onCloseMock).toHaveBeenCalledTimes(1);
    });

    it('should not render when isOpen is false', () => {
        render(<Modal isOpen={false} onClose={jest.fn()} title="Test Modal">
            <div>Test Content</div>
        </Modal>);
        const modalWrapper = screen.queryByTestId('modal-wrapper');
        expect(modalWrapper).not.toBeInTheDocument();
    });
});
