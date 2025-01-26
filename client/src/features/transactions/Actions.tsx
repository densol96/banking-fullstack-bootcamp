import React from "react";
import { Deposit } from "./Deposit";
import { Withdrawal } from "./Withdrawal";
import styled from "styled-components";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

type Props = {};

export const Actions: React.FC<Props> = () => {
  return (
    <Container>
      <Deposit />
      <Withdrawal />
    </Container>
  );
};
