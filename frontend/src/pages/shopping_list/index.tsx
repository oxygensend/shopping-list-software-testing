import {useParams} from "react-router";
import React, {useEffect, useState} from "react";
import authAxios from "../../utils/authAxios";
import {API_URL} from "../../config";
import {ShoppingList as ShoppingListType} from "../../types";
import {Layout} from "../../components/Global/Layout";
import {ShoppingListInfo} from "../../components/ShoppingListInfo";
import {ProductsList} from "../../components/ProductsList";
import {Button} from "../../components/Button";
import {ShoppingListForm} from "../../components/Form/ShoppingListForm";
import {Modal} from "../../components/Modal";

export const ShoppingList = ({}) => {
    const params = useParams();
    const shoppingListId = params.id as string;
    const [shoppingList, setShoppingList] = useState<ShoppingListType>();
    const [isEditShoppingListModalOpen, setIsEditShoppingListModalOpen] = useState<boolean>(false);
    const [reset, setReset] = useState<boolean>(false);

    useEffect(() => {
        authAxios.get(`${API_URL}/v1/shopping-lists/${shoppingListId}`)
            .then((res) => {
                setShoppingList(res.data)
            })

    }, [reset, setReset]);

    const onClickDeleteHandler = () => {
        if (!window.confirm("Are you sure you want to delete this shopping list?")) return

        authAxios.delete(`${API_URL}/v1/shopping-lists/${shoppingListId}`)
            .then((res) => {
                window.location.href = '/';
            }).catch((e) => {
            console.log(e)
        })
    }

    const onClickEditHandler = () => {
        setIsEditShoppingListModalOpen(true);
    }

    const editShoppingListRequest = async (body: any) => {
        const {data} = await authAxios.patch<any>(`${API_URL}/v1/shopping-lists/${shoppingListId}`, body, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        setIsEditShoppingListModalOpen(false);
        setReset(true);
    }

    if (shoppingList) {

        return (
            <Layout>
                <div className={"lg:grid lg:grid-cols-12 flex flex-col items-center gap-10 h-full"} data-testid={'t'}>

                    <div className={"col-start-2 col-end-6 flex flex-col gap-10"}>
                        <div>
                            <ShoppingListInfo field={"Name"} text={shoppingList.name}/>
                            <ShoppingListInfo field={"Status"} text={shoppingList.completed ? "Executed" : "Waiting"}/>
                            <ShoppingListInfo field={"Updated at"} text={shoppingList.updatedAt.toString()}/>
                            <ShoppingListInfo field={"Created at"} text={shoppingList.createdAt.toString()}/>
                            <ShoppingListInfo field={"Date of Execution"}
                                              text={shoppingList.dateOfExecution?.toString()}/>


                        </div>
                        <div className={"flex flex-row gap-6"}>
                            <Button
                                name={"Edit"}
                                color={"bg-blue-600"}
                                hoverColor={"bg-blue-500"}
                                type={"button"}
                                onClick={onClickEditHandler}
                                dataTestId={"edit-button"}
                            />
                            <Button
                                name={"Delete"}
                                color={"bg-red-600"}
                                hoverColor={"bg-red-700"}
                                type={"button"}
                                onClick={onClickDeleteHandler}
                            />

                        </div>
                    </div>

                    <div className={"lg:col-start-7 lg:col-end-12 w-3/4 lg:w-full justify-center"}>
                        <ProductsList products={shoppingList.products}/>
                    </div>

                    <div className={"col-start-2 col-end-6"}>
                        <img
                            src={`${API_URL}/v1/shopping-lists/attachment_image/${shoppingList.imageAttachmentFilename}`}
                            alt={shoppingList.name}
                            className={"w-full"}
                        />
                    </div>

                    <Modal isOpen={isEditShoppingListModalOpen}
                           onClose={() => setIsEditShoppingListModalOpen(false)}
                           title={"Create new shopping list"}>
                        <ShoppingListForm request={editShoppingListRequest} shoppingList={shoppingList}/>
                    </Modal>
                </div>
            </Layout>
        )
    } else {
        return null;
    }


}