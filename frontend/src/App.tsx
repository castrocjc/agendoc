import {
  BrowserRouter,
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import AccountHomePage from "./features/account/pages/AccountHomePage";
import MedicalAgendaPage from "./features/agenda/pages/MedicalAgendaPage";
import AppointmentAgendaPage from "./features/appointment/pages/AppointmentAgendaPage";
import AppointmentPage from "./features/appointment/pages/AppointmentPage";
import ProtectedRoute from "./features/auth/components/ProtectedRoute";
import RoleHomeRedirect from "./features/auth/components/RoleHomeRedirect";
import LoginPage from "./features/auth/pages/LoginPage";
import DashboardPage from "./features/dashboard/pages/DashboardPage";
import RegisterDoctorPage from "./features/doctor/pages/RegisterDoctorPage";
import RegisterPatientPage from "./features/patient/pages/RegisterPatientPage";
import SearchPatientPage from "./features/patient/pages/SearchPatientPage";
import PublicReceptionPage from "./features/publicReception/pages/PublicReceptionPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={<RoleHomeRedirect />}
        />

        <Route
          path="/login"
          element={<LoginPage />}
        />

        <Route
          path="/c/:clinicSlug"
          element={<PublicReceptionPage />}
        />

        <Route
          path="/account"
          element={
            <ProtectedRoute
              allowedRoles={["PATIENT", "DOCTOR"]}
            >
              <AccountHomePage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <DashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/doctors/new"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <RegisterDoctorPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/patients/new"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <RegisterPatientPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/patients/search"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <SearchPatientPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/agenda"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <MedicalAgendaPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/appointments"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <AppointmentAgendaPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/appointments/new"
          element={
            <ProtectedRoute
              allowedRoles={["RECEPTIONIST"]}
            >
              <AppointmentPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="*"
          element={<Navigate to="/" replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
