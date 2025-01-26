export type Account = {
  id: number;
  balance: number;
  accountNumber: string;
};

export type UserProfile = {
  accounts: Account[];
  department: string | null;
  position: string | null;
};

export type Role = "CUSTOMER" | "EMPLOYEE";

export type User = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  profile: UserProfile;
};

const example: User = {
  id: 1,
  email: "solo@deni.com",
  firstName: "Deniss",
  lastName: "Solovjovs",
  role: "CUSTOMER",
  profile: {
    accounts: [
      {
        id: 1,
        balance: 100.0,
        accountNumber: "SOLDEN_2c2bf8ec6744",
      },
    ],
    department: null,
    position: null,
  },
};
