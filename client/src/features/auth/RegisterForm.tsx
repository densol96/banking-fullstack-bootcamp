import { FormLine } from "../../ui/FormLine";
import { Form } from "../../ui/Form";
import { Button } from "../../ui/Button";
import { AppLink } from "../../ui/AppLink";

export const RegisterForm = () => {
  return (
    <Form>
      <FormLine>
        <label>Email</label>
        <input type="text" />
      </FormLine>
      <FormLine>
        <label>First name</label>
        <input type="text" />
      </FormLine>
      <FormLine>
        <label>Last name</label>
        <input type="text" />
      </FormLine>
      <FormLine>
        <label>Password</label>
        <input type="password" />
      </FormLine>
      <FormLine>
        <label>Password confirm</label>
        <input type="password" />
      </FormLine>
      <Button>Submit</Button>
      <AppLink to="/login">Login</AppLink>
      <AppLink to="/forgot-password">Forgot password</AppLink>
    </Form>
  );
};
