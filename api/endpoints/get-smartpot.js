/**
 * FIND SMARTPOT ENDPOINT
 * By passing a unique smartpot id, the smartpot will be created
 * in DynamoDB on AWS.
 */

const findSmartPot = require('../db/actions').findSmartPot;
import { failure, success } from './helpers/response-helper';

export const main = async (event, context, cb) => {
  const potId = event.pathParameters.potId;
  try {
    const result = await findSmartPot(potId);

    if (result.isFound) {
      cb(null, success(result.item));
    } else {
      cb(null, failure({ status: false }));
    }
  } catch (e) {
    cb(null, failure({status: e }));
  }

};
