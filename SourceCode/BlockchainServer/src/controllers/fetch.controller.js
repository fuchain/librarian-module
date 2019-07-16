import errorHandler from "@core/handlers/error.handler"
import fetchService from "@services/fetch.service"

async function getBookDetailFromAssetId(req, res){
    const assetId = req.body.asset_id;

    const bookDetail = await fetchService.getBookDetail(assetId);
    if(!bookDetail){
        res.status(404).send({
            message: "Cannot find any book detail with this asset id"
        });
    }
    res.send(bookDetail);
}

export default errorHandler({
    getBookDetailFromAssetId
});
