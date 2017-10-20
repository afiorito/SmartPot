import chai from 'chai';
import rewire from 'rewire';
const assert = chai.assert;
import sinon from 'sinon';

const watered = rewire('../endpoints/watered');

describe('Last Watered Endpoint', () => {
  const mockSmartPot = { potId: 'pot1', moisture: 0, lastWatered: 0 };
  const mockContext = {};

  let cb;

  // mock the clock since last watered uses current time
  const now = new Date().getTime();
  let clock;
  let sandbox;
  beforeEach(() => {
    sandbox = sinon.sandbox.create();
    clock = sinon.useFakeTimers(now);
    cb = sinon.spy();
  });

  afterEach(() => {
    cb.reset();
    sandbox.restore();
    clock.restore();
  });

  it('GET – returns the last time watered of a smart pot', async () => {
    watered.__set__('findSmartPot', (potId) => { 
      return { item: mockSmartPot, isFound: true };  
    });
    const mockEvent = {
      httpMethod: 'GET',
      pathParameters: {
        potId: 'pot1'
      }
    };

    await watered.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 200,
      body: JSON.stringify({ lastWatered: mockSmartPot.lastWatered })
    });
  });

  it('GET – returns proper status code when smart pot is not found', async () => {
    watered.__set__('findSmartPot', (potId) => { 
      return { item: undefined, isFound: false };  
    });
    const mockEvent = {
      httpMethod: 'GET',
      pathParameters: {
        potId: 'pot1'
      }
    };

    await watered.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 500,
      body: JSON.stringify({ status: false })
    });
  });

  it('PUT – returns the proper status code when the moisture of a smartpot is updated', async () => {
    watered.__set__('updateAttribute', (attributeValue, potId) => { 
      return true;
    });
    const spy = sinon.spy(watered.__get__('updateAttribute'));
    watered.__set__('updateAttribute', spy);
    
    const mockEvent = {
      httpMethod: 'PUT',
      pathParameters: {
        potId: 'pot1'
      }
    };

    await watered.main(mockEvent, mockContext, cb);
    
    // Should pass the body of the event to the update function
    sinon.assert.calledWith(spy, { lastWatered: now }, 'pot1');
    
    // should return a successful status
    sinon.assert.calledWith(cb, null, {
      statusCode: 200,
      body: JSON.stringify({ status: true })
    });
  });

  it('PUT – returns the proper status code when the pot is not found', async () => {
    watered.__set__('updateAttribute', (attributeValue, potId) => { 
      throw new Error();
    });

    const mockEvent = {
      httpMethod: 'PUT',
      pathParameters: {
        potId: 'pot1'
      }
    };

    await watered.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 500,
      body: JSON.stringify({ status: false })
    });

  })

});