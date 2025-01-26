import React from "react";
import { useUserContext } from "../context/UserContext";

type Props = {
  className?: string;
  children?: React.ReactNode;
};

export const Home: React.FC<Props> = () => {
  const { user } = useUserContext();
  return (
    <h1>
      Welcome back, {user?.firstName} {user?.lastName}{" "}
    </h1>
  );
};
