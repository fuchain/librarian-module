import { isEmpty } from "ramda";
import constants from "@auth/constants";

import setItem from "@localstorage/setItem";
import removeItem from "@localstorage/removeItem";

export default function(boardName = "") {
    if (isEmpty(boardName)) removeItem(constants.LOCAL_STORAGE_KEY.BOARD);
    else setItem(constants.LOCAL_STORAGE_KEY.BOARD, boardName);
}
