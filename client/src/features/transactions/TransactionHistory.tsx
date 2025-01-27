import { Transaction } from "../../types/Transaction";
import { useAccountContext } from "../../context/AccountContext";
import { useUserContext } from "../../context/UserContext";
import { headersWithToken } from "../../helpers/headersWithToken";
import axios from "axios";
import React, { useEffect, useState } from "react";

type Props = {};

export const TransactionHistory: React.FC<Props> = () => {
  const { jwt } = useUserContext();
  const { activeAccountId } = useAccountContext();
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
    <div>
      {transactions.map((transaction) => (
        <p>{`${transaction.type} - ${transaction.transactionDateTime} - ${transaction.amount} EUR`}</p>
      ))}
    </div>
  );
};
