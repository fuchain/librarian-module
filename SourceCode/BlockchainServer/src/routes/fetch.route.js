import express from 'express';
import controller from "@controllers/fetch.controller"

const router = express.Router();

router.route('/asset_id').post(controller.getBookDetailFromAssetId);
router.route('/public_key').post(controller.getEmail);

export default router;
