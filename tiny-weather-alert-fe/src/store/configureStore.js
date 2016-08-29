'use strict';
import {createStore, applyMiddleware, compose} from 'redux';
import rootReducer from '../reducers/rootReducer';

// TODO remove when production, dev only
import reduxImmutableStateInvariant from 'redux-immutable-state-invariant';

import thunk from 'redux-thunk';

export default function configureStore(initialState) {
   return createStore(
      rootReducer,
      initialState,
      // TODO remove when production, dev only
      compose(
         applyMiddleware(thunk, reduxImmutableStateInvariant()),
         window.devToolsExtension ? window.devToolsExtension() : f => f
      )
   );
}

