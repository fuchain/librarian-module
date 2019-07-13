import store from "@pools/transaction.store";
import action from "@pools/transaction.action";
import actionType from "@pools/transaction.constant";
import reducer from "@pools/transaction.reducer";

export default {
    store: store.createTransactionPool(),
    action,
    actionType,
    reducer
};
