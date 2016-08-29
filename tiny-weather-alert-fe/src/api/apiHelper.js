'use strict';
import {retrieveAuthCookie} from './CookieManager';

const server = 'localhost:8080/twa';
const appRoot = 'http://' + server;

export const alertsApiRoot = appRoot + '/api/alerts/';
export const authApiRoot = appRoot + '/api/auth/';
export const citiesApiRoot = appRoot + '/api/cities/';
export const wsRoot = 'ws://' + server + '/realtimealerts';

export const HEADER_AUTH_NAME = 'TWA-auth';

export function getAuthToken() {
   return retrieveAuthCookie();
}

export function getAuthTokenString() {
   const authCookie = retrieveAuthCookie();
   return '{userName: "' + authCookie.userName + '", token: "' + authCookie.token +'"}';
}
