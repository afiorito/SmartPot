/**
 * DYNAMODB HELPER FUNCTIONS
 */

import * as AWS from 'aws-sdk';
import { documentClient } from './dynamoDB';

export async function findSmartPot(potId) {
  const getParams = {
    TableName: 'smartpots',
    Key: {
      potId
    }
  };

  const result = await execute('get', getParams);
  if (!result.Item) {
    return { item: undefined, isFound: false };
  }

  return { item: result.Item, isFound: true };

}

function execute(action, params) {
  return documentClient[action](params).promise();
}
