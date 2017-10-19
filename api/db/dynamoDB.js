/**
 * DYNAMODB OBJECT
 * Configures the dynamodb object
 * Creates two instances of dynamoDB
 * One instance for regular db object and one for document client
 */

import * as AWS from 'aws-sdk';
import { ServiceConfigurationOptions } from 'aws-sdk/lib/service';

const config = {
  region: 'us-east-1',
  endpoint: process.env.ENDPOINT || undefined
};

export const dynamoDB = new AWS.DynamoDB(config);
export const documentClient = new AWS.DynamoDB.DocumentClient(config);
