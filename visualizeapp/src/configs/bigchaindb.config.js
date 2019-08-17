import queryString from "query-string";

const cluster = [
  {
    name: "node1",
    port: 33276
  },
  {
    name: "node2",
    port: 33282
  },
  {
    name: "node3",
    port: 33288
  },
  {
    name: "node4",
    port: 33294
  },
  {
    name: "node5",
    port: 33300
  },
  {
    name: "node6",
    port: 33306
  },
  {
    name: "node7",
    port: 33312
  },
  {
    name: "node8",
    port: 33318
  },
  {
    name: "testnet",
    port: 9985
  }
];

function getNode() {
  const queryObj = queryString.parse(window.location.search) || {};
  if (queryObj.node) {
    const node = cluster.find(e => e.name === queryObj.node);
    if (node) {
      return node;
    }
  }

  return cluster[0];
}

export default {
  host:
    getNode().name === "testnet"
      ? "testnet.fuchain.fptu.tech"
      : "fuchain.fptu.tech",
  ws_port: getNode().port,
  node_name: getNode().name,
  api: "/api/v1/",
  validTx: "streams/valid_transactions",
  secure: false
};
