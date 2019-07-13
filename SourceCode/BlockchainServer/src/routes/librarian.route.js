import express from "express";
import controller from "@controllers/librarian.controller";

const router = express.Router();

router.route("/book_details").get(controller.getAllBookDetails);
router.route("/users").get(controller.getAllUsers);
router.route("/users/books").post(controller.getBookByUser);

export default router;
