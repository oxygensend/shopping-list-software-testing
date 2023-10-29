import {Navigate, Outlet} from 'react-router-dom';
import {ReactElement} from "react";

export interface ProtectedRouteProps {
    isAuthorized: boolean,
    redirect: string,
    children?: ReactElement
}
export const ProtectedRoute = ({isAuthorized, redirect, children}: ProtectedRouteProps) => {
    if (!isAuthorized) {
        return redirect ? <Navigate to={redirect}/> : <Navigate to={'/'}/>;
    } else {
        return children ? children : <Outlet/>;
    }
};