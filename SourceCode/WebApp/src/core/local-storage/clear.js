export default function() {
    if (typeof localStorage !== "undefined") {
        localStorage.clear();
    }
}
