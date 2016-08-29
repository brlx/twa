'use strict';
import * as types from '../actions/actionTypes';
import initialState from './initialState';

export default function authReducer(state = initialState.auth, action) {
   switch (action.type) {
      case types.AUTH_LOGIN_STARTED: {
         return Object.assign(
            {}
            , state
            , {busy: true}
         );
      }
      case types.AUTH_LOGIN_SUCCESS: {
         return Object.assign(
            {}
            , state
            , {
               userName: action.userName,
               busy: false,
               token: action.token
            }
         );
      }
      case types.AUTH_LOGIN_FAILED: {
         return Object.assign(
            {}
            , state
            , {busy: false}
         );
      }
      case types.AUTH_LOGOUT_STARTED: {
         return Object.assign(
            {},
            state,
            {busy: true}
         );
      }
      case types.AUTH_LOGOUT_SUCCESS: {
         return Object.assign(
            {},
            state,
            {
               userName: null,
               token: null,
               password: null,
               busy: false
            }
         );
      }
      case types.AUTH_LOGOUT_FAILED: {
         return Object.assign(
            {},
            state,
            {busy: true}
         );
      }
      default: {
         return state;
      }
   }
}
