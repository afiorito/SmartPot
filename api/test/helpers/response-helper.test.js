import chai from 'chai';
import * as responseHelper from '../../endpoints/helpers/response-helper';

const assert = chai.assert;

describe('Response helper', () => {
  const mockBody = {};

  it('returns proper status code and body on success', (done) => {
    const res = responseHelper.success(mockBody);
    const data = JSON.parse(res.body);

    assert.isObject(res);
    assert.equal(res.statusCode, 200);
    assert.deepEqual(data, mockBody);

    done();
  });

  it('returns proper status code and body on failure', (done) => {
    const res = responseHelper.failure(mockBody);
    const data = JSON.parse(res.body);

    assert.isObject(res);
    assert.equal(res.statusCode, 500);
    assert.deepEqual(data, mockBody);

    done();
  });

});