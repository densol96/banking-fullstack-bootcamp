import React, { createContext, useContext, useEffect, useState } from "react";

import useLocalStorageAsState from "../hooks/useLocalStorageAsState";
import { Spinner } from "../ui/Spinner";
import { User } from "../types/User";
import axios from "axios";

type UserContextType = {
  user: User | null;
  logout: () => void;
  updateJwt: (jwt: string) => void;
  refreshUser: () => void;
  jwt: string;
};

const UserContext = createContext<UserContextType>({
  user: null,
  logout: () => {},
  updateJwt: (jwt: string) => {},
  refreshUser: () => {},
  jwt: "",
});

type Props = {
  children?: React.ReactNode;
};

const UserProvider: React.FC<Props> = ({ children }) => {
  let {
    state: jwt,
    updateLocalStorage: updateJwt,
    deleteFromLocalStorage: logout,
  } = useLocalStorageAsState<string>("jwt", "");

  const [user, setUser] = useState<User | null>(null);
  const [isReady, setIsReady] = useState(false);
  const [trigger, setTrigger] = useState<boolean>(true);

  function refreshUser() {
    setTrigger(!trigger);
  }

  async function identifyJwt() {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/auth/users/iam`;
    try {
      const response = await axios.get(API_ENDPOINT, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      setUser(response.data);
    } catch (e) {
      setUser(null);
    }
    setIsReady(true);
  }

  useEffect(() => {
    identifyJwt();
  }, [jwt]);

  return (
    <UserContext.Provider value={{ user, updateJwt, logout, refreshUser, jwt }}>
      {!isReady ? <Spinner /> : children}
    </UserContext.Provider>
  );
};

function useUserContext() {
  const context = useContext(UserContext);
  if (context === undefined)
    throw new Error("UserContext must be used within a UserProvider");

  return context;
}

export { UserProvider, useUserContext, UserContextType };
