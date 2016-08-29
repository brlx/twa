'use strict';
import React from 'react';
import {Link, IndexLink} from 'react-router';
import LoadingDots from './LoadingDots';

const Header = ({loading, userName, authActions}) => {
   return (
      <nav>
         <span>Hello, {userName} </span>
         {" | "}
         <button type="button" className="btn btn-default btn-xs" onClick={authActions.logout} >
            Logout
         </button>
         {loading && <LoadingDots interval={100} dots={10} />}
      </nav>
   );
};

Header.propTypes = {
   loading: React.PropTypes.bool.isRequired,
   userName: React.PropTypes.string.isRequired,
   authActions: React.PropTypes.object.isRequired
};

export default Header;
