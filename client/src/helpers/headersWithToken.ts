export const headersWithToken = (jwt) => {
  return {
    headers: {
      Authorization: `Bearer ${jwt}`,
    },
  };
};
