import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import GlobalStyles from "./styles/GlobalStyles";
import { Login } from "./pages/Login";
import { Home } from "./pages/Home";
import { UserProvider } from "./context/UserContext";
import { Layout } from "./ui/Layout";

function App() {
  return (
    <UserProvider>
      <GlobalStyles />
      <BrowserRouter>
        <Routes>
          <Route path="login" element={<Login />} />
          <Route path="/" element={<Layout />}>
            <Route index element={<Login />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </UserProvider>
  );
}

export default App;
