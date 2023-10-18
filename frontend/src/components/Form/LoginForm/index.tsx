import {useForm} from "react-hook-form";
import {FormValues, LoginResponse} from "./index.props";
import {useState} from "react";
import axios from "axios";
import {setAccessToken, setRefreshToken} from "../../../security/tokenStorage";
import {Input} from "../Input";
import {SubmitButton} from "../SubmitButton";
import {API_URL} from "../../../config";

export const LoginForm = ({}) => {

    const {register, handleSubmit} = useForm<FormValues>();
    const [error, setError] = useState<string | null>(null);


    const onSubmit = async (body: any) => {
        try {
            const {data} = await axios.post<LoginResponse>(`${API_URL}/v1/auth/access_token`, body);
            setAccessToken(data.accessToken);
            setRefreshToken(data.refreshToken);
            window.location.href = '/';
        } catch (err: any) {
            if (err.response.status === 401) {
                console.log(err)
                setError(err.response.data.message);
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
            />

            <Input
                name={'password'}
                type={'password'}
                label={'Password'}
                required={true}
                register={register}
                placeholder={'Enter your password'}
                error={error}
                width={'w-full'}
            />

            <SubmitButton value={'Sign in'}/>
        </form>

    )
}