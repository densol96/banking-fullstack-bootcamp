import { SlActionUndo } from "react-icons/sl";
import { MiniForm } from "../../ui/MiniForm";
import { useAccountContext } from "../../context/AccountContext";
import axios from "axios";
import toast from "react-hot-toast";
import { useUserContext } from "../../context/UserContext";
import { headersWithToken } from "../../helpers/headersWithToken";
import { catchBlockSpecial } from "../../helpers/catchBlockSpecial";

type Props = {};

export const Deposit: React.FC<Props> = () => {
  const { refreshUser, jwt, logout } = useUserContext();
  const { activeAccountId } = useAccountContext();

  async function deposit(amount: number) {
    const API_ENDPOINT = `${process.env.REACT_APP_API_URL}/accounts/${activeAccountId}/deposit`;
    try {
      const response = await axios.post(
        API_ENDPOINT,
        { amount },
        headersWithToken(jwt)
      );
      refreshUser();
      toast.success(response.data.message);
    } catch (e) {
      catchBlockSpecial(e, logout);
    }
  }

  return (
    <MiniForm
      onSubmit={deposit}
      color="primary"
      heading="Deposit"
      icon={<SlActionUndo />}
    />
  );
};
