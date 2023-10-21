import {useForm} from "react-hook-form";
import {useState} from "react";
import {API_URL} from "../../../config";
import {setAccessToken, setRefreshToken} from "../../../security/tokenStorage";
import {Input} from "../Input";
import {SubmitButton} from "../SubmitButton";
import {FormValues, RegisterResponse} from "./index.props";
import axios from "axios";
import {ExceptionType, SubExceptionType} from "../../../types";
import {findPropertyViolation} from "../../../utils/findPropertyViolations";

export const RegisterForm = ({}) => {

    const {register, handleSubmit} = useForm<FormValues>();
    const [errors, setErrors] = useState<SubExceptionType[]>([]);


    const onSubmit = async (body: FormValues) => {
        try {
            const {data} = await axios.post<RegisterResponse>(`${API_URL}/v1/auth/register`, body);
            setAccessToken(data.accessToken);
            setRefreshToken(data.refreshToken);
            window.location.href = '/';
        } catch (err: any) {
            console.log(err)
            if (err.response.status === 400) {
                const response: ExceptionType = err.response.data;
                setErrors(response.subExceptions);
            } else {
                throw Error('Invalid exception occurred');
            }
        }
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} className={"flex flex-col gap-2 items-center"}>
            <Input
                name={'email'}
                label={'Email'}
                type={'email'}
                required={true}
                register={register}
                placeholder={'Enter your email'}
                width={'w-full'}
                error={findPropertyViolation(errors, 'email')}
            />

            <Input
                name={'firstName'}
                label={'Firstname'}
                type={'text'}
                required={true}
                register={register}
                placeholder={'Enter you firstname'}
                width={'w-full'}
                error={findPropertyViolation(errors, 'firstName')}
            />
            <Input
                name={'lastName'}
                label={'Lastname'}
                type={'text'}
                required={true}
                register={register}
                placeholder={'Enter your lastname'}
                width={'w-full'}
                error={findPropertyViolation(errors, 'lastName')}
            />

            <Input
                name={'password'}
                type={'password'}
                label={'Password'}
                required={true}
                register={register}
                placeholder={'Enter your password'}
                error={findPropertyViolation(errors, 'password')}
                width={'w-full'}
            />

            <SubmitButton value={'Sign up'}/>
        </form>

    )
}
