'use strict';

var gulp = require('gulp');
var es = require('event-stream');
var fs = require('fs');
var clean = require('gulp-clean');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');
var less = require('gulp-less');
var handlebars = require('gulp-handlebars');
var wrap = require('gulp-wrap');
var declare = require('gulp-declare');
var watch = require('gulp-watch');
var connect = require('gulp-connect');
var header = require('gulp-header');
var pkg = require('./package.json');
var order = require('gulp-order');
var jshint = require('gulp-jshint');
var scm = require('node-scm-source');
var banner = ['/**',
  ' * <%= pkg.name %> - <%= pkg.description %>',
  ' * @version v<%= pkg.version %>',
  ' * @link <%= pkg.homepage %>',
  ' * @license <%= pkg.license %>',
  ' */',
  ''].join('\n');
var node;
var spawn = require('child_process').spawn;

/**
 * Clean ups ./www folder
 */
gulp.task('clean', function() {
  return gulp
    .src('./www', {read: false})
    .pipe(clean({force: true}))
    .on('error', log);
});

/**
 * Starts node server
 */
gulp.task('server', function() {
  if (node) node.kill()
  node = spawn('node', ['server.js'], {stdio: 'inherit', cwd: 'www'})
  node.on('close', function (code) {
    if (code === 8) {
      gulp.log('Error detected, waiting for changes...');
    }
  });
})

/**
 * Processes Handlebars templates
 */
function templates() {
  return gulp
    .src(['./src/main/template/**/*'])
    .pipe(handlebars())
    .pipe(wrap('Handlebars.template(<%= contents %>)'))
    .pipe(declare({
      namespace: 'Handlebars.templates',
      noRedeclare: true, // Avoid duplicate declarations
    }))
    .on('error', log);
}

/**
 * JShint all *.js files
 */
gulp.task('lint', function () {
  return gulp.src('./src/main/javascript/**/*.js')
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'));
});

/**
 * Build a distribution
 */
gulp.task('dist', ['clean','lint'], function() {

  return es.merge(
      gulp.src([
        './src/main/javascript/**/*.js',
        './node_modules/swagger-client/browser/swagger-client.js'
      ]),
      templates()
    )
    .pipe(order(['scripts.js', 'templates.js']))
    .pipe(concat('swagger-ui.js'))
    .pipe(wrap('(function(){<%= contents %>}).call(this);'))
    .pipe(header(banner, { pkg: pkg } ))
    .pipe(gulp.dest('./www/dist'))
    .pipe(uglify())
    .on('error', log)
    .pipe(rename({extname: '.min.js'}))
    .on('error', log)
    .pipe(gulp.dest('./www/dist'))
    .pipe(connect.reload());
});

/**
 * Processes less files into CSS files
 */
gulp.task('less', ['clean'], function() {

  return gulp
    .src([
      './src/main/less/screen.less',
      './src/main/less/print.less',
      './src/main/less/reset.less'
    ])
    .pipe(less())
    .on('error', log)
    .pipe(gulp.dest('./src/main/html/css/'))
    .pipe(connect.reload());
});

/**
 * Generate scm-source.json
 */
gulp.task('scm-source', function(done) {
  fs.writeFile('scm-source.json', JSON.stringify(scm(), null, 4), function() {
    done();
  });
});


/**
 * Copy lib and html folders
 */
gulp.task('copy', ['less'], function() {

  // copy JavaScript files inside lib folder
  gulp
    .src(['./lib/**/*.{js,map}'])
    .pipe(gulp.dest('./www/dist/lib'))
    .on('error', log);

  // copy all files inside html folder
  gulp
    .src(['./src/main/html/**/*'])
    .pipe(gulp.dest('./www/dist'))
    .on('error', log);

  // copy all server side files
  gulp
    .src(['./server/**'])
    .pipe(gulp.dest('./www'))
    .on('error', log)

  // copy static html files
  gulp
    .src(['./src/main/html/*.html'])
    .pipe(gulp.dest('./www'))
    .on('error', log)
});

/**
 * Watch for changes and recompile
 */
gulp.task('watch', function() {
  return watch(['./src/**/*.{js,less,handlebars}', 'server/**/*.js'], function() {
    gulp.start('default');
  });
});

/**
 * Live reload web server of `dist`
 */
gulp.task('connect', function() {
  connect.server({
    root: 'dist',
    livereload: true
  });
});

function log(error) {
  console.error(error.toString && error.toString());
}


gulp.task('default', ['dist', 'copy']);
gulp.task('serve', ['watch', 'server']);
