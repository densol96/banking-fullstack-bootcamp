export function formatBalance(
  amount: number,
  currency: string = "EUR"
): string {
  return new Intl.NumberFormat("de-DE", {
    style: "currency",
    currency: currency,
  }).format(amount);
}
