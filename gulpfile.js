// generated on 2016-05-12 using generator-webapp 2.1.0
const gulp = require('gulp');
const gulpLoadPlugins = require('gulp-load-plugins');
const browserSync = require('browser-sync');
const del = require('del');
const wiredep = require('wiredep').stream;
const runSequence = require('run-sequence');
const replace = require('gulp-replace-task');
const less = require('gulp-less');
const path = require('path');

const $ = gulpLoadPlugins();
const reload = browserSync.reload;
const config = require('./gulp/config.js')

gulp.task('less', () => {
  return gulp.src(config.app + '/styles/less/**/*.less')
    .pipe(less({
      paths: [ path.join(__dirname, 'less', 'includes') ]
    }))
    .pipe(gulp.dest(config.app +'/styles'))
    .pipe(reload({stream: true}));
});

gulp.task('styles',['less'], () => {
  return gulp.src(config.app + '/styles/**/*.css')
    .pipe($.sourcemaps.init())
    .pipe($.autoprefixer({browsers: ['> 1%', 'last 2 versions', 'Firefox ESR']}))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest('.tmp/styles'))
    .pipe(reload({stream: true}));
});

gulp.task('scripts', () => {
  return gulp.src(config.app + '/scripts/**/*.js')
    .pipe($.plumber())
    .pipe($.sourcemaps.init())
    .pipe($.babel())
    .pipe($.sourcemaps.write('.'))
    .pipe(gulp.dest('.tmp/scripts'))
    .pipe(reload({stream: true}));
});

function lint(files, options) {
  return gulp.src(files)
    .pipe(reload({stream: true, once: true}))
    .pipe($.eslint(options))
    .pipe($.eslint.format())
    .pipe($.if(!browserSync.active, $.eslint.failAfterError()));
}

gulp.task('lint', () => {
  return lint(config.app + '/scripts/**/*.js', {
    fix: true
  })
    .pipe(gulp.dest(config.app + '/scripts'));
});


gulp.task('fonts', () => {
  return gulp.src(require('main-bower-files')('**/*.{eot,svg,ttf,woff,woff2}', function (err) {})
    .concat(config.app + '/fonts/**/*'))
    .pipe(gulp.dest('.tmp/fonts'))
    .pipe(gulp.dest(config.dist +  '/fonts'));
});


gulp.task('clean', del.bind(null, ['.tmp', config.dist + '']));

gulp.task('serve', ['styles', 'scripts', 'fonts'], () => {
  browserSync({
    notify: false,
    port: 9000,
    server: {
      baseDir: ['.tmp', config.app + ''],
      routes: {
        '/bower_components': config.bower
      }
    }
  });

gulp.watch([
  config.app + '/**/*.html',
  config.app + '/images/**/*',
  '.tmp/fonts/**/*'
]).on('change', reload);

gulp.watch([config.app + '/styles/**/*.css',config.app + '/styles/**/*.less'], ['styles']).on('change', reload);
gulp.watch(config.app + '/scripts/**/*.js', ['scripts']);
gulp.watch(config.app + '/fonts/**/*', ['fonts']);
gulp.watch('bower.json', ['wiredep', 'fonts']);
});

// inject bower components
gulp.task('wiredep', () => {
  gulp.src(config.app + '/**/*.html')
  .pipe(wiredep({
    ignorePath: /^(\.\.\/)*\.\./
  }))
  .pipe(gulp.dest(config.app));
});

gulp.task('build', ['lint', 'html', 'images', 'fonts', 'extras'], () => {
  return gulp.src(config.dist + '/**/*').pipe($.size({title: 'build', gzip: true}));
});

gulp.task('default', ['clean'], () => {
  gulp.start('build');
});


// gulp.task('lint:test', () => {
//   return lint('test/spec/**/*.js', {
//     fix: true,
//     env: {
//       mocha: true
//     }
//   })
//     .pipe(gulp.dest('test/spec/**/*.js'));
// });

// gulp.task('serve:test', ['scripts'], () => {
//   browserSync({
//     notify: false,
//     port: 9000,
//     ui: false,
//     server: {
//       baseDir: 'test',
//       routes: {
//         '/scripts': '.tmp/scripts',
//         '/bower_components': config.bower
//       }
//     }
//   });
//
// gulp.watch(config.app + '/scripts/**/*.js', ['scripts']);
// gulp.watch('test/spec/**/*.js').on('change', reload);
// gulp.watch('test/spec/**/*.js', ['lint:test']);
// });

