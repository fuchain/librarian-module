import setAccessToken from "./setAccessToken";

import * as localStorage from "@localstorage";

export default function() {
  setAccessToken();
  localStorage.removeItem("picture");
}
