import { LoginForm } from "../features/auth/LoginForm";
import { CenteredBlock } from "../ui/CenteredBlock";

export const Login = () => {
  return (
    <CenteredBlock title="Login to your account">
      <LoginForm />
    </CenteredBlock>
  );
};
