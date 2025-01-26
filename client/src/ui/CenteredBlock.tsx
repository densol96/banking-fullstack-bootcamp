import React from "react";
import styled from "styled-components";
import { LoginForm } from "../features/auth/LoginForm";
import Heading from "./Heading";

const Container = styled.div`
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

type Props = {
  title: string;
  children: React.ReactNode;
};

export const CenteredBlock: React.FC<Props> = ({ title, children }) => {
  return (
    <Container>
      <Logo src="Untitled.png" />
      <Heading as="h2">{title}</Heading>
      {children}
    </Container>
  );
};
