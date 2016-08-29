'use strict';
import React from 'react';

const CitySearch = ({onSearchCities, onSearchChanged, busy}) => {
   return (
      <div className="panel panel-default">
         <div className="panel-body">
            <h4>Add a new city to the existing alerts</h4>
            <div className="input-group">
               <span className="input-group-addon">City</span>
               <input type="text" className="form-control"
                      placeholder="City name"
                      onChange={onSearchChanged} disabled={busy} />
               <span className="input-group-btn">
                  <button className="btn btn-primary" type="button" disabled={busy}
                          onClick={onSearchCities}>
                     <span className="glyphicon glyphicon-search" />
                  </button>
               </span>
            </div>
         </div>
      </div>
   );
};

CitySearch.propTypes = {
   onSearchCities: React.PropTypes.func,
   onSearchChanged: React.PropTypes.func,
   busy: React.PropTypes.bool
};

export default CitySearch;
