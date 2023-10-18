import {LoginForm} from "../../components/Form/LoginForm";

export const Login = ({}) => {
    return (
        <div className={'grid h-screen place-items-center'}>
            <div className={'lg:w-1/4'}>
                <LoginForm />
            </div>
        </div>
    );
}