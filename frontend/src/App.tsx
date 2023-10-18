import React from 'react';
import './App.css';
import {createBrowserRouter, createRoutesFromElements, Route, RouterProvider,} from "react-router-dom";
import {ProtectedRoute} from "./components/ProtectedRoute";
import {Register} from "./pages/register";
import {Login} from "./pages/login";
import {getAccessToken} from "./security/tokenStorage";

function App() {

    const isAuthorized = !!getAccessToken();
    const router = createBrowserRouter(
        createRoutesFromElements(
            <Route path={'/'}>
                <Route
                    path={''}
                    element={
                        <ProtectedRoute isAuthorized={isAuthorized} redirect={'login'}>
                            <p>"XDD"</p>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path={'login'}
                    element={
                        <ProtectedRoute isAuthorized={true} redirect={'/'}>
                            <Login/>
                        </ProtectedRoute>
                    }/>
                <Route
                    path={'register'}
                    element={
                    <ProtectedRoute isAuthorized={true} redirect={'/'}>
                        <Register/>
                    </ProtectedRoute>
                    }/>
            </Route>
        )
    )

    return <RouterProvider router={router}/>
}


export default App;
