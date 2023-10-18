import {ReactElement} from "react";

export interface ProtectedRouteProps {
    isAuthorized: boolean,
    redirect: string,
    children?: ReactElement
}