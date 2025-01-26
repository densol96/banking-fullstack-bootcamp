import React, {
  useCallback,
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";

import useLocalStorageAsState from "../hooks/useLocalStorageAsState";
import { Spinner } from "../ui/Spinner";
import { User } from "../types/User";
import axios from "axios";

type UserContextType = { user: User | null };

const UserContext = createContext<UserContextType>({ user: null });

type Props = {
  children?: React.ReactNode;
};

const API_ENDPOINT = "http://localhost:8080/api/v1/auth/users/iam";

const UserProvider: React.FC<Props> = ({ children }) => {
  let {
    state: jwt,
    updateLocalStorage: updateJwt,
    deleteFromLocalStorage: logout,
  } = useLocalStorageAsState<string>("jwt", "");

  const [user, setUser] = useState<User | null>(null);
  const [isReady, setIsReady] = useState(false);

  async function identifyJwt() {
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
    <UserContext.Provider value={{ user }}>
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
