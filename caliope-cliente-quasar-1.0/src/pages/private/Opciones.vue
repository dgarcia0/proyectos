<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <q-page class="flex column" :style="color">
      <h2 class="text-align-center">OPCIONES</h2>
      <div class="q-gutter-y-md flex justify-center column" style="width:50%;margin:0 auto;margin-bottom: 50px;">
            <q-tabs
              v-model="tab"
              dense
              class="text-grey"
              active-color="primary"
              indicator-color="primary"
              align="justify"
              narrow-indicator
            >
              <q-tab name="info" icon="info" label="Informacion" />
            </q-tabs>

            <q-tab-panels
              v-model="tab"
              animated
              transition-prev="jump-up"
              transition-next="jump-up"
            >
              <q-tab-panel name="info">
                <div class="text-h4 q-mb-md">Informacion basica</div>
                <q-input
                  ref="user.username"
                  filled
                  v-model="user.username"
                  label="Nombre de usuario"
                  lazy-rules
                  :rules="[ val => val.length >= 3  && val.length <= 32 || 'Introduce entre 3 y 32 caracteres']"
                  disabled
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
                  ref="user.nombre"
                  filled
                  v-model="user.nombre"
                  label="Nombre"
                  lazy-rules
                  :rules="[ val => val.includes('@') && val.includes('.') || 'Revisa tu dirección de correo']"
                />

                <q-input
                  ref="user.apellidos"
                  filled
                  v-model="user.apellidos"
                  label="Apellidos"
                  style="margin-bottom: 20px"
                />

                <q-btn
                  label="Cambiar contraseña"
                  class="q-mt-md"
                  color="teal"
                  @click="changePasswordModal = true"
                  style="margin-right: 50px;"
                >
                  <template v-slot:loading>
                    <q-spinner-facebook />
                  </template>
                </q-btn>

                <q-btn
                  type="submit"
                  :loading="submitting"
                  label="Guardar"
                  class="q-mt-md"
                  color="teal"
                  @click="updateUserData"
                >
                  <template v-slot:loading>
                    <q-spinner-facebook />
                  </template>
                </q-btn>
              </q-tab-panel>

            </q-tab-panels>
      </div>


      <!-- ---------------------------------------- -->
      <!-- INFO -->
      <!-- ---------------------------------------- -->
      <q-dialog v-model="changePasswordModal" persistent>
        <q-card style="min-width: 400px;" >
          <q-card-section>
            <div class="text-h6" >Cambiar contraseña</div>
          </q-card-section>

          <q-card-section>

            <q-input
              filled
              v-model="userPassword.oldPassword"
              label="Escribe tu contraseña actual"
              type="password"
              style="margin-bottom: 20px"
            />

            <q-input
              filled
              v-model="userPassword.newPassword"
              label="Nueva contraseña"
              type="password"
              style="margin-bottom: 20px"
            />

          </q-card-section>

          <q-card-actions align="right" class="text-primary">
            <q-btn flat label="Cancelar" v-close-popup />
            <q-btn flat label="Guardar" @click="updateUserPassword" v-close-popup />
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

<script>
  import constants from '../../statics/js/configuration'
  const md5        = require('md5');

  export default {
    name: 'Opciones',
    data () {
      return {
        color:"",
        model: null,
        calidad: "",
        val: 'media',
        test: '',
        submitting: false,
        tab: 'info',
        step: 1,
        accept: false,
        user: { },
        you: "",
        seamless: false,
        infoText: "",
        changePasswordModal: false,
        userPassword: {
          newPassword: "",
          oldPassword: ""
        },
        getUserData: async () => {
          let userId = localStorage.getItem("user");
          await this.$axios.get(constants.REST_API_URL + "/getUsuarioById/" + userId)
            .then(response => {
              this.user = response.data;
            });
        },
        updateUserData: () => {
          this.$axios.put(constants.REST_API_URL + "/updateUserData/" + this.you, this.user)
            .then(() => {
              this.seamless = true;
              this.infoText = "Información actualizada correctamente";
              setTimeout(() => this.seamless = false, 5000);
            })
            .catch(error => console.error("updateUserData, " + error))
        },
        updateUserPassword: () => {
          this.$axios.post(constants.REST_API_URL + "/updateUserPassword/" + this.you, {
            newPassword: md5(this.userPassword.newPassword),
            oldPassword: md5(this.userPassword.oldPassword)
          })
            .then(() => {
              this.seamless = true;
              this.infoText = "Contraseña actualizada correctamente";
              setTimeout(() => this.seamless = false, 5000);
            })
            .catch(error => {
              this.seamless = true;
              this.infoText = "Error al actualizar la contraseña";
              setTimeout(() => this.seamless = false, 5000);
              console.error("updateUserPassword, " + error)
            })
        }
      }
    },
    async beforeMount(){
      await this.getUserData();
      this.color = this.$tools.randomColor();
      this.you = window.localStorage.getItem("user");

    }
  }
</script>

<style scoped>

</style>
