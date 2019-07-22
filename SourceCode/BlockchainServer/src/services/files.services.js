const AWS = require('aws-sdk');
const fs = require('fs');
const path = require('path');
//configuring the AWS environment
AWS.config.update({
    accessKeyId: "AKIAZ2JJQ6P2HCT2JWXA",
    secretAccessKey: "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"
  });

const s3 = new AWS.S3();

var uploadFile=function(filePath) {
  var params = {
    Bucket: 'fpt-library-modules',
    Body: fs.createReadStream(filePath),
    ACL:  'public-read',
    Key: Date.now() + "_" + path.basename(filePath)
  };
  s3.upload(params, function (err, data) {
    //handle error
    if (err) {
      console.log("Error", err);
    }

    //success
    if (data) {
      console.log("Uploaded in:", data.Location);
    }
  });
}

module.exports={
    uploadFile:uploadFile

}

