'use strict';
import React from 'react';

import CityAddRow from './CitySelectRow';

const CityList = ({cityList, onCitySelected}) => {
   return (
      <ul className="list-group">
         {
            cityList.map((city) => {
               return <CityAddRow key={city.id} city={city}  onCitySelected={onCitySelected}/>;
            })
         }
      </ul>
   );
};

CityList.propTypes = {
   cityList: React.PropTypes.array.isRequired,
   onCitySelected: React.PropTypes.func.isRequired
};

export default CityList;
