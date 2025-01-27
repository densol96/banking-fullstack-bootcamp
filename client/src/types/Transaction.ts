export type Transaction = {
  id: number;
  fromAccountNumber: string;
  toAccountNumber: string;
  amount: number;
  transactionDateTime: string;
  errorMessage: string | null;
  status: "SUCCESS" | "FAILURE";
  type: "TRANSFER" | "DEPOSIT" | "WITHDRAW";
};
