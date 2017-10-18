"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const actions_1 = require("../db/actions");
const response_helper_1 = require("./helpers/response-helper");
const main = (event, context, cb) => __awaiter(this, void 0, void 0, function* () {
    const result = yield actions_1.findOrCreate('smartpots', { primaryKey: 'potId', value: 'pot2' });
    if (!result.isFound) {
        cb(undefined, response_helper_1.success({ status: true }));
    }
    else {
        cb(undefined, response_helper_1.failure({ status: false }));
    }
});
exports.main = main;
//# sourceMappingURL=create-smartpot.js.map