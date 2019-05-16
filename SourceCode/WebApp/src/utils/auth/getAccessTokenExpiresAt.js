import * as LocalStorage from "@localstorage";
import constants from "@auth/constants";

/**
 * @return int
 */
export default function() {
  const accessTokenExpireAt = parseInt(
    LocalStorage.getItem(
      constants.LOCAL_STORAGE_KEY.ACCESS_TOKEN_EXPIRES_AT,
      0
    )
  );

  return Number.isInteger(accessTokenExpireAt) &&
    accessTokenExpireAt >= 0
    ? accessTokenExpireAt
    : 0;
}
