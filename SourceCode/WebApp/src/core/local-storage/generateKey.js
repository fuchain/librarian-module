import config from "./config";

export default function(key) {
  return `${config.name}_${key}`; // Local storage pattern: prefix_key
}
