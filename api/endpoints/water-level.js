/**
 * WATER LEVEL ENDPOINT
 * GET: retrieve the water level for a smartpot
 * PUT: update the water level of a smartpot
 */

const findSmartPot = require('../db/actions').findSmartPot;
const updateAttribute = require('../db/actions').updateAttribute;
import { failure, success } from './helpers/response-helper';

export const main = async (event, context, cb) => {
  try {
    switch (event.httpMethod) {
      case 'GET':
        cb(null, await get(event));
        break;
      case 'PUT':
        cb(null, await put(event));
        break;

    };

  } catch (e) {
    cb(null, failure({ status: false }));
  }
};

async function get(event) {
  const potId = event.pathParameters.potId;

  const smartpot = await findSmartPot(potId);

  if(smartpot.isFound) {
    return success({ waterLevel: smartpot.item.waterLevel });
  }

  return failure({ status: false });

}

async function put(event) {
  const potId = event.pathParameters.potId;
  const { waterLevel } = JSON.parse(event.body);

  await updateAttribute({ waterLevel }, potId);

  return success({ status: true });
}