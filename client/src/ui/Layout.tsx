import React from "react";
import styled from "styled-components";
import { useUserContext } from "../context/UserContext";
import { Navigate, Outlet } from "react-router";
import { Header } from "./Header";
import { AccountProvider } from "../context/AccountContext";

type Props = {
  children?: React.ReactNode;
};

const StyledLayout = styled.div`
  /* border: 1px solid red; */
  max-width: 120rem;
  width: 100%;
  min-height: 100vh;
  margin: 0 auto;
  padding: 1rem 0;
`;

export const Layout: React.FC<Props> = ({ children }) => {
  const { user } = useUserContext();
  return user ? (
    <AccountProvider>
      <StyledLayout>
        <Header />
        <Outlet />
      </StyledLayout>
    </AccountProvider>
  ) : (
    <Navigate to="/login" />
  );
};
