import { isEmpty } from "ramda";
import constants from "@auth/constants";

import setItem from "@localstorage/setItem";
import removeItem from "@localstorage/removeItem";

export default function(token = "") {
    if (isEmpty(token)) removeItem(constants.LOCAL_STORAGE_KEY.ACCESS_TOKEN);
    else setItem(constants.LOCAL_STORAGE_KEY.ACCESS_TOKEN, token);
}
