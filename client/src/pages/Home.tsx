import React from "react";
import { useUserContext } from "../context/UserContext";
import styled from "styled-components";
import { useAccountContext } from "../context/AccountContext";
import Heading from "../ui/Heading";
import { getFormattedDateTime } from "../helpers/formattedDateTime";
import { TransactionHistory } from "../features/transactions/TransactionHistory";
import { Actions } from "../features/transactions/Actions";
import { Button } from "../ui/Button";
import { formatBalance } from "../helpers/formatBalance";

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
  margin-top: 5rem;
`;

const Message = styled.div`
  margin-top: 20rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 3rem;

  button {
    align-self: center;
  }
`;

export const Home: React.FC<Props> = () => {
  const { user, hasAccounts } = useUserContext();
  const { activeAccountId, activeAccount, createAccount } = useAccountContext();

  if (hasAccounts)
    return (
      <>
        <BalanceSection>
          <BalanceDateText>
            <Heading as="h1">Current balance</Heading>
            <p>As of {getFormattedDateTime()}</p>
          </BalanceDateText>
          <BalanceTotal>
            <Heading as="h1">
              {formatBalance(activeAccount?.balance || 0)}
            </Heading>
          </BalanceTotal>
        </BalanceSection>
        <MainSection>
          <TransactionHistory />
          <Actions />
        </MainSection>
      </>
    );
  else
    return (
      <Message>
        <Heading>No active banking accounts</Heading>
        <Button onClick={createAccount} color="secondary">
          Create
        </Button>
      </Message>
    );
};
