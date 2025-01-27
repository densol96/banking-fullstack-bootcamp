import { useUserContext } from "../context/UserContext";
import { BsBank2 } from "react-icons/bs";
import Heading from "./Heading";
import styled from "styled-components";
import { Button } from "./Button";
import { useAccountContext } from "../context/AccountContext";
import toast from "react-hot-toast";

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

export const Header = () => {
  const { activeAccountId, setActiveAccountId } = useAccountContext();
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
          <select
            onChange={(e) => setActiveAccountId(+e.target.value)}
            value={activeAccountId}
          >
            {accounts.map((account) => (
              <option value={account.id}>{account.accountNumber}</option>
            ))}
          </select>
        )}
        <Button
          color="primary"
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
