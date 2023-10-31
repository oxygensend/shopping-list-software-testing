import React from 'react';
import {render, fireEvent, screen, waitFor} from '@testing-library/react';
import {Modal} from './index';

describe('Modal component', () => {
     it('renders modal content correctly', () => {
        const { getByText } = render(<Modal isOpen={true} onClose={() => {}} title="Test Modal" children={<div>Modal Content</div>} />);
        const modalContent = getByText('Modal Content');
        expect(modalContent).toBeInTheDocument();
    });

    it('renders modal with correct title', () => {
        const { getByText } = render(<Modal isOpen={true} onClose={() => {}} title="Test Modal" children={<div>Modal Content</div>} />);
        const modalTitle = getByText('Test Modal');
        expect(modalTitle).toBeInTheDocument();
    });

    it('renders modal with custom order class', () => {
        const { getByTestId } = render(<Modal isOpen={true} onClose={() => {}} title="Test Modal" children={<div>Modal Content</div>} order="50" />);
        const modalWrapper = getByTestId('modal-wrapper');
        expect(modalWrapper).toHaveClass('z-50');
    });

    it('closes modal when escape key is pressed', () => {
        const onCloseMock = jest.fn();
        render(<Modal isOpen={true} onClose={onCloseMock} title="Test Modal" children={<div>Modal Content</div>} />);
        fireEvent.keyDown(document, { key: 'Escape', code: 'Escape' });
        expect(onCloseMock).toHaveBeenCalledTimes(1);
    });

    it('does not close modal when other keys are pressed', () => {
        const onCloseMock = jest.fn();
        render(<Modal isOpen={true} onClose={onCloseMock} title="Test Modal" children={<div>Modal Content</div>} />);
        fireEvent.keyDown(document, { key: 'Enter', code: 'Enter' });
        expect(onCloseMock).not.toHaveBeenCalled();
    });

    it('does not close modal when modal content is clicked', () => {
        const onCloseMock = jest.fn();
        const { getByTestId } = render(<Modal isOpen={true} onClose={onCloseMock} title="Test Modal" children={<div>Modal Content</div>} />);
        const modalWrapper = getByTestId('modal-wrapper');
        fireEvent.click(modalWrapper);
        expect(onCloseMock).not.toHaveBeenCalled();
    });
});
