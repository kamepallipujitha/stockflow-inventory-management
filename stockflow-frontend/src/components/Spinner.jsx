export default function Spinner({ className = "" }) {
  return (
    <div
      className={`inline-block animate-spin rounded-full border-2 border-gray-300 border-t-brand-600 ${className}`}
      style={{ width: "1em", height: "1em" }}
    />
  );
}
