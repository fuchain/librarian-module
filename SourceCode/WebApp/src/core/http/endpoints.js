const API_BASE_URL =
  process.env.VUE_APP_API_BASE_URL || "https://lapi.fptu.tech";

const API_BETA_URL =
  process.env.VUE_APP_API_BETA_URL || "https://napi.fptu.tech";

export default {
  baseUrl: API_BASE_URL + "/api/v1",
  nodeUrl: API_BETA_URL + "/api/v1",
  socketUrl: API_BETA_URL
};
