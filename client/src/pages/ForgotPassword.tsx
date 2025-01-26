import { ForgotForm } from "../features/auth/ForgotForm";
import { RegisterForm } from "../features/auth/RegisterForm";
import { CenteredBlock } from "../ui/CenteredBlock";

export const ForgotPassword = () => {
  return (
    <CenteredBlock title="Restore your password">
      <ForgotForm />
    </CenteredBlock>
  );
};
