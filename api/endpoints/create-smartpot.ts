/**
 * CREATE NEW SMARTPOT ENDPOINT
 * By passing a unique smartpot id, the smartpot will be created
 * in DynamoDB on AWS.
 */
import { Callback, Context, Handler } from 'aws-lambda';
import { findOrCreate } from '../db/actions';
import { failure, success } from './helpers/response-helper';

const main: Handler = async (event: any, context: Context, cb: Callback): Promise<void> => {
  const result: { item: object | undefined, isFound: boolean }  = await findOrCreate('smartpots', { primaryKey: 'potId', value: 'pot2'});

  if (!result.isFound) {
    cb(undefined, success({ status: true }));
  } else {
    cb(undefined, failure({ status: false }));
  }

};

export { main };
