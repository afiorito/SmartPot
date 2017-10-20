import chai from 'chai';
import sinon from 'sinon';
import rewire from 'rewire';

import { assert } from 'chai';

const actions = rewire('../../db/actions')

describe('Database Actions', () => {
  const mockSmartPots = [
    { potId: 'pot1', moisture: 0, lastWatered: 0 },
    { potId: 'pot2', moisture: 5, lastWatered: 0 }
  ];

  // Mock execute function to not use DynamoDB singleton
  actions.__set__('execute', (action, params) => new Promise((resolve, reject) => {
    for(let i = 0; i < mockSmartPots.length; i++) {
      if(mockSmartPots[i].potId == params.Key.potId) {
        resolve({ Item: mockSmartPots[i] })
        return;
      }
    }
    resolve({ });
  }));

  it('looks for a smartpot in the database', async () => {
    // Should find the smartpot if it exists in the database
    const existsTest = await actions.findSmartPot('pot1');
    assert.deepEqual(existsTest, {
      item: mockSmartPots[0],
      isFound: true
    });

    const doesNotExistTest = await actions.findSmartPot('pot3');
    assert.deepEqual(doesNotExistTest, {
      item: undefined,
      isFound: false
    });
  });

  it('Updates smartpot with correct update object', async () => {
    const execute = actions.__get__('execute');
    const spy = sinon.spy(execute);
    actions.__set__('execute', spy);

    const res = await actions.updateAttribute({ moisture: 3 }, 'pot1');

    sinon.assert.calledWith(spy, 'update', {
      TableName: 'smartpots',
      Key: { potId: 'pot1' },
      UpdateExpression: 'set moisture = :moisture',
      ExpressionAttributeValues: {
        ':moisture': 3
      },
      ConditionExpression: 'attribute_exists(potId)',
      ReturnValues: 'ALL_NEW'
    });

  });

});