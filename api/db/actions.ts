/**
 * DYNAMODB HELPER FUNCTIONS
 */

import * as AWS from 'aws-sdk';
import { documentClient } from './dynamoDB';

// tslint:disable-next-line:export-name
export async function findSmartPot(potId: string): Promise<{ item: object | undefined, isFound: boolean}> {
  console.log('hello' , potId);
  const getParams: AWS.DynamoDB.DocumentClient.GetItemInput = {
    TableName: 'smartpots',
    Key: {
      potId
    }
  };

  const result: AWS.DynamoDB.DocumentClient.GetItemOutput = await execute('get', getParams);
  if (!result.Item) {
    return { item: undefined, isFound: false };
  }

  return { item: result.Item, isFound: true };

}

function execute(action: string, params: any): Promise<any> {
  return documentClient[action](params).promise();
}
