import AWS from "aws-sdk";
import fs from "fs";
import path from "path";
import axios from "axios";

// Configs
AWS.config.update({
    accessKeyId: "AKIAJXUUGQSVGNT4JSZQ",
    secretAccessKey: "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"
});

const s3 = new AWS.S3();

async function uploadFile(filePath) {
    const params = {
        Bucket: "fpt-library-modules",
        Body: fs.createReadStream(filePath),
        ACL: "public-read",
        Key: Date.now() + "_" + path.basename(filePath)
    };

    const { Location } = await s3.upload(params);
    return Location;
}

async function saveUserAvatar(url, email) {
    const { data } = await axios.get(url, {
        responseType: "arraybuffer",
        responseEncoding: "binary"
    });

    return new Promise((resolve, reject) => {
        s3.putObject(
            {
                Body: data,
                Key: `avatars/${email}.jpg`,
                ACL: "public-read",
                Bucket: "fuchain"
            },
            function(error, _) {
                if (error) {
                    reject(error);
                } else {
                    resolve();
                }
            }
        );
    });
}

export default {
    uploadFile,
    saveUserAvatar
};
