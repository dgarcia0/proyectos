import audioPlayer from './audioPlayer'
import constants from './configuration'


let finderData = "";

/* Obtener los datos de un usuario */
async function getUserData (userName, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getUsuarioById/" + userName)
    .then(response => {
      return response.data;
    });

}

/* Play/Stop una cancion */
async function toogleSong (cancionId, isSongPlaying, vue) {
  await vue.$axios.get(constants.REST_API_URL + "/getSongFilenameById/" + cancionId)
    .then(async response => {
      let nombreCancion = response.data;
      audioPlayer.setSong(nombreCancion);
      await vue.$axios.get(constants.REST_API_URL + "/getAutorCancionByCancionId/" + cancionId)
        .then(response => {
          let autorCancion = response.data;
          audioPlayer.setAutor(autorCancion);
          audioPlayer.toogle();
        });
      isSongPlaying = audioPlayer.getSongStatus();
    });
}

/* Stop cancion */
function stopSong(isSongPlaying) {
  if (audioPlayer.getSongStatus()) {
    audioPlayer.stop();
    isSongPlaying = false;
    audioPlayer.setSongStatus(false);
  }
}


/* Cambiar volumen de una cancion */
function changeSongVolume (songVolume) {
  if (audioPlayer.getSongStatus()) {
    if (songVolume < 10) {
      audioPlayer.changeVolume("0.0" + songVolume);
    } else {
      audioPlayer.changeVolume("0." + songVolume);
    }
  }
}

/* Dar like */
function doLike(cancionId, username, vue) {
  vue.$axios.put(constants.REST_API_URL + "/addSongToLikedList/" + cancionId + "/" + username)
      .catch(error => console.error(error))
}

/* Quitar like */
function doUnLike(cancionId, username, vue) {
  vue.$axios.post(constants.REST_API_URL + "/removeSongFromLikedList/" + cancionId + "/" + username)
    .catch(error => console.error(error))
}

/* ¿Le gusta a un usuario x cancion? */
function isThisSongLikedByTheUser(cancionId, username, vue) {
  return vue.$axios.get(constants.REST_API_URL + "/isThisSongLikedByTheUser/" + cancionId + "/" + username)
    .then(response => {
      return response.data;
    })
    .catch(error => console.error(error))
}

/* Comentar */
async function doComment(comentarioDialog, userId, songId, comentario, vue ) {
  comentarioDialog = true;
  await vue.$axios.put(constants.REST_API_URL + "/addCommentByCancionId/" + userId + "/" + songId, {
    "mensaje": comentario
  })
    .catch(error => console.error(error))
}

/* Obtener las canciones */
async function getAllSongs(vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getCanciones")
    .then(response => {
      return response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener las canciones */
async function getUserSongs(username, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getSongsFromUser/" + username)
    .then(response => {
      return response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener un numero de cancion aleatorias */
async function getRandomSongs(numberSongs, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getRandomSongs/" + numberSongs)
    .then(response => {
      return response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener canciones de mg de x usuario */
async function getLikedSongs(username, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getLikedSongsByUserId/" + username)
    .then(response => {
      return  response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener todas las playlists */
async function getPlaylists(vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getPlaylist")
    .then(response => {
      return  response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener las playlists de un usuario */
async function getPlaylistsFromUser(username, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getUserPlaylistsByUserId/" + username)
    .then(response => {
      return  response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener las canciones de una playlists en concreto */
async function getSongFromPlaylist(playlistId, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getCancionesFromPlaylist/" + playlistId)
    .then(response => {
      return  response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener playlist por id */
async function getPlaylistById(playlistId, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getPlaylistById/" + playlistId)
    .then(response => {
      return  response.data;
    })
    .catch(error => console.error(error))
}

/* Obtener playlists random */
async function getRandomPlaylists(number, vue) {
  return await vue.$axios.get(constants.REST_API_URL + "/getRandomPlaylists/" + number)
    .then(response => {
      return  response.data;
    })
    .catch(error => console.error(error))
}

function getFinderData() {
  return finderData;
}

function setFinderData(newFinderData) {
  finderData = newFinderData;
}

function randomColor() {
  let array_colors = ["#373b44, #4286f4","#203a43, #2c5364","#373b44, #4286f4","#8360c3, #2ebf91","#ad5389, #3c1053","#642b73, #c6426e"];
  return 'background: linear-gradient(to bottom, '+array_colors[Math.floor(Math.random() * 5)]+');';
}

export default { changeSongVolume, toogleSong, stopSong, doLike, doComment, getAllSongs,
  getUserData,getUserSongs, getLikedSongs, getPlaylists, getFinderData, setFinderData, getPlaylistsFromUser,
  getSongFromPlaylist, getPlaylistById, randomColor,doUnLike, isThisSongLikedByTheUser,getRandomSongs,
  getRandomPlaylists}
