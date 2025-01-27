export function getFormattedDateTime(): string {
  const forFormat = new Date();

  return forFormat.toLocaleString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    hour12: true,
  });
}

export function getConciseDateTime(date: string): string {
  const forFormat = new Date(date);
  const day = forFormat.getDate().toString().padStart(2, "0");
  const month = (forFormat.getMonth() + 1).toString().padStart(2, "0");
  const year = forFormat.getFullYear();
  const hours = forFormat.getHours().toString().padStart(2, "0");
  const minutes = forFormat.getMinutes().toString().padStart(2, "0");

  return `${day}/${month}/${year} ${hours}:${minutes}`;
}
