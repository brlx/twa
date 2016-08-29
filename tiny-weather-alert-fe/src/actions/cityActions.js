'use strict';
import toastr from 'toastr';

import * as citiesApi from '../api/citiesApi';
import * as alertsApi from '../api/alertsApi';

import * as alertsActions from './alertsActions';
import * as authActions from './authActions';

import * as types from './actionTypes';
import {beginAjaxCall, ajaxCallError} from './ajaxStatusActions';

export function loadCitiesStarted() {
   return {
      type: types.LOAD_SEARCHCITIES_STARTED
   };
}

export function loadCitiesSuccess(cities) {
   return {
      type: types.LOAD_SEARCHCITIES_SUCCESS,
      cities
   };
}

export function loadCitiesFailed() {
   return {
      type: types.LOAD_SEARCHCITIES_FAILED
   };
}

export function addCityStarted() {
   return {
      type: types.ADD_CITY_STARTED
   };
}

export function addCitySuccess() {
   return {
      type: types.ADD_CITY_SUCCESS
   };
}

export function addCityFailed() {
   return {
      type: types.ADD_CITY_FAILED
   };
}

export function searchCitiesByName(name) {
   return dispatch => {
      if (!name || name == '') {
         dispatch(loadCitiesSuccess([]));
         return;
      }
      dispatch(beginAjaxCall());
      dispatch(loadCitiesStarted());
      return citiesApi.searchCitiesByName(name)
      // TODO check the status of the returned stuff
         .then(response => {
            let loadCitiesSuccessAction = loadCitiesSuccess(response.data.cities);
            dispatch(loadCitiesSuccessAction);
         })
         .catch(error => {
            dispatch(ajaxCallError());
            dispatch(loadCitiesFailed());
            if (error.response.status === 401) {
               // no problem, we just have to log in again
               toastr.warning('Your session expired, please log in again.');
               dispatch(beginAjaxCall());
               dispatch(authActions.logoutSuccess());
               return;
            }
            // unknown error
            console.log('searchCities error: ', JSON.stringify(error, null, 2));
            toastr.error('Unknown error, please reload the page');
         });
   };
}

export function addCityForAlert(city, threshold) {
   return dispatch => {
      dispatch(beginAjaxCall());
      dispatch(addCityStarted());
      return alertsApi.addAlert(city.id, threshold)
         .then(response => {
            // TODO watch business errors
            dispatch(addCitySuccess());
            const loadAlertsThunk = alertsActions.loadAlerts();
            loadAlertsThunk(dispatch);
         })
         .catch(error => {
            dispatch(ajaxCallError());
            dispatch(addCityFailed());
            if (error.response.status === 401) {
               // no problem, we just have to log in again
               toastr.warning('Your session expired, please log in again.');
               dispatch(beginAjaxCall());
               dispatch(authActions.logoutSuccess());
               return;
            }
            console.log('addCity Error: ', JSON.stringify(error, null, 2));
            toastr.error('Unknown error, please reload the page');
         });
   };
}
