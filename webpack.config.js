var webpack = require("webpack");
var path = require("path");
 
var DEV = path.resolve(__dirname, "app/assets/javascripts");
var OUTPUT = path.resolve(__dirname, "public/javascripts");
 
var config = {
	entry: DEV + "/index.jsx",
	output: {
		path: OUTPUT,
		filename: "felicia.js"
	},
	module: {
		loaders: [{
			include: DEV,
			loader: "babel",
		}]
	},
	resolve: {
		root: DEV,
		extensions: ['', '.js', '.jsx']
	}
};

module.exports = config;