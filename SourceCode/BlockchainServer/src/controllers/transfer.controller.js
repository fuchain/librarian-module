import errorHandler from "@core/handlers/error.handler";
import transferService from "@services/transfer.service";

async function createTransfer(req, res) {
    const body = req.body;

    const tx = await transferService.createTransferRequest(
        body.asset_id,
        body.to.public_key // this is public key of the receiver
    );

    res.send(tx);
}

async function sendTxSignedToReceiver(req, res) {
    const body = req.body;

    const transferTxSigned = transferService.createReceiverConfirmAsset(
        body.tx,
        body.tx.outputs[0].public_keys[0]
    );

    // send event to receiver to sign

    res.send(transferTxSigned);
}

async function receiverSigned(req, res) {
    const body = req.body;

    const {
        transferTxPosted,
        confirmAssetPosted
    } = await transferService.postToDoneTransfer(body.tx); // this is two posted transaction

    // send event to returner that everything is done

    res.send({
        transfer_tx_posted: transferTxPosted,
        confirm_asset_posted: confirmAssetPosted
    });
}

export default errorHandler({
    createTransfer,
    sendTxSignedToReceiver,
    receiverSigned
});
