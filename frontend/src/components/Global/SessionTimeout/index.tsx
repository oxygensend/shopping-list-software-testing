import React, {Fragment, useCallback, useEffect, useRef, useState} from "react";
import {getAccessToken, removeTokens} from "../../../security/tokenStorage";
import moment from 'moment/moment'

export const SessionTimeout = () => {
    const [events, setEvents] = useState(['click', 'load', 'scroll']);
    const isAuthenticated = getAccessToken();

    const warningInactiveInterval: any = useRef();
    const startTimerInterval: any = useRef();

    const resetTimer = useCallback(() => {
        clearTimeout(startTimerInterval.current);
        clearInterval(warningInactiveInterval.current);

        if (isAuthenticated) {
            const timeStamp = moment();
            sessionStorage.setItem('lastTimeStamp', timeStamp.toString());
        } else {
            clearInterval(warningInactiveInterval.current);
            sessionStorage.removeItem('lastTimeStamp');
        }
        timeChecker();
    }, [isAuthenticated]);

    const timeChecker = () => {
        startTimerInterval.current = setTimeout(() => {
            const storedTimeStamp = sessionStorage.getItem('lastTimeStamp');
            if (storedTimeStamp) {
                warningInactive(storedTimeStamp);
            }
        }, 900000);
    };

    useEffect(() => {
        events.forEach((event) => {
            window.addEventListener(event, resetTimer);
        });

        timeChecker();

        return () => {
            clearTimeout(startTimerInterval.current);
        };
    }, [resetTimer, events, timeChecker]);

    const warningInactive = (timeString: string) => {
        clearTimeout(startTimerInterval.current);

        warningInactiveInterval.current = setInterval(() => {
            const maxTime = 15; // Maximum ideal time given before logout

            const diff = moment.duration(moment().diff(moment(timeString)));
            const minPast = diff.minutes();

            if (minPast === maxTime) {
                clearInterval(warningInactiveInterval.current);
                sessionStorage.removeItem('lastTimeStamp');
                removeTokens();
            }
        }, 1000);
    };

    return <Fragment />;
};