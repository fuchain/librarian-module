export default function(email) {
  return email.substring(0, email.lastIndexOf("@")).toUpperCase();
}
