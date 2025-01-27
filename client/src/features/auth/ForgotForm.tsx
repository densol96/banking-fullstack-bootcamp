import { FormLine } from "../../ui/FormLine";
import { Form } from "../../ui/Form";
import { Button } from "../../ui/Button";
import { AppLink } from "../../ui/AppLink";
import { useState } from "react";

export const ForgotForm = () => {
  const [] = useState();
  const [] = useState();

  return (
    <Form>
      <FormLine>
        <label>Email</label>
        <input type="text" />
      </FormLine>
      <Button color="primary">Submit</Button>
      <AppLink to="/login">Login</AppLink>
      <AppLink to="/register">Register</AppLink>
    </Form>
  );
};
