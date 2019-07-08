function errorHandler(fn) {
    return (req, res, next) => {
        const routePromise = fn(req, res, next);
        if (routePromise.catch) {
            routePromise.catch(err => next(err));
        }
    };
}

function addErrorHandlerWrapper(controllers) {
    Object.keys(controllers).forEach(function(key) {
        controllers[key] = errorHandler(controllers[key]);
    });

    return controllers;
}

export default addErrorHandlerWrapper;
