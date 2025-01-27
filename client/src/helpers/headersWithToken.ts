export const headersWithToken = (jwt: string) => {
  console.log(jwt);
  return {
    headers: {
      Authorization: `Bearer ${jwt}`,
    },
  };
};
