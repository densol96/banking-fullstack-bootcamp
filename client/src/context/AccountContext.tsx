import React, {
  createContext,
  useContext,
  useEffect,
  useRef,
  useState,
} from "react";
import { useUserContext } from "./UserContext";
import { Account } from "../types/User";
import { headersWithToken } from "../helpers/headersWithToken";
import toast from "react-hot-toast";
import { catchBlockSpecial } from "../helpers/catchBlockSpecial";
import axios from "axios";

type AccountContextType = {
  activeAccountId: number | null;
  setActiveAccountId: (value: number) => void;
  activeAccount: Account | null;
  createAccount: () => void;
  deleteAccount: () => void;
};

const AccountContext = createContext<AccountContextType>({
  activeAccountId: null,
  setActiveAccountId: () => {},
  activeAccount: null,
  createAccount: () => {},
  deleteAccount: () => {},
});

type Props = {
  children?: React.ReactNode;
};

const AccountProvider: React.FC<Props> = ({ children }) => {
  const { user, refreshUser, jwt, logout } = useUserContext();

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

  async function deleteAccount() {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/accounts/${activeAccountId}/delete`;
    try {
      const response = await axios.delete(API_ENDPOINT, headersWithToken(jwt));
      refreshUser();
      toast.success(response.data.message);
    } catch (e) {
      catchBlockSpecial(e, logout, false);
    }
  }

  async function createAccount() {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/accounts/create`;
    try {
      const response = await axios.post(
        API_ENDPOINT,
        {},
        headersWithToken(jwt)
      );
      refreshUser();
      toast.success(response.data.message);
    } catch (e) {
      catchBlockSpecial(e, logout, false);
    }
  }

  return (
    <AccountContext.Provider
      value={{
        activeAccountId,
        setActiveAccountId,
        activeAccount,
        deleteAccount,
        createAccount,
      }}
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
