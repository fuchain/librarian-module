import axios from "axios";
import auth from "@auth";
import endpoints from "./endpoints";
import keypair from "@core/crypto/keypair";

const config = {
  timeout: 10000 // 10 seconds
};

function defaultHeaders() {
  const headers = {};

  headers["Content-Type"] = "application/json";

  const accessToken = auth.getAccessToken();
  if (accessToken !== null && !auth.tokenIsExpired()) {
    headers.Authorization = `Bearer ${accessToken}`;
  }

  return headers;
}

async function request({
  to,
  method = "GET",
  data = {},
  params = {},
  headers = {},
  custom
}) {
  const axiosInstance = axios({
    headers: { ...defaultHeaders(), ...headers },
    url: to,
    method,
    data,
    params,
    config,
    // transformResponse: function(data) {
    //   // Simple the response data.data
    //   const parse = JSON.parse(data);
    //   return parse.data;
    // },
    ...custom
  });

  return axiosInstance;
}

function doGetRequest(to, data = {}, params = {}, headers = {}) {
  return request({
    to,
    method: "GET",
    data,
    params,
    headers
  });
}

function doPostRequest(to, data = {}, params = {}, headers = {}) {
  if (!data.public_key && keypair.get("publicKey")) {
    data.public_key = keypair.get("publicKey");
  }

  const instance = request({
    to,
    method: "POST",
    data,
    params,
    headers
  });

  return instance;
}

function doPutRequest(to, data = {}, params = {}, headers = {}) {
  return request({
    to,
    method: "PUT",
    data,
    params,
    headers
  });
}

function doDeleteRequest(to, data = {}, params = {}, headers = {}) {
  return request({
    to,
    method: "DELETE",
    data,
    params,
    headers
  });
}

export default {
  get: doGetRequest,
  post: doPostRequest,
  put: doPutRequest,
  delete: doDeleteRequest,
  baseUrl: endpoints.baseUrl,
  nodeUrl: endpoints.nodeUrl,
  socketUrl: endpoints.socketUrl
};
