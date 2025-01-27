import { FormLine } from "../../ui/FormLine";
import { Form } from "../../ui/Form";
import { Button } from "../../ui/Button";
import { AppLink } from "../../ui/AppLink";
import { useState } from "react";
import axios from "axios";
import { useUserContext } from "../../context/UserContext";
import toast from "react-hot-toast";
import { Navigate } from "react-router";
import { catchBlockSpecial } from "../../helpers/catchBlockSpecial";

export const LoginForm = () => {
  const [email, setEmail] = useState("solo@deni.com");
  const [password, setPassword] = useState("Password123!");

  const { user, logout, updateJwt } = useUserContext();

  async function login(e) {
    e.preventDefault();
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/auth/users/sign-in`;
    try {
      const response = await axios.post(API_ENDPOINT, { email, password });
      updateJwt(response.data.jwt);
      toast.success(response.data.message);
    } catch (e) {
      catchBlockSpecial(e, false);
    }
  }

  if (user) return <Navigate to="/" />;

  return (
    <Form onSubmit={login}>
      <FormLine>
        <label>Email</label>
        <input
          name="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          type="text"
        />
      </FormLine>
      <FormLine>
        <label>Password</label>
        <input
          name="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
        />
      </FormLine>
      <Button color="primary">Submit</Button>
      <AppLink to="/forgot-password">Forgot password</AppLink>
      <AppLink to="/register">Create account</AppLink>
    </Form>
  );
};
