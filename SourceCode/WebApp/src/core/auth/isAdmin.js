import { getItem } from "@localstorage";

export default function() {
  if (getItem("email").includes("@fe.edu.vn")) {
    return true;
  }

  return false;
}
