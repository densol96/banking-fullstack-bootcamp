import React, { createContext, useContext, useEffect, useState } from "react";
import { useUserContext } from "./UserContext";
import { Account } from "../types/User";

type AccountContextType = {
  activeAccountId: number | null;
  setActiveAccountId: (value: number) => void;
  activeAccount: Account | null;
};

const AccountContext = createContext<AccountContextType>({
  activeAccountId: null,
  setActiveAccountId: () => {},
  activeAccount: null,
});

type Props = {
  children?: React.ReactNode;
};

const AccountProvider: React.FC<Props> = ({ children }) => {
  const { user } = useUserContext();

  const [activeAccountId, setActiveAccountId] = useState<number | null>(() =>
    user.profile.accounts.length > 0 ? user.profile.accounts[0].id : null
  );

  const activeAccount = user.profile.accounts.find(
    (acc) => acc.id === activeAccountId
  );

  return (
    <AccountContext.Provider
      value={{ activeAccountId, setActiveAccountId, activeAccount }}
    >
      {children}
    </AccountContext.Provider>
  );
};

function useAccountContext() {
  const context = useContext(AccountContext);
  if (context === undefined)
    throw new Error("AccountContext must be used within a AccountProvider");

  return context;
}

export { AccountProvider, useAccountContext, AccountContextType };
