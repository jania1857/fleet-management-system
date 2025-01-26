import React, {useEffect, useState} from "react";
import {UserDto} from "../../../generated-client";
import {useParams} from "react-router-dom";
import {adminApi} from "../../../api/apiClient.ts";
import image from "../../../assets/placeholder.jpg";

const UserDetails: React.FC = () => {

    const [users, setUsers] = useState<UserDto[]>([]);
    const [user, setUser] = useState<UserDto | null>(null);
    const [loading, setLoading] = useState(true);

    const {id} = useParams<{ id: string }>();
    if (!id) {
        return (
            <h1 className="text-5xl text-red-500">Nie przekazano ID użytkownika</h1>
        )
    }
    const numberId: number = +id;

    useEffect(() => {
        adminApi.getAllUsers()
            .then((response) => {
                setUsers(response.data);
                const foundUser = response.data.find(user => user.id === numberId);
                setUser(foundUser || null);
            })
            .catch((error) => {
                console.error(error);
            })
            .finally(() => setLoading(false))
    }, [])

    console.log(users)
    console.log(user)

    if (loading) {

        return (
            <p>Ładowanie...</p>
        )
    }

    if (!user) {
        setLoading(false);
        return (
            <h1 className="text-5xl text-red-500">Nie znaleziono użytkownika o takim ID</h1>
        )
    }


    return (
        <>
            <h1 className="text-5xl text-left font-bold">{user.firstname} {user.lastname}</h1>
            <hr className="border-gray-800"/>
            <div className="text-left">
                <h3 className="text-xl text-left">ID: {user.id} | Login: {user.username}</h3>
            </div>
            <div className="flex text-left gap-20">
                <img
                    src={image}
                    alt=""
                    className="w-full max-w-sm rounded-lg border border-gray-200 shadow-md"
                />
                <table>
                    <tbody>
                    <tr>
                        <td className="font-bold">Imię:</td>
                        <td>{user.firstname}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Nazwisko:&nbsp;</td>
                        <td>{user.lastname}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Login:</td>
                        <td>{user.username}</td>
                    </tr>
                    <tr>
                        <td className="font-bold">Rola:</td>
                        <td>{user.role}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <hr className="mt-5 mb-5 border-gray-800"/>

        </>
    )
}

export default UserDetails;