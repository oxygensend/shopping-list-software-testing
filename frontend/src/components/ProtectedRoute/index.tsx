import {ProtectedRouteProps} from './index.props';
import {Navigate, Outlet} from 'react-router-dom';

export const ProtectedRoute = ({isAuthorized, redirect, children}: ProtectedRouteProps) => {
    if (!isAuthorized) {
        return redirect ? <Navigate to={redirect}/> : <Navigate to={'/'}/>;
    } else {
        return children ? children : <Outlet/>;
    }
};