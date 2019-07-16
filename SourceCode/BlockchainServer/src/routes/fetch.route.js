import express from 'express';
import controller from "@controllers/fetch.controller"

const router = express.Router();

router.route('/asset_id').post(controller.getBookDetailFromAssetId);

export default router;
