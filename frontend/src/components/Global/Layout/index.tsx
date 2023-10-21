import {Navbar} from "../Navbar";
import {SessionTimeout} from "../SessionTimeout";

type LayoutProps = {
    children: React.JSX.Element | null;
}
export const Layout = ({children}: LayoutProps) => {
    return (
        <div className={'grid grid-cols-12  h-full w-full'}>
            <SessionTimeout/>
            <Navbar/>
            <div className={"col-start-1 col-span-12 h-full w-full mt-20"}>
                {children}
            </div>

        </div>
    )

}