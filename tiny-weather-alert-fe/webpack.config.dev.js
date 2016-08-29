import webpack from 'webpack';
import path from 'path';


// weback needs only this object, in this file
export default {
	debug: true,
	devtool: 'cheap-module-eval-source-map',
	noInfo: false,   // webpack will display a list of all the files it's bundling
	entry: [
		'eventsource-polyfill', // necessary for hot reloading with IE
		'webpack-hot-middleware/client?reload=true', //note that it reloads the page if hot module reloading fails.
		'./src/index' // entry point, has to be last, and the .js extension is implied
	],
	target: 'web',  // alternatively could be node

	// when in dev mode, webpack does not generate any physical files, but serves evertything from memory, but simulates these paths
	// also, these paths will be used for the prod build
	output: {
		path: __dirname + '/dist', // Note: Physical files are only output by the production build task `npm run build`.
		publicPath: '/',
		filename: 'bundle.js'
	},
	devServer: {
		contentBase: './src'
	},
	plugins: [
		new webpack.HotModuleReplacementPlugin(), // hot reloading without full browser reload
		new webpack.NoErrorsPlugin() // errors will not break the hot deployment, they will show an errormessage in the browser instead
	],
	module: {

		// this tells webpack what files it has to handle
		loaders: [
			{test: /\.js$/, include: path.join(__dirname, 'src'), loaders: ['babel']},
			{test: /(\.css)$/, loaders: ['style', 'css']},
			{test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file'},
			{test: /\.(woff|woff2)$/, loader: 'url?prefix=font/&limit=5000'},
			{test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=application/octet-stream'},
			{test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=image/svg+xml'}
		]
	}
};
