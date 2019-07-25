import axios from "axios";
import env from "@core/env";
const { httpProtocol, host, prefix } = env;

const bigchainUrl = `${httpProtocol}://${host}/${prefix}`;

async function getTransaction(txId) {
    const result = await axios.get(bigchainUrl + "/transactions/" + txId);
    return result.data;
}

async function searchAssetByTxId(txId) {
    const result = await axios.get(bigchainUrl + "/assets?search=" + txId);
    return result.data;
}

async function getListTransactionOfAsset(assetId) {
    const result = await axios.get(
        bigchainUrl + "/transactions?asset_id=" + assetId
    );
    return result.data;
}

export default {
    getTransaction,
    searchAssetByTxId,
    getListTransactionOfAsset
};
