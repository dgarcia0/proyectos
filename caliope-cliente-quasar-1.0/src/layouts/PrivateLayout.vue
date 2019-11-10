<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <q-layout view="hHh lpR fFf" ref="layout">

    <q-header reveal bordered class="bg-primary text-accent">
      <q-toolbar>
        <q-toolbar-title id="home-publica">
          <img class="logo-layout" src="" @click="$router.push('/user/home')">
        </q-toolbar-title>

        <q-input style="margin-right: 5px;" @keydown.enter="sendDataToFinder" dark dense standout v-model="text" input-class="text-right">
          <template v-slot:append>
            <q-icon @click="sendDataToFinder" name="search" />
          </template>
        </q-input>

        <q-btn flat rounded color="accent" label="Subir canción" @click="$router.push('/user/subircancion'); text = ''"/>

        <q-btn flat rounded color="accent" label="descubrir" @click="$router.push('/user/descubrir'); text = ''"/>

        <q-btn-dropdown color="primary" :label="user.username">
          <q-list>
            <q-item v-if="user.permiso == 2" clickable v-close-popup>
              <q-item-section>
                <q-btn flat rounded outline label="Administrar" @click="$router.push('/user/administrador/'); text = ''"/>
              </q-item-section>
            </q-item>

            <q-item clickable v-close-popup>
              <q-item-section>
                <q-btn flat rounded outline label="Perfil" @click="$router.push('/user/perfil/' + user.username); text = ''"/>
              </q-item-section>
            </q-item>

            <q-item clickable v-close-popup>
              <q-item-section>
                <q-btn flat rounded outline label="Opciones" @click="$router.push('/user/opciones'); text = ''"/>
              </q-item-section>
            </q-item>

            <q-item clickable v-close-popup>
              <q-item-section>
                <q-btn flat rounded outline label="Cerrar sesión" @click="closeSession"/>
              </q-item-section>
            </q-item>
          </q-list>
        </q-btn-dropdown>


      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>

    <!-- <q-footer reveal bordered class="bg-brown-5 text-white">
      <q-toolbar             @click="test"
      >
        <q-toolbar-title>
          <q-knob
            show-value
            class="text-white q-ma-md"
            v-model="songVolume"
            size="60px"
            @drag-value="changeSongVolume"
            :thickness="0.2"
            color="orange"
            center-color="black"
            track-color="transparent"
          ><q-icon name="volume_up" />
          </q-knob>

          <q-btn ref="layoutBtn" @click="toogleSong" :icon="isSongPlaying ? 'pause' : 'play_arrow'" color="primary" style="margin-right: 20px;"></q-btn>
          <q-btn @click="stopSong" icon="stop" color="primary" style="margin-right: 20px;"></q-btn>


        </q-toolbar-title>
      </q-toolbar>
    </q-footer> -->

  </q-layout>
</template>

<script>
  import audioPlayer from '../statics/js/audioPlayer'
  import constants from '../statics/js/configuration'

  export default {
    data () {

      return {
        text: "",
        username: "",
        user: { },
        isSongPlaying: audioPlayer.getSongStatus(),
        songVolume: constants.DEFAULT_SONG_VOLUME,
        imgHeader: '',
        layout: "",

        closeSession: () => {
          localStorage.clear()
          this.$router.push('/');
          this.text = '';

        },
        toogleSong: () => {
          audioPlayer.toogle();
          this.isSongPlaying = audioPlayer.getSongStatus();

        },
        stopSong: () => {
          if (audioPlayer.getSongStatus()) {
            audioPlayer.stop();
            this.isSongPlaying = false;
            audioPlayer.setSongStatus(false);
          }
        },
        changeSongVolume: () => {
          this.$tools.changeSongVolume(this.songVolume)
        },
        getUserData: () => {
          let userId = localStorage.getItem("user");
          this.$axios.get(constants.REST_API_URL + "/getUsuarioById/" + userId)
            .then(response => {
              console.log(response);
              this.user = response.data;
            });
        },
        sendDataToFinder: () => {
          this.$tools.setFinderData(this.text);
          this.$router.push("/user/buscador/" + this.text)
        },
        // refreshToken: () => {
        //   this.$axios.post(constants.AUTH_API_URL + "/refresh-token", {}, {
        //     headers: {
        //       "refreshtoken": localStorage.getItem("token")
        //     }
        //   })
        //     .then(response => {
        //       localStorage.setItem("token",response.data);
        //     })
        // },
      }
    },
    async beforeMount(){
      await this.getUserData();
      // this.refreshToken();
    },
  }
</script>
