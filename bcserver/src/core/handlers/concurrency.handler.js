export default async function concurrencyControlHandler(
    dataList,
    concurrencyLimit = 10,
    asyncOperation
) {
    // Get total of batch need to run
    const batchesCount = Math.ceil(dataList.length / concurrencyLimit);
    let results = [];

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
    }

    return results;
}
