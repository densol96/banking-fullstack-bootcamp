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
  onSubmit: (amount: number) => Promise<void>;
};

export const MiniForm: React.FC<Props> = ({
  icon,
  color,
  heading,
  onSubmit,
}) => {
  const [value, setValue] = useState<number>(0);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  return (
    <Container color={color}>
      <Heading as="h2">{heading}</Heading>
      <ActionForm
        onSubmit={async (e) => {
          e.preventDefault();
          try {
            setIsLoading(true);
            await onSubmit(value);
            setValue(0);
          } catch (e) {
          } finally {
            setIsLoading(false);
          }
        }}
      >
        <input
          onChange={(e) => setValue(+e.target.value)}
          value={value}
          type="number"
        />
        <button disabled={isLoading}>{icon}</button>
      </ActionForm>
    </Container>
  );
};
