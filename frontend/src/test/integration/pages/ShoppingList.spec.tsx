import React from 'react';
import {render, waitFor, screen, fireEvent} from '@testing-library/react';
import authAxios from "../../../utils/authAxios";
import {ShoppingList} from "../../../pages/shopping_list";
import userEvent from "@testing-library/user-event";
import {API_URL} from "../../../config";

jest.mock('../../../utils/authAxios');

describe('ShoppingList Component Integration Tests', () => {
    const mockAuthAxios = authAxios as jest.Mocked<typeof authAxios>;
    const shoppingListData = {
        id: '1',
        name: 'Test Shopping List',
        completed: false,
        updatedAt: new Date('2021-01-01'),
        createdAt: new Date('2021-01-02'),
        dateOfExecution: new Date('2021-01-03'),
        products: [
            {
                id: '1',
                product: 'Test Product',
                grammar: 'S',
                quantity: 1,
                completed: false
            }
        ],
        imageAttachmentFilename: 'test-image.jpg',
    };

    const products = {
        names: ['Product 1', 'Product 2'],
        grammarNames: ['S', 'M', 'L', 'XL'],
    }

    beforeEach(() => {
        jest.spyOn(mockAuthAxios, 'get').mockImplementation((url: String) => {
            switch (url) {
                case `${API_URL}/v1/products`:
                    return Promise.resolve({data: products});
                default:
                    return Promise.resolve({data: shoppingListData});
            }
        })
    })

    afterEach(() => {
        jest.clearAllMocks();
    })

    it('renders ShoppingListInfo components correctly', async () => {

        render(<ShoppingList/>);

        await waitFor(() => {
            expect(screen.getByText('Name:')).toBeInTheDocument();
            expect(screen.getByText(shoppingListData.name)).toBeInTheDocument();
            expect(screen.getByText('Status:')).toBeInTheDocument();
            expect(screen.getByText(shoppingListData.completed ? "Executed" : "Waiting")).toBeInTheDocument();
            expect(screen.getByText('Created at:')).toBeInTheDocument();
            expect(screen.getByText(shoppingListData.createdAt.toString())).toBeInTheDocument();
            expect(screen.getByText('Updated at:')).toBeInTheDocument();
            expect(screen.getByText(shoppingListData.updatedAt.toString())).toBeInTheDocument();
            expect(screen.getByText('Date of Execution:')).toBeInTheDocument();
            expect(screen.getByText(shoppingListData.dateOfExecution.toString())).toBeInTheDocument();
        });
    });

    it('fetches shopping list data and renders correctly', async () => {

        render(<ShoppingList/>);

        await waitFor(() => {
            expect(screen.getByText(shoppingListData.name)).toBeInTheDocument();
        });
    });


    it('calls window confirm when delete button is clicked', async () => {
        // Arrange
        mockAuthAxios.delete.mockResolvedValueOnce({});

        const alertMock = jest.spyOn(window, 'confirm').mockImplementation();
        render(<ShoppingList/>);

        // Act
        await waitFor(() => {
            fireEvent.click(screen.getByText('Delete'));
        });

        // Assert
        await waitFor(() => {
            expect(alertMock).toHaveBeenCalledTimes(1);
        })
    });

    it('calls delete endpoint when delete button is clicked', async () => {
        // Arrange
        mockAuthAxios.delete.mockResolvedValueOnce({});

        const alertMock = jest.spyOn(window, 'confirm').mockReturnValueOnce(true);
        render(<ShoppingList/>);

        // Act
        await waitFor(() => {
            fireEvent.click(screen.getByText('Delete'));
        });

        // Assert
        await waitFor(() => {
            expect(mockAuthAxios.delete).toHaveBeenCalledTimes(1);
        })
    });

    it('opens edit modal when edit button is clicked should render form fields', async () => {

        mockAuthAxios.get.mockResolvedValueOnce({data: shoppingListData});
        // Act
        render(
            <ShoppingList/>
        );

        await waitFor(() => {
            expect(screen.getByText('Edit')).toBeInTheDocument();
            const button = screen.getByText('Edit');
            userEvent.click(button);

        })

        // Assert
        await waitFor(() => {
            expect(screen.getByText('Edit Shopping List')).toBeInTheDocument();
            expect(screen.getByLabelText('Date of execution')).toBeInTheDocument();
            expect(screen.getByLabelText('Name')).toBeInTheDocument();
            expect(screen.getByLabelText('Name')).toHaveValue(shoppingListData.name);
            expect(screen.getByLabelText('Attachment Image')).toBeInTheDocument();
            expect(screen.getByLabelText('Completed')).toBeInTheDocument();
            expect(screen.getByLabelText('Completed')).not.toBeChecked();
            expect(screen.getByText('Products:')).toBeInTheDocument();
            expect(screen.getAllByTestId('minus').length).toBe(2);
            expect(screen.getByTestId('plus')).toBeInTheDocument();
            expect(screen.getByText('Save')).toBeInTheDocument();

        })
    });

    it('opens edit modal when edit button is clicked should render image preview', async () => {

        mockAuthAxios.get.mockResolvedValueOnce({data: shoppingListData});
        // Act
        render(
            <ShoppingList/>
        );

        await waitFor(() => {
            const button = screen.getByText('Edit');
            userEvent.click(button);
        })

        // Assert
        await waitFor(() => {
            expect(screen.getByAltText('Image preview')).toBeInTheDocument();
        })
    });

    it('opens edit modal when edit button is clicked should render actual products fields form', async () => {


        jest.spyOn(mockAuthAxios, 'get').mockImplementation((url: String) => {
            switch (url) {
                case `${API_URL}/v1/products`:
                    return Promise.resolve({data: products});
                default:
                    return Promise.resolve({data: shoppingListData});
            }
        })

        render(
            <ShoppingList/>
        );

        await waitFor(() => {
            const button = screen.getByText('Edit');
            userEvent.click(button);
        })

        await waitFor(() => {

            const grammarSelect = screen.getByRole('combobox') as HTMLSelectElement;

            expect(screen.getByPlaceholderText('Product')).toBeInTheDocument();
            expect(screen.getByPlaceholderText('Quantity')).toBeInTheDocument();
            expect(screen.getByRole('combobox')).toBeInTheDocument();
            expect(screen.getByDisplayValue('Test Product')).toBeInTheDocument()
            expect(screen.getByDisplayValue('Test Product')).toHaveValue(shoppingListData.products[0].product);
            expect(screen.getByDisplayValue('1')).toBeInTheDocument()
            expect(screen.getByDisplayValue('1')).toHaveValue(shoppingListData.products[0].quantity);
            expect(grammarSelect.options[grammarSelect.selectedIndex].text).toBe(shoppingListData.products[0].grammar);
            expect(grammarSelect.value).toBe('S');
        })
    });

    it('opens edit modal when edit button is clicked and send edit request', async () => {


        mockAuthAxios.patch.mockResolvedValueOnce({
            data: {
                id: '1',
                name: 'Test Shopping List',
                completed: false,
                updatedAt: new Date('2021-01-01'),
                createdAt: new Date('2021-01-02'),
                dateOfExecution: new Date('2021-01-03'),
                products: [
                    {
                        id: '1',
                        product: 'Test Product',
                        grammar: 'S',
                        quantity: 1,
                        completed: false
                    }
                ],
            }
        })
        // Act
        render(
            <ShoppingList/>
        );

        await waitFor(() => {
            const button = screen.getByText('Edit');
            userEvent.click(button);
            const nameInput = screen.getByLabelText('Name');
            const dateInput = screen.getByLabelText('Date of execution');
            const saveButton = screen.getByText('Save');

            const body = {
                name: 'Test Shopping List',
                dateOfExecution: '2023-12-31T12:00'
            }

            // Act
            fireEvent.change(nameInput, {target: {value: body.name}});
            fireEvent.change(dateInput, {target: {value: body.dateOfExecution}});
            fireEvent.submit(saveButton);

        });
        // Assert
        await waitFor(() => {
            expect(mockAuthAxios.patch).toHaveBeenCalled();
        });
    });
    it('opens edit modal when edit button is clicked and close modal when X button is clicked', async () => {

        // Act
        render(
            <ShoppingList/>
        );

        await waitFor(() => {
            const button = screen.getByText('Edit');
            userEvent.click(button);
            const closeButton = screen.getByTestId('close-button');
            userEvent.click(closeButton);

        });
        // Assert
        await waitFor(() => {
            expect(screen.queryByText('Edit Shopping List')).not.toBeInTheDocument();
        });
    });

});
