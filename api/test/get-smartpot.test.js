import chai from 'chai';
import rewire from 'rewire';
const assert = chai.assert;
import sinon from 'sinon';

const getSmartpot = rewire('../endpoints/get-smartpot');

describe('Get Smartpot', () => {
  const mockSmartPot = { potId: 'pot1', moisture: 0, lastWatered: 0 };
  const mockContext = {};

  let cb;
  beforeEach(() => {
    cb = sinon.spy();
  });

  afterEach(() => {
    cb.reset();
  });

  it('returns the smarpot if it is found', async () => {
    getSmartpot.__set__('findSmartPot', (potId) => { 
      return { item: mockSmartPot, isFound: true };  
    });

    const mockEvent = {
      pathParameters: {
        potId: 'pot1'
      }
    };

    await getSmartpot.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 200,
      body: JSON.stringify(mockSmartPot)
    });
  });

  it('returns proper status code and body on failure', async () => {
    getSmartpot.__set__('findSmartPot', (potId) => { 
      return { item: undefined, isFound: false };  
    });
    const mockEvent = {
      pathParameters: {
        potId: 'pot5'
      }
    };

    await getSmartpot.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 500,
      body: JSON.stringify({ status: false })
    });
  });

});