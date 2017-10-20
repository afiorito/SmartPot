/**
 * DYNAMODB HELPER FUNCTIONS
 */

import * as AWS from 'aws-sdk';
import { documentClient } from './dynamoDB';

export async function findSmartPot(potId) {
  
  const result = await getSmartPot(potId);
  if (!result.Item) {
    return { item: undefined, isFound: false };
  }

  return { item: result.Item, isFound: true };
}

export async function updateMoisture(moisture, potId) {
  const params = {
    TableName: 'smartpots',
    Key: {
      potId
    },
    UpdateExpression: 'set moisture = :moisture',
    ExpressionAttributeValues: {
      ':moisture': moisture
    },
    ConditionExpression: 'attribute_exists(potId)',
    ReturnValues: 'ALL_NEW'
  };

  return execute('update', params);

}

export function getSmartPot(potId) {
  const getParams = {
    TableName: 'smartpots',
    Key: {
      potId
    }
  };

  return execute('get', getParams);
}

function execute(action, params) {
  return documentClient[action](params).promise();
}
