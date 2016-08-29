'use strict';
import React from 'react';

const CityAlertAdd = ({busy, city, threshold, onAdd, onCancel, onThresholdChanged}) => {
   const enabled = !!(city.id);
   return (
      <div className="col-sm-5">
         {
            enabled
               ? <li className="list-group-item">
               <div className="input-group">
                  <span className="input-group-addon">{city.name}</span>
                  <input type="number" className="form-control" placeholder="Enter temperature..."
                         disabled={busy} onChange={onThresholdChanged} value={threshold} />
                  <span className="input-group-btn">
                     <button className="btn btn-primary btn-sm" type="button"
                             disabled={busy} onClick={onAdd} >
                        Add
                     </button>
                  </span>
                  <span className="input-group-btn">
                     <button className="btn btn-warning btn-sm" type="button"
                             disabled={busy} onClick={onCancel} >
                        Cancel
                     </button>
                  </span>
               </div>
            </li>
               : ''
         }
      </div>
   );
};

CityAlertAdd.propTypes = {
   busy: React.PropTypes.bool.isRequired,
   city: React.PropTypes.object.isRequired,
   threshold: React.PropTypes.number.isRequired,
   onAdd: React.PropTypes.func.isRequired,
   onCancel: React.PropTypes.func.isRequired,
   onThresholdChanged: React.PropTypes.func.isRequired
};

export default CityAlertAdd;
