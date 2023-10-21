export type ButtonProps = {
    name: string;
    color: string;
    hoverColor: string;
    onClick?: () => void;
    type: "button" | "submit";
    additionalClass?: string;
}
export const Button = ({name, color, hoverColor, onClick, type, additionalClass}: ButtonProps) => {

    return (
        <button
            type={type}
            onClick={onClick}
            className={`hover:${hoverColor} ${color} text-gray-50 rounded p-3 lg:h-12 lg:w-40 w-52 ${additionalClass}`}>
            {name}
        </button>
    )
}