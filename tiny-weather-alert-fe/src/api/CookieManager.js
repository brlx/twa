'use strict';
import Cookies from 'js-cookie';

const authCookieName = 'twa-auth';

export function retrieveAuthCookieString() {
   const storedCookieString = Cookies.get(authCookieName);
   return storedCookieString;
}

export function retrieveAuthCookie() {
   const cookieString = retrieveAuthCookieString();
   let cookie;
   if (cookieString) {
      try {
         cookie = JSON.parse(cookieString);
      } catch (e) {
         console.log('CookieManager#retrieveAuthCookie, error when parsing cookieString \''
            + cookieString + '\', falling back to UNAUTHENTICATED');
         cookie = {userName: 'UNAUTHENTICATED', token: 'UNAUTHENTICATED'};
      }
   } else {
      cookie = {userName: 'UNAUTHENTICATED', token: 'UNAUTHENTICATED'};
   }
   return cookie;
}

export function setAuthCookieWithParams(userName, token) {
   const cookieValue = {
      userName: userName,
      token: token
   };
   Cookies.set(authCookieName,
               cookieValue,
               {expires: 30}
   );
}

export function setAuthCookie(authCookie) {
   const cookieValue = JSON.stringify(authCookie);
   Cookies.set(authCookieName,
               cookieValue,
               {expires: 30}
   );
}

export function removeAuthCookie() {
   Cookies.remove(authCookieName);
}
