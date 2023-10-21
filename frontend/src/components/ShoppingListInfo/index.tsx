type ShoppingListInfoProps = {
    field: string;
    text: string;
}
export const ShoppingListInfo = ({field, text}: ShoppingListInfoProps) => {
    return (
        <div className={"flex flex-row gap-4"}>
            <p className={"text-white text-xl"}>{field + ':'} </p>
            <p className={"text-white text-xl"}>{text}</p>
        </div>
    )
}