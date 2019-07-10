import express from "express";
import controller from "@controllers/librarian.controller";

const router = express.Router();

router.route("/book_details").get(controller.getAllBookDetails);

export default router;
