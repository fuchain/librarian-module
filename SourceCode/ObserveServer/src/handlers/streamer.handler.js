import socket from "@core/socket";
import poolService from "@services/pool.service";

function handleCommingMessage(message) {
    poolService.addTransaction(message);
}

function start() {
    socket.setupSocket(handleCommingMessage);
}

export default { start };
