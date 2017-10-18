/**
 * RESPONSE HELPERS
 * Allows for cleaner HTTP response generation
 */

import { Response } from './types';

export function success(body: object): Response {
  return buildResponse(200, body);

}

export function failure(error: object): Response {
  return buildResponse(500, error);
}

function buildResponse(statusCode: number, body: object): Response {
  return {
    statusCode,
    body: JSON.stringify(body)
  };
}
