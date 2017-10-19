/**
 * CREATE SMART POT TABLE
 * Configures the table structure
 * Creates the table in DynamoDB
 */

import AWS from 'aws-sdk';
import { dynamoDB } from '../dynamoDB';

const params = {
  TableName: 'smartpots',
  KeySchema: [
    { AttributeName: 'potId', KeyType: 'HASH' }
  ],
  AttributeDefinitions: [
    { AttributeName: 'potId', AttributeType: 'S'}
  ],
  ProvisionedThroughput: {
    ReadCapacityUnits: 5,
    WriteCapacityUnits: 5
  }
};

dynamoDB.createTable(params, (err, data) => {
  if (err) {
    console.error(`Unable to create table. Error JSON: ${JSON.stringify(err, null, 2)}`);
  } else {
    console.log(`Created table. Table description: ${JSON.stringify(data, null, 2)}`);
  }
});
