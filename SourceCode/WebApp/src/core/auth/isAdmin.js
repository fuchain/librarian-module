import jwtDecode from "jwt-decode";
import getAccessToken from "./getAccessToken";

export default function() {
  const tokenObj = jwtDecode(getAccessToken());
  if (tokenObj.roles && tokenObj.roles.length) {
    return true;
  }

  return false;
}
