import { RegisterForm } from "../features/auth/RegisterForm";
import { CenteredBlock } from "../ui/CenteredBlock";

export const Register = () => {
  return (
    <CenteredBlock title="Create a customer account">
      <RegisterForm />
    </CenteredBlock>
  );
};
