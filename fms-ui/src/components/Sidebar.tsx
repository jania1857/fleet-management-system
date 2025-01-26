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
            {name: "Users", path: "/admin/users"},
            {name: "Cars", path: "/admin/cars"},
            {name: "Settings", path: "/admin/settings"},

        ],
        manager: [
            {name: "Dashboard", path: "/manager/dashboard"},
            {name: "Orders", path: "/manager/orders"},
            {name: "Settings", path: "/manager/settings"}
        ],
        driver: [
            {name: "Dashboard", path: "/driver/dashboard"},
            {name: "Deliveries", path: "/driver/deliveries"},
            {name: "Settings", path: "/driver/settings"}
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
                            className="block px-4 py-2 hover:bg-gray-700"
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