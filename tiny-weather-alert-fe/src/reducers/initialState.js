'use strict';
export default {
   ajaxCallsInProgress: 0,
   alerts: {
      alertsList: [],
      busy: false
   },
   citiesSearch: {
      busyAdd: false,
      busySearch: false,
      results: []
   },
   auth: {
      userName: null,
      token: null,
      password: null,
      busy: false
   }
};
