'use strict';
class WebsocketMessages {

   static authenticate(clientCookie) {
      return {
         messageType: 'Authenticate',
         userName: clientCookie.username,
         token: clientCookie.authToken
      };
   }

   static authenticateStringFromCookie(clientCookie) {
      return '{'
         + 'messageType: "Authenticate", '
         + 'userName: "' + clientCookie.userName + '", '
         + 'token: "' + clientCookie.token + '"'
      + '}';
   }

   static authenticateStringFromParams(userName, token) {
      return '{'
         + 'messageType: "Authenticate", '
         + 'userName: "' + userName + '", '
         + 'token: "' + token + '"'
         + '}';
   }
}

export default WebsocketMessages;
