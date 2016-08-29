'use strict';
import * as types from '../actions/actionTypes';
import initialState from './initialState';

export default function alertsReducer(state = initialState.alerts, action) {
   switch (action.type) {
      case types.BUSY_ALERT_STARTED: {
         return Object.assign(
            {},
            state,
            {busy: true}
         );
      }
      case types.LOAD_ALERTLIST_SUCCESS: {
         return Object.assign(
            {},
            state,
            {alertsList: action.alerts},
            {busy: false}
         );
      }
      case types.LOAD_ALERTLIST_FAILED: {
         return Object.assign(
            {},
            state,
            {busy: false}
         );
      }
      case types.DELETE_ALERT_SUCCESS: {
         return Object.assign(
            {},
            state,
            {busy: false}
         );
      }
      case types.DELETE_ALERT_FAILED: {
         return Object.assign(
            {},
            state,
            {busy: false}
         );
      }
      default:
         return state;
   }
}
