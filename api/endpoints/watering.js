/**
 * WATERING ENDPOINT
 * GET: retrieve the watering boolean for a smartpot
 * PUT: update the watering boolean of a smartpot
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
    return success({ watering: smartpot.item.running });
  }

  return failure({ status: false });

}

async function put(event) {
  const potId = event.pathParameters.potId;
  const { watering } = JSON.parse(event.body);

  await updateAttribute({ watering }, potId);

  return success({ status: true });
}