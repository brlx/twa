'use strict';
import React, {PropTypes} from 'react';
import {connect} from 'react-redux';

// react-router router will pass the children to App, so we can reference them here with this.props.children
class App extends React.Component {

   // componentWillMount() {
      // console.info('App#componentWillMount');
   // }

   // componentWillReceiveProps(nextProps) {
      // console.info('App#componentWillReceiveProps, nextProps=', nextProps);
   // }

   // componentWillUnmount() {
      // console.info('App#componentWillUnmount');
   // }

   render() {
      // console.log('App#render, loading=', this.props.loading, 'children=', this.props.children);
      return (
         <div className="container-fluid">
            {this.props.children}
         </div>
      );
   }
}

App.propTypes = {
   children: PropTypes.object.isRequired
};

function mapStateToProps(state) {
   return state;
}

export default connect(mapStateToProps)(App);
