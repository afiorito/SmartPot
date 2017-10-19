/**
 * FIND SMARTPOT ENDPOINT
 * By passing a unique smartpot id, the smartpot will be created
 * in DynamoDB on AWS.
 */
import { Callback, Context, Handler } from 'aws-lambda';
import { findSmartPot } from '../db/actions';
import { failure, success } from './helpers/response-helper';

const main: Handler = async (event: any, context: Context, cb: Callback): Promise<void> => {
  const potId: string = event.pathParameters.potId;
  try {
    const result: { item: object | undefined, isFound: boolean }  = await findSmartPot(potId);

    if (result.isFound) {
      cb(undefined, success({ smarpot: result.item }));
    } else {
      cb(undefined, failure({ status: false }));
    }
  } catch (e) {
    cb(undefined, failure({status: e }));
  }

};

export { main };
