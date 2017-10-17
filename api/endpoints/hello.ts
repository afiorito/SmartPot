/**
 * Hello Handler
 */
import { Callback, Context, Handler } from 'aws-lambda';

interface IHelloResponse {
  statusCode: number;
  body: string;
}

const main: Handler = (event: any, context: Context, cb: Callback): void => {
  const response: IHelloResponse = {
    statusCode: 200,
    body: JSON.stringify({
      message: 'Hello!',
      input: event
    })
  };

  cb(undefined, response);
};

export { main };
