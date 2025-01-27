import React, {
  createContext,
  useContext,
  useEffect,
  useRef,
  useState,
} from "react";
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

  const selectActiveAccountId = () =>
    user.profile.accounts.length > 0 ? user.profile.accounts[0].id : null;

  const [activeAccountId, setActiveAccountId] = useState<number | null>(
    selectActiveAccountId
  );

  const accountsRef = useRef(user.profile.accounts.length);

  useEffect(() => {
    const length = user.profile.accounts.length;
    if (length < accountsRef.current) {
      setActiveAccountId(
        length !== 0 ? user.profile.accounts[length - 1].id : 0
      );
      accountsRef.current--;
    } else if (length > accountsRef.current) {
      setActiveAccountId(user.profile.accounts[length - 1].id);
      accountsRef.current++;
    }
  }, [user.profile.accounts.length]);

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
