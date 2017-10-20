import chai from 'chai';
import rewire from 'rewire';
const assert = chai.assert;
import sinon from 'sinon';

const moisture = rewire('../endpoints/moisture');

describe('Moisture Endpoint', () => {
  const mockSmartPot = { potId: 'pot1', moisture: 0, lastWatered: 0 };
  const mockContext = {};

  let cb;
  beforeEach(() => {
    cb = sinon.spy();
  });

  afterEach(() => {
    cb.reset();
  });

  it('GET – returns the moisture of a smart pot', async () => {
    moisture.__set__('findSmartPot', (potId) => { 
      return { item: mockSmartPot, isFound: true };  
    });
    const mockEvent = {
      httpMethod: 'GET',
      pathParameters: {
        potId: 'pot1'
      }
    };

    await moisture.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 200,
      body: JSON.stringify({ moisture: mockSmartPot.moisture })
    });
  });

  it('GET – returns proper status code when smart pot is not found', async () => {
    moisture.__set__('findSmartPot', (potId) => { 
      return { item: undefined, isFound: false };  
    });
    const mockEvent = {
      httpMethod: 'GET',
      pathParameters: {
        potId: 'pot1'
      }
    };

    await moisture.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 500,
      body: JSON.stringify({ status: false })
    });
  });

  it('PUT – returns the proper status code when the moisture of a smartpot is updated', async () => {
    moisture.__set__('updateAttribute', (attributeValue, potId) => { 
      return true;
    });
    const spy = sinon.spy(moisture.__get__('updateAttribute'));
    moisture.__set__('updateAttribute', spy);
    
    const mockEvent = {
      httpMethod: 'PUT',
      pathParameters: {
        potId: 'pot1'
      },
      body: "{ \"moisture\": 3 }"
    };

    await moisture.main(mockEvent, mockContext, cb);
    
    // Should pass the body of the event to the update function
    sinon.assert.calledWith(spy, { moisture: 3 }, 'pot1');
    
    // should return a successful status
    sinon.assert.calledWith(cb, null, {
      statusCode: 200,
      body: JSON.stringify({ status: true })
    });
  });

  it('PUT – returns the proper status code when the pot is not found', async () => {
    moisture.__set__('updateAttribute', (attributeValue, potId) => { 
      throw new Error();
    });

    const mockEvent = {
      httpMethod: 'PUT',
      pathParameters: {
        potId: 'pot1'
      },
      body: "{ \"moisture\": 3 }"
    };

    await moisture.main(mockEvent, mockContext, cb);

    sinon.assert.calledWith(cb, null, {
      statusCode: 500,
      body: JSON.stringify({ status: false })
    });

  })

});