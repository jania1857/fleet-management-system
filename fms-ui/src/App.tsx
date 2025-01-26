import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import './App.css'
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import Login from "./pages/Login.tsx";
import AdminRoot from "./pages/AdminRoot.tsx";
import ManagerRoot from "./pages/ManagerRoot.tsx";
import DriverRoot from "./pages/DriverRoot.tsx";
import AdminCars from "./pages/Admin/Cars/AdminCars.tsx";
import CarDetails from "./pages/Admin/Cars/CarDetails.tsx";
import AdminUsers from "./pages/Admin/Users/AdminUsers.tsx";
import UserDetails from "./pages/Admin/Users/UserDetails.tsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login/>}/>

                <Route
                    path="/admin/*"
                    element={<ProtectedRoute role="admin" component={AdminRoot}/>}
                >
                    <Route
                        path="cars"
                        element={<AdminCars />}
                    />
                    <Route
                        path="cars/:id"
                        element={<CarDetails />}
                    />
                    <Route
                        path="users"
                        element={<AdminUsers/>}
                    />
                    <Route
                        path="users/:id"
                        element={<UserDetails />}
                    />
                </Route>

                <Route
                    path="/manager/*"
                    element={<ProtectedRoute role="manager" component={ManagerRoot}/>}
                >
                    <Route
                        path="dashboard"
                        element={<h1>Dashboard</h1>}
                    />
                </Route>

                <Route
                    path="/driver/*"
                    element={<ProtectedRoute role="driver" component={DriverRoot}/>}
                >
                    <Route
                        path="dashboard"
                        element={<h1>Dashboard</h1>}
                    />
                </Route>
            </Routes>
        </Router>
    );
}

export default App
