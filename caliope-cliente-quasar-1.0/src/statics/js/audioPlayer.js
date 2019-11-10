import constants from './configuration'

/* Clase BUFFER LOADER */
function BufferLoader(context, urlList, callback) {
  this.context = context;
  this.urlList = urlList;
  this.onload = callback;
  this.bufferList = new Array();
  this.loadCount = 0;
}

BufferLoader.prototype.loadBuffer = function(url, index) {
  var request = new XMLHttpRequest();
  request.open("GET", url, true);
  request.responseType = "arraybuffer";

  var loader = this;

  request.onload = function() {
    loader.context.decodeAudioData(
      request.response,
      function(buffer) {
        if (!buffer) {
          alert('error decoding file data: ' + url);
          return;
        }
        loader.bufferList[index] = buffer;
        if (++loader.loadCount == loader.urlList.length)
          loader.onload(loader.bufferList);
      }
    );
  }

  request.onerror = function() {
    alert('BufferLoader: XHR error');
  }

  request.send();
}

BufferLoader.prototype.load = function() {
  for (var i = 0; i < this.urlList.length; ++i)
    this.loadBuffer(this.urlList[i], i);
}

/* CARGA */

//window.onload = init;
var context;
var bufferLoader;
var playing = false;
var source;
var gainNode;
var sinea;
var isNewSong = true;
var actualTimeSong = 0;
var actualVolumeSong = constants.DEFAULT_SONG_VOLUME === 100 ? "1" : "0." + constants.DEFAULT_SONG_VOLUME;

let getSongUrl = constants.REST_API_URL + "/obtenerCancion/";
let getSongName = "";
let getUserSong = "";

function init() {
  // Fix up prefixing
    window.AudioContext = window.AudioContext || window.webkitAudioContext;
    context = new AudioContext();

    bufferLoader = new BufferLoader(
      context,
      [
        getSongUrl + "/" + getUserSong + "/" + getSongName
      ],
      finishedLoading
    );

    bufferLoader.load();
    isNewSong = !isNewSong;
}

function finishedLoading(bufferList) {

  // Creamos el know
  gainNode = context.createGain();

  console.log(bufferLoader.bufferList[0].duration);

  // Cargamos el archivo de audio.
  source = context.createBufferSource();
  source.buffer = bufferList[0];

  // Audio en loop o no
  source.loop = false;

  // Conectamos el audio con el knob para subir el volumen
  source.connect(gainNode);

  // Subir, bajar volumen
  gainNode.gain.value = actualVolumeSong;

  // Conectamos el knob con la salida de audio
  gainNode.connect(context.destination);
  source.start(actualTimeSong);

  // Oscilator
  sinea = context.createDelay(1);
  sinea.connect(gainNode);

}


function toogle() {
  //
  if (source === null || source === undefined) {
    init();
  } else {
    this.playing ? context.close() : init();
  }
  this.playing = !this.playing;
}

function getSongStatus() {
  return this.playing;
}

function setSongStatus(inStatus) {
  this.playing = inStatus;
}

function changeVolume(varNewVolume) {
  actualVolumeSong = varNewVolume;
  gainNode.gain.value = actualVolumeSong;
}

function stop() {
  context.suspend();
}

function setSong(nombreCancion) {
  getSongName = nombreCancion;
}

function setAutor(autorCancion) {
  getUserSong = autorCancion;
}

export default { stop, toogle, getSongStatus, changeVolume, setSongStatus, setSong, setAutor, init }
