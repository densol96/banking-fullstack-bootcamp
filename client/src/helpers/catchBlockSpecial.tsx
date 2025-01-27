import toast from "react-hot-toast";

export const catchBlockSpecial = (
  e,
  logout: () => void,
  rethrow: boolean = true
) => {
  if (e.status === 403) {
    toast.error("Your session has expired");
    setTimeout(logout, 3000);
    return;
  }
  const errors =
    e.response?.data?.errors &&
    (Object.values(e.response?.data?.errors) as string[]);
  toast.error(
    <div style={{ textAlign: "center" }}>
      {e.response?.data?.message ||
        (errors && <p>{errors[0]}</p>) ||
        "Something went wrong..."}
    </div>
  );
  if (rethrow) throw e;
};
