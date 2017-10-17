"use strict";
/**
 * CREATE SMART POT TABLE
 */
Object.defineProperty(exports, "__esModule", { value: true });
const AWS = require("aws-sdk");
const config = {
    region: 'us-east-1',
    endpoint: process.env.ENDPOINT || undefined
};
const dynamoDB = new AWS.DynamoDB(config);
const params = {
    TableName: 'smartpots',
    KeySchema: [
        { AttributeName: 'potId', KeyType: 'HASH' }
    ],
    AttributeDefinitions: [
        { AttributeName: 'potId', AttributeType: 'S' }
    ],
    ProvisionedThroughput: {
        ReadCapacityUnits: 5,
        WriteCapacityUnits: 5
    }
};
dynamoDB.createTable(params, (err, data) => {
    if (err) {
        console.error(`Unable to create table. Error JSON: ${JSON.stringify(err, null, 2)}`);
    }
    else {
        console.log(`Created table. Table description: ${JSON.stringify(data, null, 2)}`);
    }
});
//# sourceMappingURL=create-smartpots-table.js.map