//inicializa el gulp
let gulp = require('gulp');
//minimiza el css
let cleanCSS = require('gulp-clean-css');
//minimiza el javascript
let uglify = require('gulp-uglify');
//compila el sass
let sass = require('gulp-sass');
//junta todos los archivos en uno
let concat = require('gulp-concat');
//junta todos los css en uno
let concatCSS = require('gulp-concat-css');
//borra archivos y ficheros
let clean = require('gulp-clean');

gulp.task('compilaSass', function() =>{
    return gulp.src('sass/**/*.scss')
        .pipe(sass())
        .pipe(gulp.dest('css'));
});

gulp.task('juntaCSS',()=>{
    return gulp.src(['css/application.css','http://fonts.googleapis.com/css?family=Didact+Gothic|Ribeye+Marrow'])
        .pipe(concatCSS('all.css'))
        .pipe(gulp.dest('css'));
});

gulp.task('minimizaCSS',['compilaSass','juntaCSS'], () => {
    return gulp.src('css/all.css')
        .pipe(cleanCSS())
        .pipe(gulp.dest('dist/css'));
});

gulp.task('juntaJavascript',()=>{
    return gulp.src(['js/modernizr-2.5.2.min.js','js/jquery-2.1.0.min.js','js/application.js'])
        .pipe(concat('all.js'))
        .pipe(gulp.dest('js'));
});

gulp.task('minimizarJavascript',['juntaJavascript'],() =>{
    return gulp.src('js/all.js')
        .pipe(uglify())
        .pipe(gulp.dest('dist/js'));
});

gulp.task('eliminaDist', ()=> {
    return gulp.src('dist')
        .pipe(clean());
});

gulp.task('eliminaConcatenados',()=>{
   return gulp.src(['css/all.css',"js/all.js"])
       .pipe(clean());
});