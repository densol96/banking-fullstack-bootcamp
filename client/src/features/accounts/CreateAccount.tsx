import React from "react";

type Props = {
  className?: string;
  children?: React.ReactNode;
};

export const CreateAccount: React.FC<Props> = ({ className, children }) => {
  return <div className={className}>{children}</div>;
};
