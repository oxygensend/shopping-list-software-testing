import {ListElement} from "./ListElement";
import {ShoppingListPreview} from "../../types";

export interface ListProps {
    shoppingLists: ShoppingListPreview[]
    parentClass?: string
}
export const List = ({shoppingLists, parentClass}: ListProps) => {

    return (
        <div className={"flex flex-col gap-2 rounded-md bg-yellow-400 h-full " + parentClass}>
            {shoppingLists?.map((shoppingList, i) => {
                return (
                        <ListElement
                            key={i}
                            id={shoppingList.id}
                            title={shoppingList.name}
                            completed={shoppingList.completed}
                        />
                )
            })}
        </div>
    )
}