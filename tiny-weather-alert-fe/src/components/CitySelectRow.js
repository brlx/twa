'use strict';
import React from 'react';

const CitySelectRow = ({city, onCitySelected}) => {
   const onSelected = () => {
      onCitySelected(city);
   };

   return (
      <li className="list-group-item">
         {city.name + ' (' + city.country + ')'}
         <button type="button" className="btn btn-primary btn-xs add-city" onClick={onSelected} >
         <span className="glyphicon glyphicon-plus" />
      </button>
      </li>
   );
};

CitySelectRow.propTypes = {
   city: React.PropTypes.object.isRequired,
   onCitySelected: React.PropTypes.func.isRequired
};

export default CitySelectRow;
