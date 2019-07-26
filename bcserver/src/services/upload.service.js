import AWS from "aws-sdk";
import fs from "fs";
import path from "path";

// Configs
AWS.config.update({
    accessKeyId: "AKIAZ2JJQ6P2HCT2JWXA",
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

export default {
    uploadFile
};
