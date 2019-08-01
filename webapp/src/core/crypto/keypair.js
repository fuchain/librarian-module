import secure from "./secure";

function get(type = "publicKey") {
  const keyEncrypted = window.vue.$localStorage.getItem(type);
  return secure.decrypt(keyEncrypted);
}

function set(type = "publicKey", value) {
  const keyEncrypted = secure.encrypt(value);
  window.vue.$localStorage.setItem(type, keyEncrypted);
}

export default { get, set };
