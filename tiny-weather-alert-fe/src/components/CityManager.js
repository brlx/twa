'use strict';
import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import _ from 'lodash';

import * as cityActions from '../actions/cityActions';
import * as alertsActions from '../actions/alertsActions';

import CitySearch from './CitySearch';
import CityList from './CityList';
import CityAlertAdd from './CityAlertAdd';

class CityManager extends React.Component {
   constructor(props, context) {
      super(props, context);

      this.state = {
         searchExpression: '',
         addingCity: {},
         addingThreshold: 22
      };

      this.citySearchChanged = this.citySearchChanged.bind(this);
      this.searchCities = this.searchCities.bind(this);
      this.onCitySelected = this.onCitySelected.bind(this);
      this.onCityAdd = this.onCityAdd.bind(this);
      this.onCityAddCanceled = this.onCityAddCanceled.bind(this);
      this.onThresholdChanged = this.onThresholdChanged.bind(this);
   }

   /** will be called when the input changes
    */
   citySearchChanged(event) {
      const searchText = event.target.value;
      this.setState({searchExpression: searchText});
   }

   /**
    * will be called when the search button is pressed
    */
   searchCities() {
      this.props.cityActions.searchCitiesByName(this.state.searchExpression);
      this.setState({addingCity: {}, addingThreshold: 22});
   }

   /**
    * will be called when the plus button near the city is added
    * @param city The selected city is passed
    */
   onCitySelected(city) {
      this.setState({addingCity: city, addingThreshold: 22});
   }

   /**
    * will be called, when the adding of the city is finished, by this time we have the data to add
    */
   onCityAdd() {
      const addingCity = this.state.addingCity;
      const foundAlert = _.find(this.props.alertList, alertModel => {
         return addingCity.id === alertModel.id;
      });
      if (foundAlert) {
         const confirmed = window.confirm('You already have an alert set up for '
                  + addingCity.name + ' (' + addingCity.country + '), with ' + foundAlert.threshold
                  + '\u00B0C threshold. Would you like to replace it?');
         if (!confirmed) {
            return;
         }
      }

      this.props.cityActions.addCityForAlert(addingCity, this.state.addingThreshold);
      this.setState({addingCity: {}, addingThreshold: 22, searchExpression: ''});
   }

   /**
    * will be called when the alert threshold input is changed
    * @param event
    */
   onThresholdChanged(event) {
      const threshold = event.target.value;
      this.setState({addingThreshold: threshold});
   }

   /**
    * will be called when the adding of a new city is canceled
    */
   onCityAddCanceled() {
      this.setState({addingCity: {}, addingThreshold: 22});
   }

   render() {
      return (
         <div className="row new-alerts">
            <div className="col-sm-3">
               <CitySearch onSearchChanged={this.citySearchChanged}
                           onSearchCities={this.searchCities}
                           busy={this.props.busy}/>
            </div>
            <div className="col-sm-4">
               <CityList cityList={this.props.cityList.slice(0, 6)} onCitySelected={this.onCitySelected}/>
            </div>
            <CityAlertAdd enabled={false}
                          busy={this.props.busy}
                          city={this.state.addingCity}
                          threshold={this.state.addingThreshold}
                          onAdd={this.onCityAdd}
                          onCancel={this.onCityAddCanceled}
                          onThresholdChanged={this.onThresholdChanged}/>
         </div>
      );
   }
}

CityManager.propTypes = {
   cityList: React.PropTypes.array.isRequired,
   alertList: React.PropTypes.array.isRequired,

   busy: React.PropTypes.bool.isRequired,

   cityActions: React.PropTypes.object.isRequired,
   alertsActions: React.PropTypes.object.isRequired
};

function mapStateToProps(state) {
   return {
      busy: state.citiesSearch.busyAdd || state.citiesSearch.busySearch,
      cityList: state.citiesSearch.results,
      alertList: state.alerts.alertsList.map(alert => {
            return {id: alert.city.id, threshold: alert.threshold};
         })
   };
}

function mapDispatchToProps(dispatch) {
   return {
      cityActions: bindActionCreators(cityActions, dispatch),
      alertsActions: bindActionCreators(alertsActions, dispatch)
   };
}

export default connect(mapStateToProps, mapDispatchToProps)(CityManager);
