module.exports = {
	paths: function (paths) {
		root = paths.appPath
		paths.appBuild = `${root}/target/classes/public`
		paths.appPublic = `${root}/src/main/static`
		paths.appHtml = `${root}/src/main/static/index.html`
		paths.appIndexJs = `${root}/src/main/js/index.js`
		paths.appSrc = `${root}/src/main/js`
		paths.testsSetup = `${root}/src/test/js/setupTests.js`
		return paths;
	},
	webpack: function (config) {
		config.module.rules[1].oneOf.unshift({
			test: /\.(js|mjs|jsx|ts|tsx)$/,
			use: {
				loader: "babel-loader",
				options: {
					presets: ["@babel/preset-env", "@babel/preset-react"]
				},
			},
		});
		return config;
	},
}
