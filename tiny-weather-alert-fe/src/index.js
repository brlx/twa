'use strict';
import 'babel-polyfill'; // for production, this should be avoided and only import the actual polyfills we need

import React from 'react';
import { render } from 'react-dom';
import configureStore from './store/configureStore';
import { Provider } from 'react-redux';
import { Router, browserHistory } from 'react-router';
import routes from './routes';

import * as cookieManager from './api/CookieManager';
import { loadAlerts } from './actions/alertsActions';
import { loginSuccess, logoutSuccess } from './actions/authActions';
import {beginAjaxCall} from './actions/ajaxStatusActions';
import WebsocketConnector from './api/websocket/WebsocketConnector';

import './styles/styles.css'; // WebPack can import CSS too
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap/fonts/glyphicons-halflings-regular.woff2';
import '../node_modules/toastr/build/toastr.min.css';

const store = configureStore();

const existingCookie = cookieManager.retrieveAuthCookie();
if (existingCookie && existingCookie.userName != 'UNAUTHENTICATED') {
   console.log('index: autCookie found for', existingCookie.userName, ', dispatching login_success');
   store.dispatch(beginAjaxCall());
   store.dispatch(loginSuccess(existingCookie.userName, existingCookie.token));
   store.dispatch(loadAlerts());
} else {
   console.log('index: autCookie NOT found (\'', JSON.stringify(existingCookie), '\', dispatching logout_success');
   store.dispatch(beginAjaxCall());
   store.dispatch(logoutSuccess());
}

// the provider wraps the whole component tree and provides them with the store using the context
render(
  <Provider store={store}>
    <Router history={browserHistory} routes={routes}/>
  </Provider>,
  document.getElementById('app')
);
