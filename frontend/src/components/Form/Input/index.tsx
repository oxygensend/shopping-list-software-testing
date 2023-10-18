import {InputProps} from "./index.props";
import {Error} from "../Error";

export const Input = ({
                          name,
                          label,
                          type,
                          required,
                          placeholder,
                          register,
                          className,
                          error,
                          width,
                          defaultValue,
                          step,
                      }: InputProps) => {
    return (
        <div className={`flex flex-col gap-1 ${width ? width : 'w-5/6'}`}>
            <label htmlFor={name} className={'text-left text-blue-50 font-semibold'}>{label}</label>
            <input
                id={name}
                {...register(name)}
                name={name}
                type={type}
                step={step}
                required={required}
                defaultValue={defaultValue}
                className={
                    className ??
                    `py-2.5 px-0  bg-transparent border-0 border-b-2 appearance-none text-gray-400 border-gray-700 focus:outline-none focus:ring-0 focus:border-gray-200 peer`
                }
                placeholder={placeholder}
            />
            {error && <Error error={error}/>}
        </div>
    );
};