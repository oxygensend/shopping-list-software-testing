interface ListElementProps {
    id: string;
    title: string;
    completed: boolean
}

export const ListElement = ({id, title, completed}: ListElementProps) => {

    const onClickEventHandler = () => {
        window.location.href = '/shopping-lists/' + id;
    }

    return (
        <div
            data-testid={"list-element"}
            className={"relative flex flex-row justify-center bg-yellow-400 hover:bg-yellow-300 w-full p-3 rounded-md align-middle border-b-black cursor-pointer"}
            onClick={() => onClickEventHandler()}>
            <p className={"text-xl"}>{title}</p>
            {completed ?
                <div className={"absolute h-0.5 w-5/6 bg-black top-1/2"}></div>
                : null}
        </div>
    )
}