"use strict";
/**
 * DYNAMODB HELPER FUNCTIONS
 */
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const dynamoDB_1 = require("./dynamoDB");
// tslint:disable-next-line:export-name
function findOrCreate(tableName, identifier) {
    return __awaiter(this, void 0, void 0, function* () {
        const getParams = {
            TableName: tableName,
            Key: {
                [identifier.primaryKey]: identifier.value
            }
        };
        const result = yield execute('get', getParams);
        if (!result.Item) {
            const putParams = {
                TableName: tableName,
                Item: {
                    [identifier.primaryKey]: identifier.value
                }
            };
            yield execute('put', putParams);
            return { item: result.Item, isFound: false };
        }
        return { item: result.Item, isFound: true };
    });
}
exports.findOrCreate = findOrCreate;
function execute(action, params) {
    return dynamoDB_1.documentClient[action](params).promise();
}
//# sourceMappingURL=actions.js.map