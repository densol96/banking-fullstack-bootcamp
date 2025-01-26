import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import GlobalStyles from "./styles/GlobalStyles";
import { Login } from "./pages/Login";
import { Home } from "./pages/Home";
import { UserProvider } from "./context/UserContext";
import { Layout } from "./ui/Layout";
import { Register } from "./pages/Register";
import { ForgotPassword } from "./pages/ForgotPassword";
import { Toaster } from "react-hot-toast";
import { AccountProvider } from "./context/AccountContext";

function App() {
  return (
    <UserProvider>
      <GlobalStyles />
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/" element={<Layout />}>
            <Route index element={<Navigate to="home" />} />
            <Route path="home" element={<Home />} />
          </Route>
        </Routes>
      </BrowserRouter>
      <Toaster position="top-center" reverseOrder={false} />
    </UserProvider>
  );
}

export default App;
