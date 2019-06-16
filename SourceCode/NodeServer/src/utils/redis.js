const redis = require("redis");
const { promisify } = require("util");

export const client = redis.createClient(6379, "34.87.113.91");
const getAsync = promisify(client.get).bind(client);

const KEY_CONSTANT = "NODE_";
function getKey(key) {
    return KEY_CONSTANT + key;
}

function initRedisModule() {
    client.on("connect", function() {
        console.log("Redis connected!");
    });

    client.on("error", function(error) {
        console.log("Redis connect error: ", error);
    });
}

export function setRedisItem(key, value) {
    client.set(getKey(key), value);
}

export async function getRedisitem(key) {
    try {
        return await getAsync(getKey(key));
    } catch (err) {
        // Catch error
        console.log("Get redis error: ", err);

        return null;
    }
}

export function deleteRedisItem(key) {
    client.del(getKey(key));
}

export default initRedisModule;
