"use strict";
/**
 * CREATE SMART POT TABLE
 * Configures the table structure
 * Creates the table in DynamoDB
 */
Object.defineProperty(exports, "__esModule", { value: true });
const dynamoDB_1 = require("../dynamoDB");
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
dynamoDB_1.dynamoDB.createTable(params, (err, data) => {
    if (err) {
        console.error(`Unable to create table. Error JSON: ${JSON.stringify(err, null, 2)}`);
    }
    else {
        console.log(`Created table. Table description: ${JSON.stringify(data, null, 2)}`);
    }
});
//# sourceMappingURL=create-smartpots-table.js.map