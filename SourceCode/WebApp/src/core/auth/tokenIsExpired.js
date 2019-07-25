import jwtDecode from "jwt-decode";
import getAccessToken from "./getAccessToken";

export default function() {
  const tokenObj = jwtDecode(getAccessToken());
  const expiresAt = tokenObj.exp || 0;

  return expiresAt - new Date().getTime() >= 0;
}
