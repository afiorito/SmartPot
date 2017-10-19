/**
 * RESPONSE HELPERS
 * Allows for cleaner HTTP response generation
 */
export function success(body) {
  return buildResponse(200, body);

}

export function failure(error) {
  return buildResponse(500, error);
}

function buildResponse(statusCode, body) {
  return {
    statusCode,
    body: JSON.stringify(body)
  };
}
