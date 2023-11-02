import {getAccessToken, removeTokens} from "../../../security/tokenStorage";

export const Navbar = ({}) => {
    const isAuthorized = !!getAccessToken();

    const onClickLogoutEvent = () => {
        removeTokens();
        window.location.href = '/login';
    }

    return (
        <nav className={"w-full bg-blue-600 h-16 flex flex-row fixed top-0 justify-between"}>

            <div className={"flex flex-row gap-10 mt-4 ml-10"}>
                <a href={'/'} className={"text-white hover:text-gray-400 no-underline"}>Main page</a>
            </div>

            {isAuthorized ?
                <div className={"mt-4 mr-10"}>
                    <p
                        onClick={() => onClickLogoutEvent()}
                        className={"cursor-pointer text-white hover:text-gray-400"}
                    >Logout</p>
                </div>
                :
                <div></div>
            }
        </nav>
    )
}