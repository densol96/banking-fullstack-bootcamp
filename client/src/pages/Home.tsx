import React from "react";
import { useUserContext } from "../context/UserContext";
import styled from "styled-components";
import { useAccountContext } from "../context/AccountContext";
import Heading from "../ui/Heading";
import { getFormattedDateTime } from "../helpers/formattedDateTime";
import { formatBalance } from "../helpers/formatBalance";
import { TransactionHistory } from "../features/transactions/TransactionHistory";
import { Actions } from "../features/transactions/Actions";

type Props = {
  className?: string;
  children?: React.ReactNode;
};

const BalanceSection = styled.div`
  margin-top: 3rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const BalanceDateText = styled.div``;

const BalanceTotal = styled.div``;

const MainSection = styled.section`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10rem;
  border: 1px solid red;
`;

export const Home: React.FC<Props> = () => {
  const { activeAccountId, activeAccount } = useAccountContext();

  console.log(activeAccount, activeAccountId);
  return (
    <>
      <BalanceSection>
        <BalanceDateText>
          <Heading as="h1">Current balance</Heading>
          <p>As of {getFormattedDateTime()}</p>
        </BalanceDateText>
        <BalanceTotal>
          <Heading as="h1">{formatBalance(activeAccount?.balance)}</Heading>
        </BalanceTotal>
      </BalanceSection>
      <MainSection>
        <TransactionHistory />
        <Actions />
      </MainSection>
    </>
  );
};
