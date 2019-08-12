import queryString from "query-string";

const cluster = [
  {
    name: "node1",
    port: 32820
  },
  {
    name: "node2",
    port: 32826
  },
  {
    name: "node3",
    port: 32832
  },
  {
    name: "node4",
    port: 32838
  },
  {
    name: "node5",
    port: 32844
  },
  {
    name: "node6",
    port: 32850
  },
  {
    name: "node7",
    port: 32856
  },
  {
    name: "node8",
    port: 32862
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
      ? "testnet.bigchain.fptu.tech"
      : "bigchain.fptu.tech",
  ws_port: getNode().port,
  node_name: getNode().name,
  api: "/api/v1/",
  validTx: "streams/valid_transactions",
  secure: false
};
