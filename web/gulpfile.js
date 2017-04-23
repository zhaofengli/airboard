// == Constants ==
const paths = {
  root: './',
  source: './',
  artifact: './dist/'
};

// == Plugins ==
const fs = require('fs');
const gulp = require('gulp');
const babel = require('gulp-babel');
const htmlmin = require('gulp-htmlmin');
const cleanCSS = require('gulp-clean-css');
const browserSync = require('browser-sync');
const autoprefixer = require('gulp-autoprefixer');

// == Tasks ==
gulp.task('serve', ['build'], () => {
  browserSync.init({
    server: paths.artifact
  });
  gulp.watch(paths.source + '/*.html', ['html']);
  gulp.watch(paths.source + '/*.css', ['css']);
  gulp.watch(paths.source + '/*.js', ['js']);
});

gulp.task('build', ['html', 'css', 'js', 'libs']);
gulp.task('default', ['build']);

gulp.task('css', () => {
  return gulp.src(paths.source + '/*.css')
    .pipe(autoprefixer())
    .pipe(cleanCSS())
    .pipe(gulp.dest(paths.artifact))
    .pipe(browserSync.stream());
});

gulp.task('js', () => {
  return gulp.src(paths.source + '/*.js')
    .pipe(babel({
      presets: ['env']
    }))
    .pipe(gulp.dest(paths.artifact))
    .pipe(browserSync.stream());
});

gulp.task('html', () => {
  return gulp.src(paths.source + '/*.html')
    .pipe(htmlmin({collapseWhitespace: true}))
    .pipe(gulp.dest(paths.artifact))
    .pipe(browserSync.stream());
});

gulp.task('libs', () => {
  gulp.src(paths.source + '/quiet-js/*.{js,json,mem}')
    .pipe(gulp.dest(paths.artifact))
    .pipe(browserSync.stream());
  gulp.src(paths.source + '/node_modules/libfec/libfec.js')
    .pipe(gulp.dest(paths.artifact))
    .pipe(browserSync.stream());
});

// vim: set et ts=2 sw=2: 
