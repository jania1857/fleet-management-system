import React from 'react';
import {NavLink} from "react-router-dom";
import {UserRole} from '../types/User';

interface SidebarProps {
    role: UserRole;
}

const Sidebar: React.FC<SidebarProps> = ({role}) => {
    const links: Record<UserRole, { name: string; path: string }[]> = {
        admin: [
            {name: "Dashboard", path: "/admin/dashboard"},
            {name: "Użytkownicy", path: "/admin/users"},
            {name: "Pojazdy", path: "/admin/cars"},
            {name: "Przypisania", path: "/admin/assignments"},

        ],
        manager: [
            {name: "Dashboard", path: "/manager/dashboard"},
            {name: "Kierowcy", path: "/manager/drivers"},
            {name: "Pojazdy", path: "/manager/cars"},
            {name: "Przypisania", path: "/manager/assignments"},
        ],
        driver: [
            {name: "Dashboard", path: "/driver/dashboard"}
        ]
    };

    return (
        <aside className="w-64 h-screen bg-gray-800 text-white p-3">
            <h1 className="font-bold text-3xl">MENU</h1>
            <hr className="m-3"/>
            <ul>
                {links[role].map((link) => (
                    <li key={link.path}>
                        <NavLink
                            to={link.path}
                            className="block px-4 py-2 hover:bg-gray-700 transition"
                        >
                            {link.name}
                        </NavLink>
                    </li>
                ))}
            </ul>
        </aside>
    );
};

export default Sidebar;