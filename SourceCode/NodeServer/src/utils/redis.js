const redis = require("redis");
const { promisify } = require("util");

export const client = redis.createClient(
    6379,
    process.env.REDIS_HOST || "redis"
);
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

export async function getRedisItem(key) {
    try {
        return await getAsync(getKey(key));
    } catch (err) {
        throw err;
    }
}

export function deleteRedisItem(key) {
    client.del(getKey(key));
}

export default initRedisModule;
