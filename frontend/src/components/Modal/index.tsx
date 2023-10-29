import React, {useEffect, useRef} from "react";
import { ReactComponent as XIcon } from '../../assets/x-solid.svg';

type ModalProps = {
    isOpen: boolean;
    onClose: () => void;
    title: string;
    children: React.JSX.Element;
    order?: string;
};

export const Modal = ({ isOpen, onClose, title, children,  order }: ModalProps) => {
    const modalRef = useRef<HTMLDivElement | null>(null);
    const width = {
        common: '',
        confirmation: 'w-96',
    };

    // TODO fix issue with order of closing modals

    useEffect(() => {
        const handleOutsideClick = (e: MouseEvent) => {
            const modals = document.querySelectorAll('.modal-wrapper');
            const modalArr = Array.from(modals).sort((a:any, b:any) => {
                return parseInt(b.style.zIndex || '0') - parseInt(a.style.zIndex || '0');
            });
            const highestOrderModal = modalArr[0];
            if (highestOrderModal.contains(e.target as Node)) {
                return;
            }
            onClose();
        };
        const handleOnKeyDown = (e: KeyboardEvent) => {
            if (e.key === 'Escape') {
                onClose();
            }
        };
        if (isOpen) {
            document.addEventListener('keydown', handleOnKeyDown);
            document.addEventListener('mousedown', handleOutsideClick);
            if (modalRef.current) {
                modalRef.current.style.zIndex = order || '0';
            }
        } else {
            document.removeEventListener('mousedown', handleOutsideClick);
            document.removeEventListener('keydown', handleOnKeyDown);
            if (modalRef.current) {
                modalRef.current.style.zIndex = '0';
            }
        }

        return () => {
            document.removeEventListener('mousedown', handleOutsideClick);
            document.removeEventListener('keydown', handleOnKeyDown);
            if (modalRef.current) {
                modalRef.current.style.zIndex = '0';
            }
        };
    }, [isOpen, onClose, order]);
    if (!isOpen) {
        return null;
    }

    return (
        <div
            data-testid={'modal-wrapper'}
            className={`modal-wrapper fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex items-center overflow justify-center z-${
                order ?? '40'
            }`}
        >
            <div className={'bg-[#1f1b24] rounded-lg shadow-xl  w-96  lg:w-1/2 xl:w-180'} ref={modalRef}>
                <div className='flex justify-between border-b border-pink-200 p-3 text-center'>
                    <h3 className='font-semibold text-xl text-center text-pink-50'>{title}</h3>
                    <XIcon
                        fill={'white'}
                        height={15}
                        width={15}
                        className={'cursor-pointer hover:fill-pink-600 mt-2 mr-1'}
                        onClick={onClose}
                        data-testid={'close-button'}
                    />
                </div>
                <div className={'mt-5'}>{children}</div>
            </div>
        </div>
    );
};
