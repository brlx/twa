'use strict';
import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as authActions from '../actions/authActions';

class Signin extends React.Component {

   constructor(props, context) {
      super(props, context);
      this.state = {
         inputUsername: null,
         inputPassword: null
      };
      this.onUsernameChanged = this.onUsernameChanged.bind(this);
      this.onPasswordChanged = this.onPasswordChanged.bind(this);
      this.onSignin = this.onSignin.bind(this);
   }

   onUsernameChanged(event) {
      const userName = event.target.value;
      this.setState({inputUsername: userName});
   }

   onPasswordChanged(event) {
      const password = event.target.value;
      this.setState({inputPassword: password});
   }

   onSignin() {
      this.props.authActions.login(this.state.inputUsername, this.state.inputPassword);
   }

   render() {
      return (
         <div className="form-signin">
            <h2 className="form-signin-heading"> Please sign in</h2>
            <label htmlFor="inputEmail" className="sr-only">Email address</label>
            <input type="email" id="inputEmail" className="form-control" placeholder="Username"
                   required="" autoFocus="" onChange={this.onUsernameChanged} disabled="" />
            <label htmlFor="inputPassword" className="sr-only">Password</label>
            <input type="password" id="inputPassword" className="form-control" placeholder="Password"
                   required="" onChange={this.onPasswordChanged} />
            <button className="btn btn-lg btn-primary btn-block" onClick={this.onSignin} >
               Sign in
            </button>
         </div>
      );
   }
}

Signin.propTypes = {
   // userName: React.PropTypes.string.isRequired,
   // password: React.PropTypes.func.isRequired,
   authActions: React.PropTypes.object.isRequired,
   busy: React.PropTypes.bool.isRequired
};

function mapStateToProps(state) {
   return {busy: state.auth.busy};
}

function mapDispatchToProps(dispatch) {
   return {
      authActions: bindActionCreators(authActions, dispatch)
   };
}

export default connect(mapStateToProps, mapDispatchToProps)(Signin);
