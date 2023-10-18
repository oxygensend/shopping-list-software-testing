import {ErrorProps} from "./index.props";


export const Error = ({ error }: ErrorProps) => {
    if (typeof error === 'string') {
        return <p className={'font-medium tracking-wide text-red-500 text-xs'}>{error}</p>;
    } else {
        return (
            <>
                {Object.values(error).map((err: any, i) => {
                    return (
                        <p className={'font-medium tracking-wide text-red-500 text-xs'} key={i}>
                            {err}
                        </p>
                    );
                })}
            </>
        );
    }
};