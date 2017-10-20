/**
 * WATERED ENDPOINT
 * GET: retrieve the last time the plant was watered
 * PUT: update the watering time of the plant
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

  if(!smartpot.isFound) return failure({ status: false });
  
  return success({ lastWatered: smartpot.item.lastWatered });

}

async function put(event) {
  const potId = event.pathParameters.potId;
  const lastWatered = new Date().getTime();

  await updateAttribute({ lastWatered }, potId);

  return success({ status: true });
}