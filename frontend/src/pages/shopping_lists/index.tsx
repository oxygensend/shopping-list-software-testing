import {useEffect, useState} from "react";
import {ShoppingListPreview} from "../../types";
import authAxios from "../../utils/authAxios";
import {API_URL} from "../../config";
import {List} from "../../components/List";
import {Layout} from "../../components/Global/Layout";
import {Button} from "../../components/Button";
import {Modal} from "../../components/Modal";
import {ShoppingListForm} from "../../components/Form/ShoppingListForm";

type ShoppingListPagedResponse = {
    data: ShoppingListPreview[];
    numberOfElements: number;
    numberOfPages: number;
}

export const ShoppingLists = ({}) => {
    const [shoppingLists, setShoppingLists] = useState<ShoppingListPreview[]>();
    const [numberOfElements, setNumberOfElements] = useState<number>();
    const [numberOfPages, setNumberOfPages] = useState<number>();
    const [isCreateShoppingListModalOpen, setIsCreateShoppingListModalOpen] = useState<boolean>(false);
    const [reset, setReset] = useState<boolean>(false);

    useEffect(() => {
        authAxios
            .get<ShoppingListPagedResponse>(`${API_URL}/v1/shopping-lists?page=0`)
            .then((res) => {
                console.log(res)
                setShoppingLists(res.data.data);
                setNumberOfElements(res.data.numberOfElements);
                setNumberOfPages(res.data.numberOfPages);
            })
            .catch((e) => {
                throw new Error("Unhandled error: " + e)
            })
    }, [reset, setReset]);

    const addNewShoppingListRequest = async (body: any) => {
        const {data} = await authAxios.post<any>(`${API_URL}/v1/shopping-lists`, body, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        setIsCreateShoppingListModalOpen(false);
        setReset(true);
    }

    if (shoppingLists) {

        return (
            <Layout>
                <div className={'grid grid-cols-12 w-full h-full'}>

                    <div
                        className={"lg:col-start-5 lg:col-end-10  col-start-3 col-end-12 mt-10 flex lg:flex-row  flex-col gap-4 lg:gap-20 items-center"}>
                        <div>
                            <p className={"text-4xl text-white"}>Collection of shopping lists</p>
                        </div>

                        <div className={""}>
                            <Button
                                name={"Create new"}
                                color={"bg-green-600"}
                                hoverColor={"bg-green-500"}
                                type={"button"}
                                onClick={() => setIsCreateShoppingListModalOpen(true)}
                            />
                        </div>
                    </div>

                    <div
                        className={"lg:col-start-4 lg:col-end-10 col-start-2 col-end-12 content-center text-center flex flex-col gap-10 h-144"}>
                        <List shoppingLists={shoppingLists}/>
                    </div>

                    <Modal isOpen={isCreateShoppingListModalOpen}
                           onClose={() => setIsCreateShoppingListModalOpen(false)} title={"Create new shopping list"}>
                        <ShoppingListForm request={addNewShoppingListRequest}/>
                    </Modal>
                </div>
            </Layout>
        )

    } else {
        return null;
    }
}