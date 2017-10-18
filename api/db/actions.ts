/**
 * DYNAMODB HELPER FUNCTIONS
 */

import * as AWS from 'aws-sdk';
import { documentClient } from './dynamoDB';

// tslint:disable-next-line:export-name
export async function findOrCreate(
  tableName: string,
  identifier: { primaryKey: string, value: any}
): Promise<{ item: object | undefined, isFound: boolean}> {
  const getParams: AWS.DynamoDB.DocumentClient.GetItemInput = {
    TableName: tableName,
    Key: {
      [identifier.primaryKey]: identifier.value
    }
  };

  const result: AWS.DynamoDB.DocumentClient.GetItemOutput = await execute('get', getParams);
  if (!result.Item) {
    const putParams: AWS.DynamoDB.DocumentClient.PutItemInput = {
      TableName: tableName,
      Item: {
        [identifier.primaryKey]: identifier.value
      }
    };

    await execute('put', putParams);

    return { item: result.Item, isFound: false };
  }

  return { item: result.Item, isFound: true };

}

function execute(action: string, params: any): Promise<any> {
  return documentClient[action](params).promise();
}
