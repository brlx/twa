'use strict';
import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as authActions from '../actions/authActions';
import * as alertsActions from '../actions/alertsActions';
import WebsocketConnector from '../api/websocket/WebsocketConnector';

import Header from './common/Header';
import AlertList from './AlertList';
import CityManager from './CityManager';
import Signin from './Signin';

class HomePage extends React.Component {
   constructor(props, context) {
      console.info('HomePage#CONSTRUCTOR');
      super(props, context);
      this.state = {
         wsManager: null,
         userName: null,
         token: null
      };

      this.componentWillReceiveProps = this.componentWillReceiveProps.bind(this);
      this.componentWillUnmount = this.componentWillUnmount.bind(this);
      this.disconnectWebsocket = this.disconnectWebsocket.bind(this);
   }

   componentWillReceiveProps(nextProps) {
      const nextAuth = nextProps.auth,
            alertsActions = this.props.alertsActions;
      if (this.state.userName) {
         // there is a websocket now
         if (!(nextAuth.userName)) {
            this.disconnectWebsocket();
            this.setState({
               wsManager: null,
               userName: null,
               token: null
            });
         }
      } else {
         // there's no websocket yet
         if (nextAuth.userName) {
            const wsConn = new WebsocketConnector({
               userName: nextAuth.userName,
               token: nextAuth.token,
               onAlertAction: function onAlertAction() {
                  alertsActions.loadAlerts();
               }
            });
            wsConn.connect();
            this.setState({
               wsManager: wsConn,
               userName: nextAuth.userName,
               token: nextAuth.token
            });
         }
      }
   }

   componentWillUnmount() {
      this.disconnectWebsocket();
   }

   disconnectWebsocket() {
      if (this.state.wsManager) {
         try {
            this.state.wsManager.deactivate();
         } catch (error) {
            console.log('Error when closing websocket', error);
         }
      }
   }

   render() {
      return (
         <div>
            {
               this.props.isLoggedin
                  ? (
                     <div>
                        <Header
                           loading={this.props.busy} userName={this.props.userName}
                           authActions={this.props.authActions}
                        />
                        <div className="jumbotron">
                           <h1>Tiny weather alert</h1>
                           <p>A simple webapp that sends you alerts about the weather.</p>
                        </div>
                        <CityManager />
                        <AlertList alerts={this.props.alertsList}/>
                     </div>
                  )
                  : ( <Signin /> )
            }
         </div>
      );
   }
}

HomePage.propTypes = {
   alertsList: React.PropTypes.array.isRequired,
   busy: React.PropTypes.bool.isRequired,
   userName: React.PropTypes.string.isRequired,
   isLoggedin: React.PropTypes.bool.isRequired,

   authActions: React.PropTypes.object.isRequired,
   alertsActions: React.PropTypes.object.isRequired
};

function mapStateToProps(state) {
   return Object.assign(
      {},
      state,
      {
         busy: state.ajaxCallsInProgress > 0 || state.auth.busy || state.alerts.busy
                     || state.citiesSearch.busyAdd || state.citiesSearch.busySearch,
         userName: state.auth.userName,
         isLoggedin: state.auth.userName != null
      }
   );
}

function mapDispatchToProps(dispatch) {
   return {
      authActions: bindActionCreators(authActions, dispatch),
      alertsActions: bindActionCreators(alertsActions, dispatch)
   };
}

export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
