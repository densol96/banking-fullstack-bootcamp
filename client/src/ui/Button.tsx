import styled from "styled-components";

const Button = styled.button<{ color: string }>`
  display: inline-block;
  background-color: var(--color-${(props) => props.color});
  font-size: 1.6rem;
  font-family: inherit;
  font-weight: 500;
  border: none;
  padding: 1.25rem 4.5rem;
  border-radius: 10rem;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
  }
`;

export { Button };
