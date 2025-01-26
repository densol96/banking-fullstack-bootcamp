import { MiniForm } from "../../ui/MiniForm";
import React from "react";
import { SlActionRedo } from "react-icons/sl";

type Props = {};

export const Withdrawal: React.FC<Props> = () => {
  return (
    <MiniForm
      onSubmit={() => {}}
      color="tertiary"
      heading="Withdraw"
      icon={<SlActionRedo />}
    />
  );
};
