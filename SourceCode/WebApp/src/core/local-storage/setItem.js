import generateKey from "./generateKey";

export default function(key, value) {
    if (typeof localStorage !== "undefined") {
        localStorage.setItem(generateKey(key), value);
    }
}
