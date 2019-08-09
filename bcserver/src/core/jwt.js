import jwt from "jsonwebtoken";

export function createJWT(email, librarian = false) {
    return jwt.sign(
        {
            email: email,
            roles: librarian ? "librarian" : "reader"
        },
        "capstone",
        { expiresIn: "24h" }
    );
}

export function verifyJWT(token) {
    return jwt.verify(token, "capstone");
}
