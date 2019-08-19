export default async function concurrencyControlHandler(
    dataList,
    concurrencyLimit = 10,
    asyncOperation
) {
    // Get total of batch need to run
    const batchesCount = Math.ceil(dataList.length / concurrencyLimit);
    let results = [];

    console.log(batchesCount);

    for (let i = 0; i < batchesCount; i++) {
        const batchStart = i * concurrencyLimit;
        const batchArguments = dataList.slice(
            batchStart,
            batchStart + concurrencyLimit
        );
        const batchPromises = batchArguments.map(asyncOperation);

        // Harvesting
        const batchResults = await Promise.all(batchPromises);
        results = results.concat(batchResults);

        console.log("Done: ", i);
    }

    // Merge array of promise to one array; EG: [[item 1], [item2]] => [item1, item2]
    const resultFinal = arrayOfPromisesResult.reduce((a, b) => a.concat(b));
    return resultFinal;
}
