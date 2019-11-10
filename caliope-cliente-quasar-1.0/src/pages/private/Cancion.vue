<template>
  <q-page class="flex column" :style="color">
    <div style="width: 80%;margin: 0 auto;">
      <q-card class="my-card bg-black-gradient text-white text-align-center">
        <q-card-section>
          <div class="text-h2" style="margin-bottom: 10px;">{{cancionActual.nombre}}</div>
          <div class="text-subtitle2" @click="$router.push('/user/perfil/' + cancionActual.autor.username)">{{cancionActual.autor.username}}</div>
        </q-card-section>

        <q-card-actions class="flex-center">
          <a  class="playButton"  @click="toogleSong(idCancionActual, songPlaying)">
            <i class="material-icons underlineHover font-size55" :ref="idCancionActual">play_arrow</i>
          </a>
          <a style="margin: 0 20px;" @click="doLike(idCancionActual)">
            <i  class="material-icons likeHover font-size25">
              favorite
            </i>
          </a>
          <a style="margin: 0 20px;" @click="addSongToPlaylistDialog = true; songSelected = idCancionActual;">
            <i class="material-icons underlineHover font-size25">
              playlist_add
            </i>
          </a>

          <q-btn @click="comentarioDialog = true" flat>Comentar</q-btn>

        </q-card-actions>

        <div class="flex row justify-center" style="padding-top: 20px;">
          <span style="margin: 20px;">Genero: {{cancionActual.genero.nombre}}</span>
          <span style="margin: 20px;">Reproducciones: {{cancionActual.reproducciones}}</span>
          <span style="margin: 20px;">Fecha subida: {{moment(cancionActual.fechaRegistro)}}</span>
        </div>
      </q-card>
    </div>
    <div style="width: 80%;margin: 0 auto;" class="flex column justify-between">
      <h2 style="color:white;" class="text-align-center">COMENTARIOS</h2>

      <div class="text-white text-align-center" v-if="!comentarios[0]">
        <h5>No hay comentarios</h5>
      </div>
      <!-- ---------------------------------------- -->
      <!-- COMENTARIOS -->
      <!-- ---------------------------------------- -->
      <div v-for="comentario in comentarios"  class="flex row bg-grey-14" style="margin-bottom: 20px; padding: 20px; -webkit-border-radius: 20px;-moz-border-radius: 20px;border-radius: 20px; box-shadow: 1px 1px 5px black;">
        <i style="margin-right: 20px;" class="material-icons font-size25">
          comment
        </i>
        <div color="white" class="flex column justify-between" style="width: 80%;">
          <h5 style="margin:0;" class="underlineHover" @click="$router.push('/user/perfil/' + comentario.usuario.username)">{{comentario.usuario.username}}</h5>
          <div style="word-wrap: break-word;">{{comentario.mensaje}}</div>
        </div>
      </div>
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

<script>
  import constants from '../../statics/js/configuration'
  import audioPlayer from "../../statics/js/audioPlayer";
  import moment from 'moment'

  export default {
    name: 'Cancion',
    data () {
      return {
        idCancionActual: "",
        cancionActual: {
          nombre: "",
          autor: { },
          genero: { },

        },


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
        you: "",

        moment: (fecha) => { return moment(fecha).format(constants.DATE_FORMAT) },
        comentarios: {},
        getCancion: () => {
          this.$axios.get(constants.REST_API_URL + "/getCancionById/" + this.idCancionActual)
            .then(response => {
              this.cancionActual = response.data;

            })
            .catch(error => console.error(error))
        },
        getComentarios: () => {
          this.$axios.get(constants.REST_API_URL + "/getComentariosFromCancion/" + this.idCancionActual)
            .then(response => {
              this.comentarios = response.data;

            })
            .catch(error => console.error(error));
        },
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
          actualSong.innerHTML = this.isSongPlaying ? 'play_arrow' : 'pause';
          //this.$refs.layoutBtn[0].innerHTML = this.isSongPlaying ? 'play_arrow' : 'pause';

          this.isSongPlaying = await !audioPlayer.getSongStatus();
          await this.$tools.toogleSong(cancionId, this.isSongPlaying, this);
          if (!this.isSongPlaying) {
            await this.addOneRepro(cancionId);
          }

        },
        stopSong: () => this.$tools.stopSong(this.isSongPlaying),
        doLike: (cancionId) => {
          this.$tools.doLike(cancionId, this.you, this);
          this.seamless = true;
          this.infoText = "Te gusta una nueva canción.";
          setTimeout(() => this.seamless = false, 5000);
        },
        doComment: () => {
          this.$tools.doComment(this.comentarioDialog, this.you, this.idCancionActual, this.comentario, this);
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
          console.log(playlistId);
          this.$axios.put(constants.REST_API_URL + "/addSongToPlaylist/" + playlistId + "/" + this.songSelected)
            .then (() => {
              this.seamless = true;
              this.infoText = "¡Canción añadida correctamente!";
              setTimeout(() => this.seamless = false, 5000);
            })
            .catch(error => console.error(error));
        },
      }
    },
    async beforeMount(){
      this.idCancionActual = this.$route.params.cancionId;
      await this.getComentarios();
      await this.getCancion();
      this.color = this.$tools.randomColor();
      this.you = window.localStorage.getItem("user");
      this.myPlaylists = await this.$tools.getPlaylistsFromUser(this.you, this);

      for (let item of this.myPlaylists) {
        this.myPlaylistsNombre.push({
          label: item.nombre,
          value: item.nombre,
          id: item.id
        });
      }

    },
    watch: {
      async '$route'(to, from) {
        // react to route changes...
        this.idCancionActual = to.params.cancionId;
        this.you = window.localStorage.getItem("user");

      }
    }
  }
</script>

<style scoped>

</style>
