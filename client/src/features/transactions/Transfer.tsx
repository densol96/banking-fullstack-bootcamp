import { SlActionUndo } from "react-icons/sl";
import { MiniForm } from "../../ui/MiniForm";
import { useAccountContext } from "../../context/AccountContext";
import axios from "axios";
import toast from "react-hot-toast";
import { useUserContext } from "../../context/UserContext";
import { headersWithToken } from "../../helpers/headersWithToken";
import { catchBlockSpecial } from "../../helpers/catchBlockSpecial";
import { useState } from "react";
import styled from "styled-components";
import Heading from "../../ui/Heading";
import { GrTransaction } from "react-icons/gr";

type Props = {};

const ActionForm = styled.form`
  display: flex;
  gap: 2rem;
`;

const Container = styled.div<{ color: string }>`
  width: 70%;
  margin-left: auto;
  display: flex;
  flex-direction: column;
  padding: 2rem 5rem;
  border-radius: 12px;
  background: var(--gradient-${({ color }) => color});

  button {
    padding: 1rem;
    border: 3px solid white;
    transition: all 300ms;

    &:hover {
      transform: translateY(-3px);
    }
  }

  input {
    align-self: center;
    font-size: 2rem;
    width: 100%;
  }

  svg {
    font-size: 3rem;
  }
`;

const InputHolder = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: stretch;
  margin-top: 1rem;
`;

export const Transfer: React.FC<Props> = () => {
  const { refreshUser, jwt, logout } = useUserContext();
  const { activeAccountId } = useAccountContext();

  const [amount, setAmount] = useState(0);
  const [toAccountNumber, setToAccountNumber] = useState("");

  async function transfer() {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/accounts/${activeAccountId}/transfer`;
    try {
      const response = await axios.post(
        API_ENDPOINT,
        { amount, toAccountNumber },
        headersWithToken(jwt)
      );
      refreshUser();
      toast.success(response.data.message);
    } catch (e) {
      catchBlockSpecial(e, logout, false);
    }
  }

  return (
    <Container color="secondary">
      <Heading as="h2">Transfer</Heading>
      <ActionForm
        onSubmit={(e) => {
          e.preventDefault();
          transfer();
        }}
      >
        <InputHolder>
          <input
            onChange={(e) => setAmount(+e.target.value)}
            value={amount}
            type="number"
          />
          <input
            placeholder="Acc. number"
            onChange={(e) => setToAccountNumber(e.target.value)}
            value={toAccountNumber}
            type="text"
          />
        </InputHolder>

        <button>
          <GrTransaction />
        </button>
      </ActionForm>
    </Container>
  );
};
