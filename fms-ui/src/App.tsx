import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import './App.css'
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import Login from "./pages/Login.tsx";
import AdminRoot from "./pages/AdminRoot.tsx";
import ManagerRoot from "./pages/ManagerRoot.tsx";
import DriverRoot from "./pages/DriverRoot.tsx";
import AdminCars from "./pages/Admin/Cars/AdminCars.tsx";
import AdminCarDetails from "./pages/Admin/Cars/AdminCarDetails.tsx";
import AdminUsers from "./pages/Admin/Users/AdminUsers.tsx";
import AdminUserDetails from "./pages/Admin/Users/UserDetails.tsx";
import AdminNewUser from "./pages/Admin/Users/NewUser.tsx";
import AdminNewCar from "./pages/Admin/Cars/NewCar.tsx";
import AdminAssignments from "./pages/Admin/Assignments/AdminAssignments.tsx";
import {NewAssignment as AdminNewAssignment} from "./pages/Admin/Assignments/NewAssignment.tsx";
import ManagerCars from "./pages/Manager/Cars/ManagerCars.tsx";
import ManagerUserDetails from "./pages/Manager/Drivers/UserDetails.tsx";
import ManagerNewUser from "./pages/Manager/Drivers/NewUser.tsx";
import ManagerNewCar from "./pages/Manager/Cars/NewCar.tsx";
import {NewAssignment as ManagerNewAssignment} from "./pages/Manager/Assignments/NewAssignment.tsx";
import ManagerUsers from "./pages/Manager/Drivers/ManagerUsers.tsx";

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
                        element={<AdminCarDetails />}
                    />
                    <Route
                        path="cars/new"
                        element={<AdminNewCar />}
                    />
                    <Route
                        path="users"
                        element={<AdminUsers/>}
                    />
                    <Route
                        path="users/:id"
                        element={<AdminUserDetails />}
                    />
                    <Route
                        path="users/new"
                        element={<AdminNewUser />}
                    />
                    <Route
                        path="assignments"
                        element={<AdminAssignments />}
                    />
                    <Route
                        path="assignments/new"
                        element={<AdminNewAssignment />}
                    />
                </Route>

                <Route
                    path="/manager/*"
                    element={<ProtectedRoute role="manager" component={ManagerRoot}/>}
                >
                    <Route
                        path="cars"
                        element={<ManagerCars />}
                    />
                    <Route
                        path="cars/:id"
                        element={<AdminCarDetails />}
                    />
                    <Route
                        path="cars/new"
                        element={<ManagerNewCar />}
                    />
                    <Route
                        path="drivers"
                        element={<ManagerUsers/>}
                    />
                    <Route
                        path="drivers/:id"
                        element={<ManagerUserDetails />}
                    />
                    <Route
                        path="drivers/new"
                        element={<ManagerNewUser />}
                    />
                    <Route
                        path="assignments"
                        element={<AdminAssignments />}
                    />
                    <Route
                        path="assignments/new"
                        element={<ManagerNewAssignment />}
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
