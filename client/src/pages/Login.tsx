import React from "react";
import styled from "styled-components";
import { LoginForm } from "../features/auth/LoginForm";
import Heading from "../ui/Heading";

type Props = {
  className?: string;
  children?: React.ReactNode;
};

const StyledLogin = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  padding: 2.5rem 5rem;
  transform: translate(-50%, -50%);
  text-align: center;

  h2 {
    margin-bottom: 2rem;
  }
`;

const Logo = styled.img`
  width: 20rem;
  border-radius: 50%;
`;

export const Login: React.FC<Props> = () => {
  return (
    <StyledLogin>
      <Logo src="Untitled.png" />
      <Heading as="h2">Login page</Heading>
      <LoginForm />
    </StyledLogin>
  );
};
