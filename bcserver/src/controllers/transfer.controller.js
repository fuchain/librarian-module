import errorHandler from "@core/handlers/error.handler";
import transferLogic from "@logics/transfer.logic";

async function createTransfer(req, res) {
    const body = req.body;

    const tx = await transferLogic.createTransferRequest(
        body.asset_id,
        body.to.email // this is public key of the receiver
    );

    res.send(tx);
}

async function sendTxSignedToReceiver(req, res) {
    const body = req.body;

    // public_key of the receiver
    const publicKey = body.tx.outputs[0].public_keys[0];

    const transferTxSigned = await transferLogic.createReceiverConfirmAsset(
        body.tx,
        publicKey
    );

    res.send({
        message: "Transaction sent to receiver",
        tx: transferTxSigned
    });
}

async function receiverSigned(req, res) {
    const body = req.body;

    const {
        transferTxPosted,
        confirmAssetPosted
    } = await transferLogic.postToDoneTransfer(body.tx); // this is two posted transaction

    res.send({
        transfer_tx_posted: transferTxPosted,
        confirm_asset_posted: confirmAssetPosted
    });
}

async function giveTest(req, res) {
    const body = req.body;

    const confirmAssetTx = await transferLogic.giveTestbook(
        req.publicKey,
        body.coupon || "null"
    );

    res.send({ tx: confirmAssetTx });
}

export default errorHandler({
    createTransfer,
    sendTxSignedToReceiver,
    receiverSigned,
    giveTest
});
