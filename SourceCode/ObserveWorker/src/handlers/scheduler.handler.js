import { SCHEDULE_TIME } from "@constants/scheduler.constant";
import poolService from "@services/pool.service";

function validatePool() {
    poolService.validateTransactionPool();
}

function start() {
    setInterval(validatePool, SCHEDULE_TIME);
}

export default { start };
