import React from "react";
import {UserRole} from "../types/User.ts";
import {MdLogout} from "react-icons/md";

interface LayoutProps {
    children: React.ReactNode;
    role: UserRole;
}

const Header: React.FC<LayoutProps> = ({ children, role }) => {
    return (
        <header className="bg-blue-700 flex p-4 text-white justify-between text-lg">
            <div>Zalogowany użytkownik: &lt;&lt;username&gt;&gt;</div>
            <div>Rola: {role}</div>
            <div>Moduł: {children}</div>
            <div />
            <div className="flex align-baseline gap-3"><div>Wyloguj się...</div> <MdLogout size={30}/></div>
        </header>
    )
}

export default Header;