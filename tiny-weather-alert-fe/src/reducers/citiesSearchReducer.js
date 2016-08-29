'use strict';
import * as types from '../actions/actionTypes';
import initialState from './initialState';

export default function citiesSearchReducer(state = initialState.citiesSearch, action) {
   switch (action.type) {
      case types.LOAD_SEARCHCITIES_STARTED: {
         return Object.assign(
            {},
            state,
            {busySearch: true}
         );
      }
      case types.LOAD_SEARCHCITIES_SUCCESS: {
         return Object.assign(
            {},
            state,
            {results: action.cities},
            {busySearch: false}
         );
      }
      case types.LOAD_SEARCHCITIES_FAILED: {
         return Object.assign(
            {},
            state,
            {busySearch: false}
         );
      }
      case types.ADD_CITY_STARTED: {
         return Object.assign(
            {},
            state,
            {busyAdd: true}
         );
      }
      case types.ADD_CITY_SUCCESS: {
         return Object.assign(
            {},
            state,
            {results: []},
            {busyAdd: false}
         );
      }
      case types.ADD_CITY_FAILED: {
         return Object.assign(
            {},
            state,
            {busyAdd: false}
         );
      }
      default:
         return state;
   }
}
