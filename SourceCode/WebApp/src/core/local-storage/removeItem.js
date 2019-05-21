import generateKey from "./generateKey";

export default function(key) {
    if (typeof localStorage !== "undefined") {
        localStorage.removeItem(generateKey(key));
    }
}
