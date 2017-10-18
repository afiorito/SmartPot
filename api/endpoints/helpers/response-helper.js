"use strict";
/**
 * RESPONSE HELPERS
 * Allows for cleaner HTTP response generation
 */
Object.defineProperty(exports, "__esModule", { value: true });
function success(body) {
    return buildResponse(200, body);
}
exports.success = success;
function failure(error) {
    return buildResponse(500, error);
}
exports.failure = failure;
function buildResponse(statusCode, body) {
    return {
        statusCode,
        body: JSON.stringify(body)
    };
}
//# sourceMappingURL=response-helper.js.map