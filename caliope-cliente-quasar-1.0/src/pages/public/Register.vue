<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <q-page class="flex flex-center">

    <q-dialog v-model="errorWindow" @escape-key="errorWindow = false">
      <q-card>
        <q-card-section class="row items-center">
          <div class="text-h6">Error al crear el usuario</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section>
          <p v-text="errorMsg"></p>
        </q-card-section>
      </q-card>
    </q-dialog>

    <q-dialog v-model="successWindow" @escape-key="successWindow = false" @hide="goHome">
      <q-card>
        <q-card-section class="row items-center">
          <div class="text-h6">¡Enhorabuena!</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section>
          <p v-text="successMsg"></p>
        </q-card-section>
      </q-card>
    </q-dialog>


    <div class="q-pa-md">
      <q-stepper
        v-model="step"
        ref="stepper"
        color="primary"

      >
        <q-step
          :name="1"
          title="Introduce tus credenciales"
          icon="settings"
          :done="step > 1"
        >
          <q-input
            ref="user.username"
            filled
            v-model="user.username"
            label="Nombre de usuario"
            lazy-rules
            :rules="[ val => val.length >= 3  && val.length <= 32 || 'Introduce entre 3 y 32 caracteres']"
          />

          <q-input
            ref="user.email"
            filled
            v-model="user.email"
            label="Email"
            lazy-rules
            :rules="[ val => val.includes('@') && val.includes('.') || 'Revisa tu dirección de correo']"
          />

          <q-input
            ref="user.password"
            filled
            type="password"
            v-model="user.password"
            label="Contraseña"
            lazy-rules
            :rules="[ val => val.length >= 3  && val.length <= 32 || 'Introduce entre 3 y 32 caracteres']"
          />

        </q-step>

        <q-step
          :name="2"
          title="Datos personales"
          caption="Opcional"
          icon="create_new_folder"
          :done="step > 2"
        >

          <div class="q-gutter-md">

            <q-input ref="user.nombre" filled v-model="user.name" label="Nombre"/>

            <q-input ref="user.apellidos" filled v-model="user.surname" label="Apellidos"/>

          </div>

        </q-step>


        <template v-slot:navigation>
          <q-stepper-navigation>
            <q-btn v-if="step < 2" @click="$refs.stepper.next()" color="primary" :label="'Siguiente'" />
            <q-btn v-else @click="register" color="primary" :label="'Finalizar'" />
            <q-btn v-if="step > 1" flat color="primary" @click="$refs.stepper.previous()" label="Atras" class="q-ml-sm" />
          </q-stepper-navigation>
        </template>
      </q-stepper>
    </div>
  </q-page>
</template>

<style>
</style>

<script>

  import moment from "moment";
  const md5        = require('md5');
  const constants  = require('../../statics/js/configuration');

export default {
  name: 'Register',
  data () {
    return {
      step: 1,
      accept: false,
      user: {
        username: '',
        email: '',
        password: '',
        name: '',
        surname: '',
      },
      errorMsg: '',
      successMsg: '',
      errorWindow: false,
      successWindow: false,
      register: () => {
        this.$axios.post(constants.REST_API_URL + "/registro", {
          username: this.user.username,
          email: this.user.email,
          contraseña: md5(this.user.password),
          nombre: this.user.name,
          apellidos: this.user.surname,
          permiso: 1,
          numeroSeguidores: 0,
          numeroSeguidos: 0,
          fechaRegistro: moment().format("YYYY-MM-DD")
        })
          .then(response => {
            let json = response.data;

            if (json.status !== "OK") {
              this.errorMsg = "";
              this.errorMsg = json.message;
              this.errorWindow = true;
              return;
            }

            this.successWindow = true;
            this.successMsg = json.message;

          })
          .catch(error => console.error(error))
      },
      goHome: () => {
        this.$router.push("/");
      }
    }
  }
}
</script>
