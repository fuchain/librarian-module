import store from "./transaction.store";
import action from "./transaction.action";
import actionType from "./transaction.constant";
import reducer from "./transaction.reducer";

export default {
    store: store.createTransactionPool(),
    action,
    actionType,
    reducer
};
