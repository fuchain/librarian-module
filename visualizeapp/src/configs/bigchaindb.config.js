import queryString from "query-string";

const cluster = [
  {
    name: "node1",
    port: 33564
  },
  {
    name: "node2",
    port: 33570
  },
  {
    name: "node3",
    port: 33576
  },
  {
    name: "node4",
    port: 33582
  },
  {
    name: "node5",
    port: 33588
  },
  {
    name: "node6",
    port: 33594
  },
  {
    name: "node7",
    port: 33600
  },
  {
    name: "node8",
    port: 33606
  },
  {
    name: "testnet",
    port: 9985
  },
  {
    name: "localhost",
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
  host: getHost(getNode().name),
  ws_port: getNode().port,
  node_name: getNode().name,
  api: "/api/v1/",
  validTx: "streams/valid_transactions",
  secure: false
};

function getHost(name) {
  if (name === "testnet") {
    return "testnet.fuchain.fptu.tech";
  } else if (name === "localhost") {
    return "localhost";
  } else {
    return "fuchain.fptu.tech";
  }
}
