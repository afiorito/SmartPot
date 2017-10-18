"use strict";
/**
 * DYNAMODB OBJECT
 * Configures the dynamodb object
 * Creates two instances of dynamoDB
 * One instance for regular db object and one for document client
 */
Object.defineProperty(exports, "__esModule", { value: true });
const AWS = require("aws-sdk");
const config = {
    region: 'us-east-1',
    endpoint: process.env.ENDPOINT || undefined
};
exports.dynamoDB = new AWS.DynamoDB(config);
exports.documentClient = new AWS.DynamoDB.DocumentClient(config);
//# sourceMappingURL=dynamoDB.js.map