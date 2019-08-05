import redis from "redis";
import { promisify, callbackify } from "util";
import env from "@core/env";

export const client = redis.createClient({
    url: `redis://${env.redisHost}` || "redis"
});
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

export async function setRedisItem(key, value) {
    return new Promise(function(resolve, reject) {
        client.set(getKey(key), value, function(err) {
            if (err) {
                reject(err);
            } else {
                resolve();
            }
        });
    });
}

export async function getRedisItem(key) {
    try {
        return await getAsync(getKey(key));
    } catch (err) {
        throw err;
    }
}

export function deleteRedisItem(key) {
    return new Promise(function(resolve, reject) {
        client.del(getKey(key), function(err) {
            if (err) {
                reject(err);
            } else {
                resolve();
            }
        });
    });
}

export default initRedisModule;
