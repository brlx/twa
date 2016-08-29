# Tiny weather alert #

This application is the solution for the specified challenge challenge. The application will be referenced as _TWA_ in this document.

**Please read this document before using TWA.**

### The challenge ###

The specification of TWA is the one sent via email.

I took the liberty, and added one detail to it: if the user does not sign in all day when an alert is triggered for one of their cities, than that alert is considered *missed* and will never be sent.

## Components and architecture of TWA ##

The application consists of a frontend and a backend.

The **backend** is written purely in Java EE, the only dependencies are the `jdbc` driver and Google's `gson`. The backend can be run on any full profile Java EE application server with minimal configuration. Used technologies: JPA, EJB, CDI, JAX RS, Websocket.

The **frontend** is a single page webapplication. It can be separately deployed, only the backend's address has to be provided via startup params or environment variables.
The frontend uses [React](https://facebook.github.io/react/index.html) as the DOM framework, [Bootstrap](http://getbootstrap.com/) for design and layout, and uses [Redux](https://github.com/reactjs/redux) as the data model. Further notable libs, components: [js-cookie](https://github.com/js-cookie/js-cookie) for cookie handling, [toastr](https://github.com/CodeSeven/toastr) for popup notifications, and [axios](https://github.com/mzabriskie/axios) for consuming REST services.
The frontend's setup (webpack setup, choice of helper libs) is based on [Cory House's React with Redux starter kit](https://github.com/coryhouse/react-flux-starter-kit).

The frontend consumes the REST services provided by the backend. The backend authenticates these request by looking for the header `TWA-auth` containing an authentication token. The authentication token is provided by the login service, and is stored in the browser as a cookie, so that the user does not have to log in every time.

The realtime notifications are sent to the browsers via Websockets. After login, one Websocket is opened to the backend. The websockets are authenticated in the first message sent by the client, using the same token as the REST services.

TWA is deployed in the google compute engine on a `CentOS 7` vm. It runs on `WildFly` and uses `PostgreSQL` as the db.
The frontend is served with `webpack` running on `node.js`.

## How to use ##

After login, you will be presented with a list of you already added alerts, where you can delete them, and a search box, where you can search for further cities. To search, type part of the city to the searchfiled and press the search button. To add a city for alerting, press the plus button behind it's name, and the provide the alert threshold and press add.

When alerts come in, they can be dismissed with a click.

The weather is refreshed every half hour, the alerts are sent out after these moments. To help timely evalutation, there's an API call which triggers the same refresh: just send a GET to http://twa-address:8080/twa/api/dev/refreshWeather without any params. Please be careful with this, because the weather service has a rate limit, see below.

## Weather API ##

The application consumes the [OpenWeatherMap](http://openweathermap.org/api) service. TWA is on the free tier of the service, so only 60 requests per minute are allowed. The cities can be requested only separately, so if you add more than 60 cities cumulative for all users, the periodical refresh will not succeed.
 
The service's available city list is is loaded into TWA's DB at deployment time, so it will not be updated by TWA itself. This is a limitation of the weather service, since they provide the list in a separate, downloadable file.

## Areas of possible improvement, known issues ##

### Technically ###

* frontend
    * the frontend should use https
    * public DNS instead of just an IP
    * the frontend is running in dev mode for now, you can even try the React dev tools and the Redux dev tools
    * the react code could be separated into more components
    * missing automated tests
* backend
    * the API's should use https, at least with a self signed certificate
    * send and store hashed passwords instead of plain text, eg. with bcrypt
    * cookies: the BE should generate more secure ones, preferably a hash with a salt value
    * CORS headers properly set up instead of '*' (but this makes sense only with a proper FQDN) 
    * available cities' list: the list is big enough no to be obvious to load it into memory, so it is in a db table for now. It's also big enough so that search by the city name's fragment as a wildcard is slow without a proper index, so as a workaround, at the moment **only hungarian cities are loaded**. This way the searches's speed is reasonably ok.
    * search for cities is case sensitive, and also accented character sensitive, ie. Pécs != Pecs
    * should use error codes instead of `String` error messages
    * the application is only usable in one instance per user, opening new instances will break the previos ones' authentication token and websocket

### Usability ###

* user registration and password change are completely missing for now, so there are only a few pre-inserted user with predefined passwords
* an alert should be triggered immediately if the user adds a new city for which the alert condition is met right at that moment. For now, the alert is only triggered, if/when the API is periodically polled next time and the trigger condition is met at the moment of that polling
* the GUI looks rather standard bootstrappy
* alert to smartphones with push notifications is missing for now
    
