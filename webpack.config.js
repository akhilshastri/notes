var webpack = require('webpack');
var path = require('path');
module.exports = {
    entry: {
       "main" : './src/index.js',
        "framework": ["jquery", "underscore", "angular","angular-ui-router", "angular-aria", "angular-material"]

    },
    output: {
        path: path.join(__dirname , '../build/js'),
        filename: '[name].bundle.js'
    },
    module: {
        preLoaders: [
            {test: /\.js$/, loader: "eslint-loader", exclude: /node_modules/}
        ],
        loaders: [
            { test: /.js$/, loader: 'babel-loader',exclude: /node_modules/,query: {
                optional: ["es7.decorators", "es7.classProperties"]
            }}

        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"framework", /* filename= */"framework.bundle.js")
    ]
};
