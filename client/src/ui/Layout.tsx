import React from "react";
import styled from "styled-components";
import { useUserContext } from "../context/UserContext";
import { Navigate } from "react-router";

type Props = {
  children?: React.ReactNode;
};

const StyledLayout = styled.div`
  border: 1px solid red;
`;

export const Layout: React.FC<Props> = ({ children }) => {
  const { user } = useUserContext();
  return user ? (
    <StyledLayout>{children}</StyledLayout>
  ) : (
    <Navigate to="/login" />
  );
};
