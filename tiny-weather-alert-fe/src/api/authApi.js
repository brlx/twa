'use strict';
import axios from 'axios';

import * as apiHelper from './apiHelper';

export function login(userName, password) {
   console.log('authApi login, userName=', userName);
   const url = apiHelper.authApiRoot + 'login';
   return axios.get(url, {
         params: {
            username: userName,
            password: password
         }
      }
   );
}

export function logout() {
   const url = apiHelper.authApiRoot + 'logout';
   return axios.get(url, {
      params: {},
      headers: {
         'TWA-auth': apiHelper.getAuthTokenString()
      }
   });
}
