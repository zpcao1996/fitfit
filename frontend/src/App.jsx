import { Routes, Route, Navigate } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import Login from './pages/Login';
import MainLayout from './components/MainLayout';
import Dashboard from './pages/Dashboard';
import Projects from './pages/Projects';
import Researchers from './pages/Researchers';
import Publications from './pages/Publications';
import Milestones from './pages/Milestones';
import Announcements from './pages/Announcements';
import Expenditures from './pages/Expenditures';

function PrivateRoute({ children }) {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" />;
}

export default function App() {
  return (
    <ConfigProvider locale={zhCN} theme={{
      token: {
        colorPrimary: '#1677ff',
        borderRadius: 6,
      },
    }}>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={
          <PrivateRoute><MainLayout /></PrivateRoute>
        }>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="projects" element={<Projects />} />
          <Route path="researchers" element={<Researchers />} />
          <Route path="publications" element={<Publications />} />
          <Route path="milestones" element={<Milestones />} />
          <Route path="announcements" element={<Announcements />} />
          <Route path="expenditures" element={<Expenditures />} />
        </Route>
      </Routes>
    </ConfigProvider>
  );
}
