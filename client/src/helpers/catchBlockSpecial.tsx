import toast from "react-hot-toast";

export const catchBlockSpecial = (e, rethrow: boolean = true) => {
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
