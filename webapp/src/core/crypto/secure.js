/* eslint-disable */
import crypto from "crypto";

function encrypt(text) {
  const cipher = crypto.createCipher("aes-256-cbc", "a6F4tfwg");
  let crypted = cipher.update(text, "utf8", "hex");
  crypted += cipher.final("hex");
  return crypted;
}

function decrypt(text) {
  const decipher = crypto.createDecipher("aes-256-cbc", "a6F4tfwg");
  let dec = decipher.update(text, "hex", "utf8");
  dec += decipher.final("utf8");
  return dec;
}

export default {
  encrypt,
  decrypt
};
