'use strict';
import toastr from 'toastr';
import * as alertsApi from '../api/alertsApi';
import * as types from './actionTypes';
import {beginAjaxCall, ajaxCallError} from './ajaxStatusActions';
import * as authActions from './authActions';

export function busyAlertStarted() {
   return {
      type: types.BUSY_ALERT_STARTED
   };
}

export function loadAlertsSuccess(alerts) {
   return {
      type: types.LOAD_ALERTLIST_SUCCESS,
      alerts
   };
}

export function loadAlertsFailed(error) {
   return {
      type: types.LOAD_ALERTLIST_FAILED,
      error
   };
}

export function deleteAlertsSuccess() {
   return {
      type: types.DELETE_ALERT_SUCCESS
   };
}

export function deleteAlertsFailed() {
   return {
      type: types.DELETE_ALERT_FAILED
   };
}

export function loadAlerts() {
   // console.log('creating loadAlertsTHUNK');
   return dispatch => {
      dispatch(beginAjaxCall());
      dispatch(busyAlertStarted());
      return alertsApi.listAlertsForUser()
         .then(response => {
            let loadAlertsSuccessAction = loadAlertsSuccess(response.data.alerts);
            dispatch(loadAlertsSuccessAction);
         })
         .catch(result => {
            dispatch(ajaxCallError());
            dispatch(loadAlertsFailed());
            if (result.response && result.response.status === 401) {
               // no problem, we just have to log in again
               toastr.warning('Your session expired, please log in again.');
               dispatch(beginAjaxCall());
               dispatch(authActions.logoutSuccess());
               return;
            } else if (result.message == 'Network Error') {
               console.log('loadAlerts ERROR:', JSON.stringify(result, null, 2));
               toastr.error('Error, TWA might be down');
               return;
            }
            console.log('loadAlerts ERROR:', JSON.stringify(result, null, 2));
            toastr.error('Unknown error, please reload the page');
         });
   };
}

export function deleteAlert(alertId) {
   // console.log('creating deleteAlertsTHUNK, alertId:', alertId);
   return dispatch => {
      dispatch(beginAjaxCall());
      dispatch(busyAlertStarted());
      return alertsApi.deleteAlert(alertId)
         .then(response => {
            let deleteAlertSuccessAction = deleteAlertsSuccess();
            dispatch(deleteAlertSuccessAction);
            const loadAlertsThunk =  loadAlerts();
            loadAlertsThunk(dispatch);
         })
         .catch(error => {
            dispatch(ajaxCallError());
            dispatch(deleteAlertsFailed());
            if (error.response && error.response.status === 401) {
               // no problem, we just have to log in again
               toastr.warning('Your session expired, please log in again.');
               dispatch(beginAjaxCall());
               dispatch(authActions.logoutSuccess());
               return;
            } else if (result.message == 'Network Error') {
               console.log('deleteAlert ERROR:', JSON.stringify(result, null, 2));
               toastr.error('Error, TWA might be down');
               return;
            }
            console.log('ERROR when deleting alert: ', JSON.stringify(error, null, 2));
            toastr.error('Unknown error, please reload the page');
         });
   };
}
