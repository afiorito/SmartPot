/**
 * RUNNING ENDPOINT
 * GET: retrieve the running boolean for a smartpot
 * PUT: update the running boolean of a smartpot
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
    return success({ running: smartpot.item.running });
  }

  return failure({ status: false });

}

async function put(event) {
  const potId = event.pathParameters.potId;
  const { running } = JSON.parse(event.body);

  await updateAttribute({ running }, potId);

  return success({ status: true });
}