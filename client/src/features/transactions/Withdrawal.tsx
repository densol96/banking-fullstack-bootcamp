import { headersWithToken } from "../../helpers/headersWithToken";
import { MiniForm } from "../../ui/MiniForm";
import React, { useState } from "react";
import { SlActionRedo } from "react-icons/sl";
import axios from "axios";
import { useUserContext } from "../../context/UserContext";
import { useAccountContext } from "../../context/AccountContext";
import toast from "react-hot-toast";
import { catchBlockSpecial } from "../../helpers/catchBlockSpecial";

type Props = {};

export const Withdrawal: React.FC<Props> = () => {
  const { refreshUser, jwt } = useUserContext();
  const { activeAccountId } = useAccountContext();

  async function withdraw(amount: number) {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/accounts/${activeAccountId}/withdraw`;
    try {
      const response = await axios.post(
        API_ENDPOINT,
        { amount },
        headersWithToken(jwt)
      );
      refreshUser();
      toast.success(response.data.message);
    } catch (e) {
      catchBlockSpecial(e);
    }
  }

  return (
    <MiniForm
      onSubmit={withdraw}
      color="tertiary"
      heading="Withdraw"
      icon={<SlActionRedo />}
    />
  );
};
