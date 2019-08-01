import secure from "./secure";
import * as $localStorage from "@localstorage";

function get(type = "publicKey") {
  const keyEncrypted = $localStorage.getItem(type);
  return secure.decrypt(keyEncrypted);
}

function set(type = "publicKey", value) {
  const keyEncrypted = secure.encrypt(value);
  $localStorage.setItem(type, keyEncrypted);
}

export default { get, set };
