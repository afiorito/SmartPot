/**
 * CREATE SMART POT TABLE
 */

import * as AWS from 'aws-sdk';
import { ServiceConfigurationOptions } from 'aws-sdk/lib/service';

const config: ServiceConfigurationOptions = {
  region: 'us-east-1',
  endpoint: process.env.ENDPOINT || undefined
};

const dynamoDB: AWS.DynamoDB = new AWS.DynamoDB(config);

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
