'use strict';
import axios from 'axios';

import * as apiHelper from './apiHelper';

export function searchCitiesByName(name) {
   const url = apiHelper.citiesApiRoot + 'search';

   return axios.get(url, {
      params: {
         name: name
      },
      headers: {
         'TWA-auth': apiHelper.getAuthTokenString()
      }
   });
}
