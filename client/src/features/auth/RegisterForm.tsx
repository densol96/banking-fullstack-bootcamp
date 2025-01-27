import { FormLine } from "../../ui/FormLine";
import { Form } from "../../ui/Form";
import { Button } from "../../ui/Button";
import { AppLink } from "../../ui/AppLink";
import axios from "axios";
import toast from "react-hot-toast";
import { catchBlockSpecial } from "../../helpers/catchBlockSpecial";
import { useState } from "react";

export const RegisterForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  function cleanInputs() {
    setEmail("");
    setPassword("");
    setPasswordConfirm("");
    setFirstName("");
    setLastName("");
  }

  async function register(e) {
    e.preventDefault();
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/auth/customers/sign-up`;
    try {
      const response = await axios.post(API_ENDPOINT, {
        email,
        password,
        passwordConfirm,
        firstName,
        lastName,
      });
      toast.success(<p>{response.data.message}</p>);
      cleanInputs();
    } catch (e) {
      catchBlockSpecial(e, null, false);
    }
  }

  return (
    <Form onSubmit={register}>
      <FormLine>
        <label>Email</label>
        <input
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          type="text"
        />
      </FormLine>
      <FormLine>
        <label>First name</label>
        <input
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
          type="text"
        />
      </FormLine>
      <FormLine>
        <label>Last name</label>
        <input
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          type="text"
        />
      </FormLine>
      <FormLine>
        <label>Password</label>
        <input
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
        />
      </FormLine>
      <FormLine>
        <label>Password confirm</label>
        <input
          value={passwordConfirm}
          onChange={(e) => setPasswordConfirm(e.target.value)}
          type="password"
        />
      </FormLine>
      <Button color="primary">Submit</Button>
      <AppLink to="/login">Login</AppLink>
      <AppLink to="/forgot-password">Forgot password</AppLink>
    </Form>
  );
};
