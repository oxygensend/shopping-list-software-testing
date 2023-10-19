import {RegisterForm} from "../../components/Form/RegisterForm";

export const Register = ({}) => {

    return (
        <div className={'grid h-screen place-items-center'}>
            <div className={'lg:w-1/4'}>
                <RegisterForm/>
            </div>
        </div>
    )
}
