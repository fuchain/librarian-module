import models from "@models";
import { createJWT } from "@utils/jwt";
import { comparePassword } from "@utils/hash";

const login = async (req, res) => {
    const { email, password } = req.body;
    try {
        const user = await models.user.findOne({
            where: {
                email
            }
        });

        if (user) {
            const isValid = await comparePassword(password, user.password);

            if (isValid) {
                const token = createJWT(user.id);

                res.send({ token });
            } else {
                res.status(400);
                res.send();
            }
        } else {
            res.status(400);
            res.send();
        }
    } catch (err) {
        res.status(400);
        res.send(err);
    }
};

export default { login };
