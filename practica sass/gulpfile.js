let gulp = require('gulp');
let sass = require('gulp-sass');


gulp.task('compilaSass', () =>{
  return gulp.src('*.scss')
    .pipe(sass())
    .pipe(gulp.dest('css'));
});
