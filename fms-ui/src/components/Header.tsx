import React from "react";
import {UserRole} from "../types/User.ts";
import {MdLogout} from "react-icons/md";
import {useNavigate} from "react-router-dom";

interface LayoutProps {
    children: React.ReactNode;
    role: UserRole;
}

const Header: React.FC<LayoutProps> = ({ children, role }) => {

    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.clear();
        navigate("/");
    }

    return (
        <header className="bg-blue-700 flex p-4 text-white justify-between text-lg ">
            <div>Zalogowany użytkownik: {localStorage.getItem("username")}</div>
            <div>Rola: {role}</div>
            <div>Moduł: {children}</div>
            <div />
            <button
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded flex align-baseline gap-3"
                onClick={handleLogout}
            >
                Wyloguj się <MdLogout size={30}/>
            </button>
        </header>
    )
}

export default Header;