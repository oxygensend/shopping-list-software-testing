type MinusPlusProps = {
    onMinusClick: () => void;
    onPlusClick?: () => void;
    position: 'horizontal' | 'vertical';
}
export const MinusPlus = ({onMinusClick, onPlusClick, position}: MinusPlusProps) => {

    const flex = {
        horizontal: 'flex-row',
        vertical: 'flex-col'
    }

    return (
        <div className={"flex gap-2  text-center " + flex[position]}>
            <p
                data-testid={'minus'}
                className={"text-white text-xl hover:text-blue-500 cursor-pointer"}
                onClick={onMinusClick}
            >-</p>
            {onPlusClick &&
                <p
                    data-testid={'plus'}
                    className={"text-white text-xl hover:text-blue-500 cursor-pointer"}
                    onClick={onPlusClick}
                >+</p>
            }
        </div>
    )

}