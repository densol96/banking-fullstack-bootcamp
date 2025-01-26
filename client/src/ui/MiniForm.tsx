import styled from "styled-components";
import Heading from "./Heading";
import { useState } from "react";

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
    font-size: 3rem;
    width: 50%;
  }

  svg {
    font-size: 3rem;
  }
`;

type Props = {
  heading: string;
  color: string;
  icon: React.ReactNode;
  onSubmit: (amount: number) => void;
};

export const MiniForm: React.FC<Props> = ({
  icon,
  color,
  heading,
  onSubmit,
}) => {
  const [value, setValue] = useState<number>(0);

  return (
    <Container color={color}>
      <Heading as="h2">{heading}</Heading>
      <ActionForm
        onSubmit={(e) => {
          e.preventDefault();
          onSubmit(value);
        }}
      >
        <input
          onChange={(e) => setValue(+e.target.value)}
          value={value}
          type="number"
        />
        <button>{icon}</button>
      </ActionForm>
    </Container>
  );
};
