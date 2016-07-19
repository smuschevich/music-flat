const gulp = require('gulp');
const typescript = require('gulp-typescript');
const sourcemaps = require('gulp-sourcemaps');
const tslint = require('gulp-tslint');
const del = require('del');
const browserSync = require('browser-sync');
const reload = browserSync.reload;
const tscConfig = require('./tsconfig.json');
const Builder = require('systemjs-builder');
const useref = require('gulp-useref');
const gulpif = require('gulp-if');
const uglify = require('gulp-uglify');

// clean the contents of the distribution directory
gulp.task('clean', function () {
    return del(['build/app/**/*', 'dist/**/*']);
});

// linting tool
gulp.task('tslint', function() {
    return gulp.src('app/**/*.ts')
        .pipe(tslint())
        .pipe(tslint.report('verbose'));
});

// TypeScript compile
gulp.task('compile', ['clean'], function() {
    return gulp
        .src(tscConfig.files)
        .pipe(sourcemaps.init())
        .pipe(typescript(tscConfig.compilerOptions))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('build/app'));
});

// copy dependency styles
gulp.task('copy:styles', ['clean'], function() {
    return gulp.src([
            'node_modules/bootstrap/dist/css/bootstrap.css'
        ])
        .pipe(gulp.dest('styles'))
});

// run browsersync for development
gulp.task('serve', ['build'], function() {
    browserSync({
        server: {
            baseDir: '.'
        }
    });

    gulp.watch(['app/**/*', 'index.html', 'styles/**/*.css'], ['buildAndReload']);
});

// make self-executing (SFX) bundle that is independent of the SystemJS
gulp.task('bundle:systemjs', ['compile'], function() {
    var builder = new Builder();
    builder.loadConfig('./systemjs.config.js').then(function() {
        return builder.buildStatic('build/app/Bootstrap.js', 'dist/scripts/app.js', {
            normalize: true,
            minify: true,
            mangle: true,
            globalDefs: { DEBUG: false }
        });
    })
});

// copy static assets - i.e. non TypeScript compiled source
gulp.task('bundle:assets', ['clean'], function() {
    return gulp.src(['app/**/*', 'styles/**/*.css', 'images/**/*', '!app/**/*.ts'], { base : './' })
        .pipe(gulp.dest('dist'))
});

// concat, minify and uglify js files
gulp.task('bundle:index', ['clean'], function() {
    return gulp.src('index.html')
        .pipe(useref())
        .pipe(gulpif('*.js', uglify()))
        .pipe(gulp.dest('dist'))
});

gulp.task('build', ['tslint', 'compile', 'copy:styles']);
gulp.task('buildAndReload', ['build'], reload);
gulp.task('bundle', ['copy:styles', 'bundle:systemjs', 'bundle:assets', 'bundle:index']);
gulp.task('default', ['build']);