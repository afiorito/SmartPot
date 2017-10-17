"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const main = (event, context, cb) => {
    const response = {
        statusCode: 200,
        body: JSON.stringify({
            message: 'Hello!',
            input: event
        })
    };
    cb(undefined, response);
};
exports.main = main;
//# sourceMappingURL=hello.js.map