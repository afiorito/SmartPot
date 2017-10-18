/**
 * CREATE SMART POT TABLE
 * Configures the table structure
 * Creates the table in DynamoDB
 */

import * as AWS from 'aws-sdk';
import { dynamoDB } from '../dynamoDB';

const params: AWS.DynamoDB.CreateTableInput = {
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

dynamoDB.createTable(params, (err: AWS.AWSError, data: AWS.DynamoDB.CreateTableOutput) => {
  if (err) {
    console.error(`Unable to create table. Error JSON: ${JSON.stringify(err, null, 2)}`);
  } else {
    console.log(`Created table. Table description: ${JSON.stringify(data, null, 2)}`);
  }
});
