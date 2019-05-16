import getAccessTokenExpiresAt from "./getAccessTokenExpiresAt";

export default function() {
  return getAccessTokenExpiresAt() - new Date().getTime() >= 0;
}
