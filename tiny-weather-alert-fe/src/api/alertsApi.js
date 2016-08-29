'use strict';
import axios from 'axios';

import * as apiHelper from './apiHelper';

export function listAlertsForUser() {
   const url = apiHelper.alertsApiRoot + 'list';
   return axios.get(url, {
      params: {},
      headers: {
         'TWA-auth': apiHelper.getAuthTokenString()
      }
   });
}

export function addAlert(cityId, threshold) {
   const url = apiHelper.alertsApiRoot + 'add';
   return axios.get(url, {
      params: {
         cityid: cityId,
         threshold: threshold
      },
      headers: {
         'TWA-auth': apiHelper.getAuthTokenString()
      }
   });
}

export function deleteAlert(alertId) {
   const url = apiHelper.alertsApiRoot + 'delete';
   return axios.get(url, {
      params: {
         alertid: alertId
      },
      headers: {
         'TWA-auth': apiHelper.getAuthTokenString()
      }
   });
}
