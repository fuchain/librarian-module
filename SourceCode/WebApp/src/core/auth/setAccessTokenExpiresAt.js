import { isEmpty } from "ramda";
import constants from "@auth/constants";

import setItem from "@localstorage/setItem";
import removeItem from "@localstorage/removeItem";

export default function(expires_at = "") {
  if (isEmpty(expires_at)) { removeItem(constants.LOCAL_STORAGE_KEY.ACCESS_TOKEN_EXPIRES_AT); } else {
    setItem(
      constants.LOCAL_STORAGE_KEY.ACCESS_TOKEN_EXPIRES_AT,
      expires_at
    );
  }
}
