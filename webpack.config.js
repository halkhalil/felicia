var webpack = require("webpack");
var path = require("path");
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var DEV = path.resolve(__dirname, "app/assets/javascripts");
var OUTPUT = path.resolve(__dirname, "public");
 
var config = {
	entry: DEV + "/index.jsx",
	output: {
		path: OUTPUT,
		filename: "javascripts/felicia.js"
	},
	module: {
		loaders: [{
			include: DEV,
			loader: "babel",
		}, {
			test: /\.css$/,
			loader: ExtractTextPlugin.extract("style-loader", "css-loader")
		}]
	},
	resolve: {
		root: DEV,
		extensions: ['', '.js', '.jsx']
	},
	plugins: [
        new ExtractTextPlugin("stylesheets/felicia-frontend.css")
    ]
};

module.exports = config;