/**
 * MOISTURE ENDPOINT
 * GET: retrieve the moisture for a smartpot
 * PUT: update the moisture of a smartpot
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
    return success({ moisture: smartpot.item.moisture });
  }

  return failure({ status: false });

}

async function put(event) {
  const potId = event.pathParameters.potId;
  const { moisture } = JSON.parse(event.body);

  await updateAttribute({ moisture }, potId);

  return success({ status: true });
}
