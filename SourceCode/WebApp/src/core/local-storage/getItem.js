import generateKey from "./generateKey";

export default function(key, defaultValue = null) {
    if (typeof localStorage !== "undefined") {
        return localStorage.getItem(generateKey(key)) || defaultValue;
    }
}
