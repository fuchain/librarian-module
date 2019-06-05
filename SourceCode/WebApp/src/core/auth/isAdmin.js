import jwtDecode from "jwt-decode";
import getAccessToken from "./getAccessToken";

export default function() {
  const email = jwtDecode(getAccessToken()).sub;
  if (email.includes("@fe.edu.vn")) {
    return true;
  }

  return false;
}
