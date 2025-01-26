import React from "react";
import styled from "styled-components";
import { FormLine } from "../../ui/FormLine";
import { Form } from "../../ui/Form";
import { Link } from "react-router-dom";

const Button = styled.button`
  display: inline-block;
  background-color: var(--color-primary);
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

const StyledLink = styled(Link)`
  text-decoration: underline;

  &:hover {
    text-decoration: none;
  }
`;

export const LoginForm = () => {
  return (
    <Form>
      <FormLine>
        <label>Email</label>
        <input type="text" />
      </FormLine>
      <FormLine>
        <label>Password</label>
        <input type="password" />
      </FormLine>
      <Button>Submit</Button>
      <StyledLink to="/forgot">Forgot password</StyledLink>
      <StyledLink to="/forgot">Create account</StyledLink>
    </Form>
  );
};
