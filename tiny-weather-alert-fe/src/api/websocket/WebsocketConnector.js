'use strict';
import messages from './WebsocketMessages';
import toastr from 'toastr';
import * as apiHelper from '../apiHelper';

class WebsocketConnector {
   constructor({url, userName, token, onAlertAction}) {
      this.url = url || apiHelper.wsRoot;
      this.userName = userName;
      this.token = token;
      this.active = true;
      this.onAlertAction = onAlertAction;
      this.states = {
         0: 'CONNECTING',
         1: 'OPEN',
         2: 'CLOSING',
         3: 'CLOSED'
      };
      this.reconnectDelay = 5000;
      this.messageHandlers = {
         Alert: this.handleAlertMessage.bind(this),
         AuthenticationSuccessful: this.handleAuthSuccessful.bind(this),
         AuthenticationFailed: this.handleAuthFailed.bind(this)
      };
      this.onOpen = this.onOpen.bind(this);
      this.onError = this.onError.bind(this);
      this.onMessage = this.onMessage.bind(this);
      this.onClose = this.onClose.bind(this);
      this.connect = this.connect.bind(this);
      this.deactivate = this.deactivate.bind(this);
   }

   onMessage(event) {
      const cmd = JSON.parse(event.data);
      this.handleCommand(cmd);
   }

   handleCommand(cmd) {
      const messageHandler = this.messageHandlers[cmd.messageType];
      if (messageHandler) {
         messageHandler(cmd);
      } else {
         console.log('MessageHandler not found for cmd: ' + JSON.stringify(cmd, null, 2));
      }
   }

   handleAlertMessage(alertCmd) {
      toastr.options.timeOut = 0;
      toastr.options.closeButton = true;
      toastr.success('Alert: city: ' + alertCmd.city + ', current temperature: ' + alertCmd.currentTemperature +
                     ', threshold: ' + alertCmd.threshold);
      this.onAlertAction && this.onAlertAction();
   }

   handleAuthSuccessful(authSuccessCmd) {
      console.log('WebsocketConnector#handleAuthSuccessful');
   }

   handleAuthFailed(authFailedCmd) {
      console.log('WebsocketConnector#handleAuthFailed, authFailedCmd=', authFailedCmd);
   }

   connect() {
      if (!(this.websocket) || this.websocket.readyState === 3) {
         console.log('WebsocketConnector#connect(): closed or not inited, connecting now');
         this.websocket = new WebSocket(this.url);
         this.websocket.onopen = this.onOpen;
         this.websocket.onerror = this.onError;
         this.websocket.onmessage = this.onMessage;
         this.websocket.onclose = this.onClose;
      } else {
         console.log('WebsocketConnector#connect(): connected or closing, NOOP');
      }
   }

   deactivate() {
      this.active = false;
      this.websocket.close();
   }

   onOpen() {
      const authMsg = messages.authenticateStringFromParams(this.userName, this.token);
      this.websocket.send(authMsg);
   }

   onError(error) {
      console.log('WebsocketConnector#onError, error=', JSON.stringify(error, null, 2));
   }

   onClose(close) {
      if (this.active) {
         setTimeout(this.connect, this.reconnectDelay);
      }
   }
}

export default WebsocketConnector;
