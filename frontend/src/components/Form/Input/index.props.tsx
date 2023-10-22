import {UseFormRegister} from "react-hook-form";
import {ErrorType} from "../../../types";

export interface InputProps {
    name: string;
    type: 'text' | 'number' | 'password' | 'date' | 'email' | 'checkbox' | 'datetime-local' | 'file';
    label: string;
    required: boolean;
    placeholder?: string;
    register: UseFormRegister<any>;
    className?: string;
    error?: string | null | ErrorType;
    width?: string;
    defaultValue?: string;
    step?: number;
    onChange?: (e: any) => void;
    accept?: string;
}