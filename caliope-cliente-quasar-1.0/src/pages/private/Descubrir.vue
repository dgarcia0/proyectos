<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <q-page class="flex column" :style="color">

    <div class="column justify-center" style="padding: 100px;padding-bottom:20px;">


      <q-tabs
        v-model="tab"
        dense
        class="text-black"
        style="background-color: gray"
        active-color="primary"
        indicator-color="primary"
        align="justify"
        narrow-indicator
      >
        <q-tab name="canciones" label="Canciones" />
        <q-tab name="playlists" label="Playlists" />
      </q-tabs>

      <q-separator />

      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="canciones">

          <div class="text-h6">Canciones</div>
          <div class="flex column justify-between">


            <!-- ---------------------------------------- -->
            <!-- CANCIONES -->
            <!-- ---------------------------------------- -->
            <div class="row flex cancion" v-for="cancion in canciones">
              <a  class="playButton"  @click="toogleSong(cancion.id, songPlaying)">
                <i class="material-icons underlineHover font-size55" :ref="cancion.id">play_arrow</i>
              </a>
              <div class="flex column justify-between">
                <div class="cancion-info">
                  <a class="m-20 underlineHover"  @click="$router.push('/user/cancion/' + cancion.id)" color="primary">{{cancion.nombre}}</a>
                  <p> - </p>
                  <a class="m-20 underlineHover"  @click="$router.push('/user/perfil/' + cancion.autor.username)" color="primary">{{cancion.autor.username}}</a>
                  <a @click="addSongToPlaylistDialog = true; songSelected = cancion.id;">
                    <i class="material-icons underlineHover font-size25">
                      playlist_add
                    </i>
                  </a>
                </div>
                <div class="cancion-opciones">

                  <a class="underlineHover" @click="openDialog(cancion.id)">Comentar</a>

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

        </q-tab-panel>

        <q-tab-panel name="playlists">
          <div class="text-h6">Playlists</div>
          <div v-for="playlist in myPlaylists" class="q-pa-md float-left " style="width: auto;">

            <q-card @click="$router.push('/user/playlists/' + playlist.dueño.username + '/' + playlist.id)" class="my-card playlistHover container" style="width: max-content;min-width: 300px;">
              <img src="../../assets/playlist.png" style="height: 150px;width:auto;margin:0 auto;padding: 5px 0">

              <q-card-section style="background-color: cornflowerblue;">
                <div class="text-h5 text-align-center">{{playlist.nombre}}</div>
              </q-card-section>
              <div class="overlay">
                <i class="icon material-icons underlineHover font-size55">hearing</i>
              </div>
            </q-card>

          </div>
        </q-tab-panel>
      </q-tab-panels>

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
      <!-- MODAL AÑADIR CANCION A PLAYLIST -->
      <!-- ---------------------------------------- -->
      <q-dialog v-model="addSongToPlaylistDialog" persistent>
        <q-card style="min-width: 400px;" >
          <q-card-section>
            <div class="text-h6" >Añadir canción a la playlist</div>
          </q-card-section>

          <q-card-section>

            <q-select style="z-index: 50;" v-model="myPlaylistsModal" :options="myPlaylistsNombre" label="Escoge tu playlist" />

          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat label="Cancelar" v-close-popup />
            <q-btn flat label="Añadir" @click="addSongToPlaylist(myPlaylistsModal.id)" v-close-popup />
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
    </div>

  </q-page>
</template>

<style>
</style>

<script>
  import constants from '../../statics/js/configuration'
  import audioPlayer from "../../statics/js/audioPlayer";

  export default {
    name: 'Descubrir',
    data () {
      return {
        tab:'canciones',

        canciones: {},
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

        /* Reproductor functions */
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
          //this.$refs.layoutBtn[0].innerHTML = this.isSongPlaying ? 'play_arrow' : 'pause';

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
        },
        addSongToPlaylist: (playlistId) => {
          this.$axios.put(constants.REST_API_URL + "/addSongToPlaylist/" + playlistId + "/" + this.songSelected)
            .then (() => {
              this.seamless = true;
              this.infoText = "¡Canción añadida correctamente!";
              setTimeout(() => this.seamless = false, 5000);
            })
            .catch(error => console.error(error));
        },

        /* Funciones */
      }
    },
    async beforeMount(){
      this.user = await this.$tools.getUserData(localStorage.getItem("user"), this);
      this.canciones = await this.$tools.getRandomSongs(5, this);
      this.myPlaylists = await this.$tools.getRandomPlaylists(5, this);
      this.color = this.$tools.randomColor();

      for (let item of this.myPlaylists) {
        this.myPlaylistsNombre.push({
          label: item.nombre,
          value: item.nombre,
          id: item.id
        });
      }
    },

  }
</script>
