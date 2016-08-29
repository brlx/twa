'use strict';
import toastr from 'toastr';

import * as types from './actionTypes';
import {beginAjaxCall, ajaxCallError} from './ajaxStatusActions';
import * as alertsActions from './alertsActions';
import * as authApi from '../api/authApi';

import *as cookieManager from '../api/CookieManager';

export function loginStarted() {
   return {
      type: types.AUTH_LOGIN_STARTED
   };
}

export function loginSuccess(userName, token) {
   return {
      type: types.AUTH_LOGIN_SUCCESS,
      userName,
      token
   };
}

export function loginFailed() {
   return {
      type: types.AUTH_LOGIN_FAILED
   };
}

export function logoutStarted() {
   return {
      type: types.AUTH_LOGOUT_STARTED
   };
}

export function logoutSuccess() {
   return {
      type: types.AUTH_LOGOUT_SUCCESS
   };
}

export function logoutFailed() {
   return {
      type: types.AUTH_LOGOUT_FAILED
   };
}

export function login(userName, password) {
   return dispatch => {
      dispatch(beginAjaxCall());
      dispatch(loginStarted());
      return authApi.login(userName, password)
         .then(response => {
            const data = response.data;
            if (data.success) {
               cookieManager.setAuthCookie(data.cookie);
               dispatch(loginSuccess(userName, data.cookie.token));

               const loadAlertsThunk = alertsActions.loadAlerts();
               loadAlertsThunk(dispatch);
            } else {
               console.log('loginTHUNK, response received, but unSuccessful, message: ' + data.message);
               toastr.error('Unsuccessful login: ' + data.message);
               dispatch(ajaxCallError());
               dispatch(loginFailed());
            }
         })
         .catch(error => {
            dispatch(ajaxCallError());
            if (error.message == 'Network Error') {
               console.log('login ERROR:', JSON.stringify(error, null, 2));
               toastr.error('Error, TWA might be down');
               return;
            }
            toastr.error('Error when logging in, please reload the page');
            console.log('authActions#login ERROR: ', JSON.stringify(error, null, 2));
            // TODO toastr
            dispatch(loginFailed());
         });
   };
}

export function logout() {
   return dispatch => {
      dispatch(beginAjaxCall());
      dispatch(logoutStarted());
      return authApi.logout().then(response => {
         dispatch(logoutSuccess());
         cookieManager.removeAuthCookie();
         dispatch(alertsActions.loadAlertsSuccess([]));
      }).catch(error => {
         dispatch(ajaxCallError());
         dispatch(logoutFailed());
         if (error.response && error.response.status === 401) {
            // no problem, we just have to log in again
            toastr.warning('Your session expired, please log in again.');
            dispatch(beginAjaxCall());
            dispatch(logoutSuccess());
            return;
         }
         console.log('logout error: ', JSON.stringify(error, null, error));
         toastr.error('Unknown error, please reload the page');
      });
   };
}
