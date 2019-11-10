<template>
  <q-page class="flex column" :style="color">
    <div class="flex flex-center"><h2>{{playlist.nombre}}</h2></div>
    <div class="flex flex-center"><a style="margin-top: -50px;" class="underlineHover" :href="'/user/perfil/' + playlist.dueño.username">{{playlist.dueño.username}}</a></div>
    <div style="width: 70%;margin: 0 auto;">
      <h2 class="text-align-center">Canciones</h2>

      <div v-if="!cancionesPlaylist[0]">
        <h6>¡Ups! Este usuario aún tiene ninguna canción dentro de su playlists.</h6>
      </div>
      <!-- ---------------------------------------- -->
      <!-- CANCION -->
      <!-- ---------------------------------------- -->
      <div class="row flex cancion" v-for="cancion in cancionesPlaylist">
        <a  class="playButton"  @click="toogleSong(cancion.id, songPlaying)">
          <i class="material-icons underlineHover font-size55" :ref="cancion.id">play_arrow</i>
        </a>
        <div class="flex column justify-between">
          <div class="cancion-info">
            <a class="m-20 underlineHover"  @click="$router.push('/user/cancion/' + cancion.id)" color="primary">{{cancion.nombre}}</a>
            <p> - </p>
            <a class="m-20 underlineHover"  @click="$router.push('/user/perfil/' + cancion.autor.username)" color="primary">{{cancion.autor.username}}</a>
          </div>
          <div class="cancion-opciones">

            <a class="underlineHover" style="margin: auto;" @click="openDialog(cancion.id)">Comentar</a>

            <a @click="doLike(cancion.id)">
              <i  class="material-icons likeHover font-size25">
                favorite
              </i>
            </a>

          </div>
        </div>
      </div>
      <!-- ---------------------------------------- -->


    </div>

    <!-- ---------------------------------------- -->
    <!-- MODAL AÑADIR COMENTARIO -->
    <!-- ---------------------------------------- -->
    <q-dialog v-model="comentarioDialog" persistent>
      <q-card style="min-width: 400px">
        <q-card-section>
          <div class="text-h6">Escribe tu comentario</div>
        </q-card-section>

        <q-card-section>
          <q-input dense v-model="comentario" autofocus @keyup.enter="comentarioDialog = false" />
        </q-card-section>

        <q-card-actions align="right" class="text-primary">
          <q-btn flat label="Cancelar" v-close-popup />
          <q-btn flat label="Añadir comentario" @click="doComment" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <!-- ---------------------------------------- -->

    <!-- ---------------------------------------- -->
    <!-- MODAL AÑADIR COMENTARIO -->
    <!-- ---------------------------------------- -->
    <q-dialog v-model="comentarioDialog" persistent>
      <q-card style="min-width: 400px">
        <q-card-section>
          <div class="text-h6">Escribe tu comentario</div>
        </q-card-section>

        <q-card-section>
          <q-input dense v-model="comentario" autofocus @keyup.enter="doComment" />
        </q-card-section>

        <q-card-actions align="right" class="text-primary">
          <q-btn flat label="Cancelar" v-close-popup />
          <q-btn flat label="Añadir comentario" @click="doComment" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <!-- ---------------------------------------- -->


    <!-- ---------------------------------------- -->
    <!-- INFO -->
    <!-- ---------------------------------------- -->
    <q-dialog v-model="seamless" seamless position="bottom">
      <q-card>

        <q-card-section class="row items-center no-wrap">
          <div class="text-weight-bold">{{infoText}}</div>
          <q-btn flat round icon="close" v-close-popup />
        </q-card-section>

      </q-card>
    </q-dialog>

  </q-page>


</template>

<style>
</style>

<script>
  import constants from '../../statics/js/configuration'
  import audioPlayer from "../../statics/js/audioPlayer";


export default {

  name: 'Playlist',
  data () {
    return {
      cancionesPlaylist: {},
      playlistId: "",
      playlist: {
        dueño: {}
      },

      /* Reproductor cancion */
      likedSongs: {},
      mySongs: {},
      otherUserId: "",
      you: "",
      user: {},
      color: "",
      /* Reproductor cancion */
      songPlaying: "",
      actualSongId: "",
      actualIcon: "play_arrow",
      comentarioDialog: false,
      comentario: "",
      songVolume: constants.DEFAULT_SONG_VOLUME,
      isSongPlaying: false,
      addSongToPlaylistDialog: false,
      myPlaylists: [],
      myPlaylistsModal: null,
      myPlaylistsNombre: [],
      songSelected: "",
      seamless: false,
      infoText: "",
      userLikedSongs: [],

      //* Reproductor functions */
      toogleSong: async (cancionId, songPlaying) => {

        // Entra si ya hay una cancion reproduciendose y es diferente a la que este sonando actualmente
        if (this.isSongPlaying && songPlaying !== cancionId) {
          // Paramos la cancion anterior y cambiamos el icono
          let refs = this.$refs;
          let oldSong = refs[this.songPlaying];
          oldSong[0].innerHTML = 'play_arrow';
          this.isSongPlaying = false;
          await audioPlayer.setSongStatus(false);
          await audioPlayer.stop();
        }

        this.songPlaying = cancionId;
        let refs = this.$refs;
        let actualSong = refs[cancionId];
        actualSong[0].innerHTML = this.isSongPlaying ? 'play_arrow' : 'pause';

        this.isSongPlaying = await !audioPlayer.getSongStatus();
        await this.$tools.toogleSong(cancionId, this.isSongPlaying, this);
        if (!this.isSongPlaying) {
          await this.addOneRepro(cancionId);
        }

      },
      stopSong: () => this.$tools.stopSong(this.isSongPlaying),
      doLike: (cancionId) => {
        this.$tools.doLike(cancionId, this.user.username, this);
        this.seamless = true;
        this.infoText = "Te gusta una nueva canción.";
        setTimeout(() => this.seamless = false, 5000);
      },
      openDialog: (cancionId) => {
        this.comentarioDialog = true;
        this.comentario = "";
        this.actualSongId = cancionId;
      },
      doComment: () => {
        this.$tools.doComment(this.comentarioDialog, this.user.username, this.actualSongId, this.comentario, this);
        this.comentarioDialog = false;
        this.seamless = true;
        this.infoText = "¡Comentario añadido correctamente!";
        setTimeout(() => this.seamless = false, 5000);

      },
      addOneRepro: (cancionId) => {
        this.$axios.put(constants.REST_API_URL + "/addNewRepro/" + cancionId)
          .catch(error => console.error(error))
      }
    }
  },
  async beforeMount(){
    this.playlistId = this.$route.params.nombrePlaylist;
    this.cancionesPlaylist = await this.$tools.getSongFromPlaylist(this.playlistId,this);
    this.playlist = await this.$tools.getPlaylistById(this.playlistId, this);
    this.color = this.$tools.randomColor();
  },
}
</script>
