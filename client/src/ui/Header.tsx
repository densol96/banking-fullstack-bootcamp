import { useUserContext } from "../context/UserContext";
import { BsBank2 } from "react-icons/bs";
import Heading from "./Heading";
import styled from "styled-components";
import { Button } from "./Button";
import { useAccountContext } from "../context/AccountContext";
import toast from "react-hot-toast";
import axios from "axios";
import { headersWithToken } from "../helpers/headersWithToken";
import { catchBlockSpecial } from "../helpers/catchBlockSpecial";
import { MdDelete } from "react-icons/md";
import { IoMdPersonAdd } from "react-icons/io";
import { useEffect } from "react";

const StyledHeader = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  div {
    display: flex;
    align-items: center;
    gap: 1rem;
  }
`;

const AccountGroup = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;

export const Header = () => {
  const { activeAccountId, setActiveAccountId, createAccount, deleteAccount } =
    useAccountContext();
  const { user, logout, hasAccounts } = useUserContext();
  const accounts = user.profile.accounts;

  return (
    <StyledHeader>
      <div>
        <BsBank2 size={25} />
        <Heading as="h2">
          Welcome back, {user.firstName} {user.lastName}
        </Heading>
      </div>
      <div>
        {hasAccounts && (
          <AccountGroup>
            <select
              onChange={(e) => setActiveAccountId(+e.target.value)}
              value={activeAccountId}
            >
              {accounts.map((account) => (
                <option value={account.id}>{account.accountNumber}</option>
              ))}
            </select>
            <Button color="tertiary" onClick={deleteAccount}>
              <MdDelete />
            </Button>
          </AccountGroup>
        )}
        {accounts.length < 3 && (
          <Button color="primary" onClick={createAccount}>
            <IoMdPersonAdd />
          </Button>
        )}
        <Button
          color="secondary"
          onClick={() => {
            toast.success("Logout succesfull");
            logout();
          }}
        >
          Logout
        </Button>
      </div>
    </StyledHeader>
  );
};
