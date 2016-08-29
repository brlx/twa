'use strict';
import React from 'react';
import {Route, IndexRoute} from 'react-router';
import App from './components/App';
import HomePage from './components/HomePage';

// if they just load the path '/', display the HomePage,
// else display the other routes specified

export default (
   <Route path="/" component={App}>
      <IndexRoute component={HomePage}/>
   </Route>
);
