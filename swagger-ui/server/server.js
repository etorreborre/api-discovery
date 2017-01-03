var express = require('express'),
    winston = require('winston'),
    request = require('superagent'),
    server = express(),
    fs = require('fs'),
    path = require('path'),
    index = fs.readFileSync('./index.html');

winston.remove(winston.transports.Console);
winston.add(winston.transports.Console, {
    timestamp: true,
    showLevel: true
});

function generateEnv() {
    'use strict';
    var env = '';
    for(var key in process.env ) {
        if (process.env.hasOwnProperty(key)) {
            if (key.indexOf( 'SUIENV_' ) === 0 ) {
                env += '' + key + '="' + process.env[key] + '";\n';
            }
        }
    }
    // read client configuration from mint
    if (process.env.CREDENTIALS_DIR) {
        var clientConfig = JSON.parse(fs.readFileSync(path.join(process.env.CREDENTIALS_DIR, 'client.json')));
        env += 'SUIENV_OAUTH_CLIENT_ID="' + clientConfig.client_id + '";\n';
    }
    return env;
}

function writeEnv() {
    'use strict';
    var env = generateEnv();
    fs.writeFileSync('dist/env.js', env );
    winston.info('Successfully updated OAuth credentials.');
}

writeEnv();
setInterval(writeEnv, 1000 * 60 * 60);

server.use('/dist', express.static('dist'));

// tunnel tokeninfo
server.get('/tokeninfo', function(req, res) {
    'use strict';
    request
        .get(process.env.SUIENV_OAUTH_TOKENINFO_URL)
        .accept('json')
        .query({
            access_token: req.query.access_token
        })
        .end(function(err, response) {
            if (err) {
                if (err.status !== 400) {
                    // log error on tokeninfo only if it's not
                    // because of an invalid token
                    winston.error('Could not GET /tokeninfo: %d %s', err.status || 0, err.message);
                }
                return res.status(err.status || 0).send(err);
            }
            return res
                    .status(200)
                    .type('json')
                    .send(response.text);
        });
});

// redirect to index.html
server.get('/*', function(req, res) {
    'use strict';
    res
        .append('Content-Type', 'text/html')
        .status(200)
        .send(index);
});

server.listen(8080);