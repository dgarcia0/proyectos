<template>
  <q-page class="flex column" :style="color">
    <div class="flex flex-center"><h1>Buscador</h1></div>
    <div class="row justify-around" style="padding: 0 20px;padding-bottom:20px;">
      <div class="col-9">
        <div class="flex column justify-between">

          <div v-if="usuariosEncontrados === '' && canciones === ''">
            <h4>No se han obtenido resultados</h4>
          </div>

          <!-- ---------------------------------------- -->
          <!-- CANCIONES -->
          <!-- ---------------------------------------- -->
          <div v-if="finderSong">
            <h4>{{canciones === "" ? 'No se han encontrado canciones' : 'Canciones'}}</h4>
            <div class="row flex cancion" v-for="cancion in canciones">
              <a  class="playButton"  @click="toogleSong(cancion.id, songPlaying)">
                <i class="material-icons underlineHover font-size55" :ref="cancion.id">play_arrow</i>
              </a>
              <div class="flex column justify-between">
                <div class="cancion-info">
                  <a class="m-20 underlineHover"  @click="$router.push('/user/cancion/' + cancion.id)" color="primary">{{cancion.nombre}}</a>
                  <p> - </p>
                  <a class="m-20 underlineHover"  @click="$router.push('/user/perfil/' + cancion.autor.username)" color="primary">{{cancion.autor.username}}</a>
                </div>
              </div>
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



          <!-- Usuarios-->
          <div v-if="finderUser">
            <h4>{{usuariosEncontrados === "" ? 'No se han encontrado usuarios' : 'Usuarios'}}</h4>

            <div v-for="usuario in usuariosEncontrados" class="q-pa-md float-left" style="width: 300px;">
              <q-card @click="$router.push('/user/perfil/' + usuario.username)" class="my-card usuarioHover container">
                <img src="../../assets/usuario_icono.png" style="height: 150px;width:auto;margin:0 auto;padding: 5px 0">

                <q-card-section style="background-color: mediumslateblue;">
                  <div class="text-h5 text-align-center">{{usuario.username}}</div>
                  <div class="text-h6 text-align-center">{{usuario.nombre + " " + usuario.apellidos}}</div>
                </q-card-section>
                <div class="overlay">
                  <i class="icon material-icons underlineHover font-size55">visibility</i>
                </div>
              </q-card>
            </div>

          </div>



        </div>
      </div>
      <div class="col-3">
        <h3 class="menu-lateral">Filtro</h3>
        <div class="flex column menu-lateral">
          <span class="underlineHover" id="todo" @click="finderUser = true; finderSong = true;">Todo</span>
          <span class="underlineHover" id="usuario" @click="finderUser = true; finderSong = false;">Usuario</span>
          <span class="underlineHover" id="cancion" @click="finderUser = false; finderSong = true;">Cancion</span>
        </div>
      </div>
    </div>
  </q-page>
</template>

<style>
</style>

<script>

  import constants from '../../statics/js/configuration'
  import audioPlayer from "../../statics/js/audioPlayer";

  export default {
    name: 'Buscador',
    data () {
      return {
        isSongPlaying: false,
        songVolume: constants.DEFAULT_SONG_VOLUME,
        canciones: "",
        usuariosEncontrados: "",
        user: {},
        color:"",
        comentario: "",
        comentarioDialog: false,
        actualSongId: "",
        // Activa / desactiva que estas buscando
        finderUser: true,
        finderSong: true,
        finderData: "",

        /* Reproductor cancion */
        songPlaying: "",
        actualIcon: "play_arrow",
        addSongToPlaylistDialog: false,
        myPlaylists: [],
        myPlaylistsModal: null,
        myPlaylistsNombre: [],
        songSelected: "",
        seamless: false,
        infoText: "",
        userLikedSongs: [],

        finder: async () => {
          console.log(1);
          console.log(this.finderData);
          await this.$axios.get(constants.REST_API_URL + "/getSongsThatContains/" + this.finderData)
            .then(response => {
              if (response.data === "" || response.data.length === 0) { return; }
              this.canciones = response.data;
            });
          await this.$axios.get(constants.REST_API_URL + "/getUserThatContains/" + this.finderData)
            .then(response => {
              if (response.data === "" || response.data.length === 0) { return; }
              this.usuariosEncontrados = response.data;
            });
        },
        checkData: () => {
          if (this.canciones === "" ) {
            this.finderSong = false;
          } else { this.finderSong = true; }
          if (this.usuariosEncontrados === "") {
            this.finderUser = false;
          } else { this.finderUser = true; }
        },
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
      this.canciones = "";
      this.finderUser = "";
      this.finderData = await this.$route.params.data;
      await this.finder();
      await this.checkData();
      this.color = this.$tools.randomColor();
      this.user = await this.$tools.getUserData(this.otherUserId, this);
      this.myPlaylists = await this.$tools.getPlaylistsFromUser(this.user.username, this);

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
        this.canciones = "";
        this.finderUser = "";
        this.finderData = await to.params.data;
        await this.finder();
        await this.checkData();
        this.color = this.$tools.randomColor();
        this.user = await this.$tools.getUserData(this.otherUserId, this);
        this.myPlaylists = await this.$tools.getPlaylistsFromUser(this.user.username, this);

        for (let item of this.myPlaylists) {
          this.myPlaylistsNombre.push({
            label: item.nombre,
            value: item.nombre,
            id: item.id
          });
        }
      }
    }
  }

</script>
