import { Transaction } from "../../types/Transaction";
import { useAccountContext } from "../../context/AccountContext";
import { useUserContext } from "../../context/UserContext";
import { headersWithToken } from "../../helpers/headersWithToken";
import axios from "axios";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import {
  getConciseDateTime,
  getFormattedDateTime,
} from "../../helpers/formattedDateTime";
import { FaPlusCircle, FaMinusCircle } from "react-icons/fa";
import { formatBalance } from "../../helpers/formatBalance";

type Props = {};

const Transactions = styled.table`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const TransactionLine = styled.tr`
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.4rem;
  font-weight: 700;
`;

const Type = styled.td<{ color: string }>`
  background-color: var(--color-${(props) => props.color});
  border-radius: 12px;
  padding: 0.3rem 0.5rem;
  font-size: 1.2rem;
  font-weight: 700;
  color: #423131;
`;

const TransferSpecifier = styled.td`
  display: flex;
  align-items: center;
  gap: 3rem;
`;

const Td = styled.td`
  display: flex;
  align-items: center;
`;

export const TransactionHistory: React.FC<Props> = () => {
  const { jwt } = useUserContext();
  const { activeAccountId, activeAccount } = useAccountContext();
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  async function fetchTransactions() {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/transactions/${activeAccountId}`;
    try {
      const response = await axios.get(API_ENDPOINT, headersWithToken(jwt));
      setTransactions(response.data);
    } catch (e) {
      console.log("UNABLE TO LOAD TRANSACTIONS!");
      console.log(e);
    }
  }

  useEffect(() => {
    fetchTransactions();
  });

  return (
    <Transactions>
      {transactions.map((transaction) => {
        let color;
        if (transaction.type === "DEPOSIT") {
          color = "primary";
        } else if (transaction.type === "WITHDRAW") {
          color = "tertiary";
        } else {
          color = "secondary";
        }

        return (
          <TransactionLine>
            <TransferSpecifier>
              <Type color={color}>{transaction.type}</Type>
              {transaction.type === "TRANSFER" &&
                (transaction.toAccountNumber === activeAccount.accountNumber ? (
                  <FaPlusCircle />
                ) : (
                  <FaMinusCircle />
                ))}
            </TransferSpecifier>
            <Td>{getConciseDateTime(transaction.transactionDateTime)}</Td>
            <Td>{formatBalance(transaction.amount)}</Td>
          </TransactionLine>
        );
      })}
    </Transactions>
  );
};
