const API_BASE_URL =
  process.env.VUE_APP_API_BASE_URL || "https://api.fptu.tech/bc";

const API_BETA_URL =
  process.env.VUE_APP_API_BETA_URL || "https://api.fptu.tech/io";

const SOCKET_URL = process.env.VUE_APP_SOCKET_URL || "https://api.fptu.tech/io";

export default {
  baseUrl: API_BASE_URL + "/api/v1",
  nodeUrl: API_BETA_URL + "/api/v1",
  socketUrl: SOCKET_URL
};
