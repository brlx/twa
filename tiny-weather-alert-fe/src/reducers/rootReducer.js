'use strict';
import {combineReducers} from 'redux';

// the filename contains reducer so that it is clear on the editor tab that it is a reducer
// but since it is exported default, here we can alias it
// and this name is also with which we'll reference this throughout the application, like state.alertsList
import ajaxCallsInProgress from './ajaxStatusReducer';
import alerts from './alertsReducer';
import citiesSearch from './citiesSearchReducer';
import auth from './authReducer';

/**
 * The resulting reducer calls every child reducer, and gathers their results into a single state object.
 * You can control state key names by using different keys for the reducers in the passed object.
 * http://redux.js.org/docs/api/combineReducers.html
 */
const rootReducer = combineReducers({
   ajaxCallsInProgress,
   alerts,
   citiesSearch,
   auth
});

export default rootReducer;
