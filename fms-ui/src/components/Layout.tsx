import React from "react";
import Sidebar from "./Sidebar.tsx";
import {Outlet} from "react-router-dom";
import {UserRole} from "../types/User.ts";
import Header from "./Header.tsx";

interface LayoutProps {
    role: UserRole;
}

const Layout: React.FC<LayoutProps> = ({role}) => {
    let module: string;

    switch (role) {
        case "admin":
            module = "Admin";
            break;
        case "manager":
            module = "Manager";
            break;
        case "driver":
            module = "Driver";
            break;
        default:
            module = "Unknown";
    }

    return (
        <>
            <Header role={role}>{module}</Header>
            <div className="flex">
                <Sidebar role={role}/>
                <main className="flex-1 p-4">
                    <Outlet/>
                </main>
            </div>
        </>
    )
}

export default Layout;