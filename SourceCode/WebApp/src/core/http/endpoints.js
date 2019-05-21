const API_BASE_URL = process.env.API_BASE_URL || "https://api.fptu.tech";

export default {
    baseUrl: API_BASE_URL + "/admin/v1",
    profileUrl: API_BASE_URL + "/user/v1"
};
