import setAccessToken from "./setAccessToken";
import setRefreshToken from "./setRefreshToken";
import setAccessTokenExpiresAt from "./setAccessTokenExpiresAt";

export default function() {
  setAccessToken();
  setRefreshToken();
  setAccessTokenExpiresAt();
}
