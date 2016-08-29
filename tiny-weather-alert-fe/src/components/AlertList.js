'use strict';
import React, {PropTypes} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as alertsActions from '../actions/alertsActions';

import AlertListRow from './AlertListRow';

class AlertList extends React.Component {
   constructor(props, context) {
      super(props, context);
      this.createAlertDeleteCallback = this.createAlertDeleteCallback.bind(this);
   }

   createAlertDeleteCallback(alertId) {
      const alertsActions = this.props.alertsActions;
      return function () {
         alertsActions.deleteAlert(alertId);
      };
   }

   render() {
      return (
         <div className="row existing-alerts">
            <div className="col-sm-12">
               <div className="panel panel-default">
                  <div className="panel-heading">
                     <h3 className="panel-title">Your cities</h3>
                  </div>
                  <div className="panel-body">
                     <table className="table table-striped table-hover">
                        <thead>
                        <tr>
                           <th>City</th>
                           <th>Current temp.</th>
                           <th>Alert temp.</th>
                           <th>Alert status</th>
                           <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.props.alertsList.map(alert =>
                           <AlertListRow key={alert.id} alert={alert}
                                         deleteAction={this.createAlertDeleteCallback(alert.id)}
                                          busy={this.props.busy}/>
                        )}
                        </tbody>
                     </table>
                  </div>
               </div>
            </div>
         </div>
      );
   }
}

AlertList.propTypes = {
   alertsList: PropTypes.array.isRequired,
   alertsActions: PropTypes.object.isRequired
};

function mapStateToProps(state) {
   return {
      alertsList: state.alerts.alertsList,
      busy: state.alerts.busy
   };
}

function mapDispatchToProps(dispatch) {
   return {
      alertsActions: bindActionCreators(alertsActions, dispatch)
   };
}

export default connect(mapStateToProps, mapDispatchToProps)(AlertList);
