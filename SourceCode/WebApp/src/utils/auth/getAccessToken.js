import * as LocalStorage from "@localstorage";
import constants from "@auth/constants";

export default function() {
  return LocalStorage.getItem(constants.LOCAL_STORAGE_KEY.ACCESS_TOKEN);
}
