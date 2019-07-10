import express from "express";
import controller from "@controllers/book.controller";

const router = express.Router();

router.route("/search").get(controller.searchBookDetails);

export default router;
