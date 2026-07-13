import {
  BrowserRouter,
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import LoginPage from "./features/auth/pages/LoginPage";
import ProtectedRoute from "./features/auth/components/ProtectedRoute";
import DashboardPage from "./features/dashboard/pages/DashboardPage";
import RegisterDoctorPage from "./features/doctor/pages/RegisterDoctorPage";
import RegisterPatientPage from "./features/patient/pages/RegisterPatientPage";
import SearchPatientPage from "./features/patient/pages/SearchPatientPage";
import MedicalAgendaPage from "./features/agenda/pages/MedicalAgendaPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={<Navigate to="/login" replace />}
        />

        <Route
          path="/login"
          element={<LoginPage />}
        />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/doctors/new"
          element={
            <ProtectedRoute>
              <RegisterDoctorPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/patients/new"
          element={
            <ProtectedRoute>
              <RegisterPatientPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/patients/search"
          element={
            <ProtectedRoute>
              <SearchPatientPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/agenda"
          element={
            <ProtectedRoute>
              <MedicalAgendaPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="*"
          element={<Navigate to="/login" replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;