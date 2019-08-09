import queryString from "query-string";

const cluster = [
  {
    name: "node1",
    port: 33012
  },
  {
    name: "node2",
    port: 33018
  },
  {
    name: "node3",
    port: 33024
  },
  {
    name: "node4",
    port: 33030
  },
  {
    name: "node5",
    port: 33036
  },
  {
    name: "node6",
    port: 33042
  },
  {
    name: "node7",
    port: 33048
  },
  {
    name: "node8",
    port: 33054
  },
  {
    name: "testnet",
    port: 32796
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
      ? "testnet.bigchain.fptu.tech"
      : "bigchain.fptu.tech",
  ws_port: getNode().port,
  node_name: getNode().name,
  api: "/api/v1/",
  validTx: "streams/valid_transactions",
  secure: false
};
