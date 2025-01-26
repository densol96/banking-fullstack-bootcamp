import { SlActionUndo } from "react-icons/sl";
import { MiniForm } from "../../ui/MiniForm";
import { useAccountContext } from "../../context/AccountContext";
import axios from "axios";
import toast from "react-hot-toast";
import { useUserContext } from "../../context/UserContext";
import { headersWithToken } from "../../helpers/headersWithToken";

type Props = {};

export const Deposit: React.FC<Props> = () => {
  const { refreshUser, jwt } = useUserContext();
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
      const errors =
        e.response?.data?.errors &&
        (Object.values(e.response?.data?.errors) as string[]);
      toast.error(
        <div style={{ textAlign: "center" }}>
          {e.response?.data?.message ||
            (errors && errors?.map((err) => <p>{err}</p>)) ||
            "Something went wrong..."}
        </div>
      );
      throw e;
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
