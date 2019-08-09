export default function librarianMiddleware(req, res, next) {
    if (req.roles === "librarian") {
        next();
    } else {
        res.status(401);
        res.send({ message: "Unauthorized" });
    }
}
