<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <q-page class="padded-top" :style="color">

    <h2 class="text-align-center" style="margin-top:0;">Sube tu canción</h2>

    <div class="q-pa-md flex justify-center">
      <div class="q-gutter-md flex column justify-center fa-align-center" style="width: 50%;padding:50px;border:1px solid gray;border-radius: 90px; background-color: white">
        <q-uploader
          :factory="fileUpload"
          label="Solo .mp3"
          accept=".mp3"
          style="margin:0 auto;"
          ref="uploaderRef"
          bordered

        />
        <q-separator />
        <q-input
          ref="cancion.nombre"
          filled
          v-model="cancion.nombre"
          label="Nombre de la canción"
          maxlength="32"
          counter
        />
        <q-separator />
        <q-select v-model="cancion.genero" :options="generos" label="Escoge genero" />
        <q-separator />
        <q-btn @click="$refs.uploaderRef.upload()" label="Subir"></q-btn>
      </div>
    </div>


    <!-- ---------------------------------------- -->
    <!-- INFO -->
    <!-- ---------------------------------------- -->
    <q-dialog v-model="seamless" seamless position="bottom">
      <q-card>

        <q-card-section class="row items-center no-wrap">
          <div class="text-weight-bold">{{infoText}}</div>
          <q-btn flat round icon="close" v-close-popup />
          <q-btn flat round icon="home" @click="$router.push('/user/home')" />
        </q-card-section>

      </q-card>
    </q-dialog>

  </q-page>
</template>

<style>
</style>

<script>
  import constants from '../../statics/js/configuration'


  export default {
  name: 'Home',
    data () {
      return {
        multiple: false,
        user: {},
        color:"",
        cancion: {
          nombre: "",
          genero: "Seleccione un genero"
        },
        infoText: "",
        uploaderRef: "",
        seamless: false,
        generos: [],
        fileUpload: (files) => {
          let moment = require('moment');
          let now = moment().format("YYYY-MM-DD");
          let fileName = files.name.replace(".mp3", "");
          let bodyFormdata = new FormData();
          bodyFormdata.append("file", files);
          bodyFormdata.set("nombre", this.cancion.nombre);
          bodyFormdata.set("duracion", "50");
          bodyFormdata.set("cancion_path", fileName);
          bodyFormdata.set("userId", this.user.username);
          bodyFormdata.set("genero", this.cancion.genero);
          bodyFormdata.set("fechaRegistro", now);

          console.log(bodyFormdata);

          this.$axios.post(constants.REST_API_URL + "/subirCancion", bodyFormdata, {
            headers: {
              "content-type": "multipart/form-data"
            }
          })
            .then(() => {
              this.seamless = true;
              this.infoText = "Canción subida correctamente";
              setTimeout(() => this.seamless = false, 5000);
            })
            .catch(error => console.error(error))
        },
        getUserData: () => {
          let userId = localStorage.getItem("user");
          this.$axios.get(constants.REST_API_URL + "/getUsuarioById/" + userId)
            .then(response => {
              this.user = response.data;
            });
        },
        getGeneros: () => {
          this.$axios.get(constants.REST_API_URL + "/getGeneros")
            .then(response => {
              for (let item in response.data) {
                this.generos.push(response.data[item].nombre);
              }
            })
            .catch(error => console.error(error));
        }
      }
    },
    beforeMount() {
      this.getUserData();
      this.getGeneros();
      this.color = this.$tools.randomColor();
    }

  }
</script>
