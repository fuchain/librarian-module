export default async function concurrencyControlHandler(
    dataList,
    concurrencyLimit = 10,
    asyncOperation
) {
    // Get total of batch need to run
    const batchesCount = Math.ceil(dataList.length / concurrencyLimit);

    // Loop over the total batch number
    const batchesArray = Array.from(Array(batchesCount));
    const promises = batchesArray.map(async (_, i) => {
        // This function will return an array of promises
        const batchStart = i * concurrencyLimit;
        const batchArguments = dataList.slice(
            batchStart,
            batchStart + concurrencyLimit
        );
        const batchPromises = batchArguments.map(asyncOperation);

        const batchResults = await Promise.all(batchPromises);

        return batchResults;
    });

    // Resolve above array of promises
    const arrayOfPromisesResult = await Promise.all(promises);

    // Merge array of promise to one array; EG: [[item 1], [item2]] => [item1, item2]
    const result = arrayOfPromisesResult.reduce((a, b) => a.concat(b));
    return result;
}
