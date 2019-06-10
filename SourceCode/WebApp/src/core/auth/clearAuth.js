import setAccessToken from "./setAccessToken";
import setRefreshToken from "./setRefreshToken";
import setAccessTokenExpiresAt from "./setAccessTokenExpiresAt";

import * as localStorage from "@localstorage";

export default function() {
  setAccessToken();
  setRefreshToken();
  setAccessTokenExpiresAt();
  localStorage.removeItem("picture");
}
