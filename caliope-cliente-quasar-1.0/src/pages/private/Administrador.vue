<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <q-page class="flex column" :style="color">
    <h2 class="text-align-center">Panel de control de administrador</h2>
    <h3 class="text-align-center">{{user.username}}</h3>
    <div class="q-pa-md">
      <div class="q-gutter-y-md">
        <q-card>
          <q-tabs
            v-model="tab"
            dense
            class="bg-grey-3"
            align="justify"
            narrow-indicator
          >
            <q-tab name="users" label="Usuarios" />
            <q-tab name="songs" label="Canciones" />
          </q-tabs>

          <q-separator />

          <q-tab-panels v-model="tab" animated>
            <q-tab-panel name="users">
              <div>

                <q-table
                  title="Lista de canciones"
                  :data="user"
                  :columns="columnsUser"
                  row-key="username"
                  selection="single"
                  :selected.sync="selectedUser"
                  :filter="filterUser"
                  :loading="loadingUser"
                >

                  <template v-slot:top>
                    <q-btn class="on-right" flat dense color="primary" :disable="loading" icon="autorenew" label="Actualizar" @click="refreshData" ></q-btn>
                    <q-btn class="on-right" flat dense color="primary" :disable="loading" icon="delete" label="Eliminar" @click="removeUserExtra" ></q-btn>
                    <q-space ></q-space>
                    <q-input borderless dense debounce="300" color="primary" v-model="filter">
                      <template v-slot:append>
                        <q-icon name="search" ></q-icon>
                      </template>
                    </q-input>
                  </template>

                </q-table>

              </div>
            </q-tab-panel>

            <q-tab-panel name="songs">
              <div>

                <q-table
                  title="Lista de canciones"
                  :data="mySongs"
                  :columns="columns"
                  row-key="id"
                  selection="single"
                  :selected.sync="selected"
                  :filter="filter"
                  :loading="loading"
                >

                  <template v-slot:top>
                    <q-btn class="on-right" flat dense color="primary" :disable="loading" icon="autorenew" label="Actualizar" @click="refreshData" ></q-btn>
                    <q-btn class="on-right" flat dense color="primary" :disable="loading" icon="delete" label="Eliminar" @click="removeSongExtra" ></q-btn>
                    <q-space ></q-space>
                    <q-input borderless dense debounce="300" color="primary" v-model="filter">
                      <template v-slot:append>
                        <q-icon name="search" ></q-icon>
                      </template>
                    </q-input>
                  </template>

                </q-table>

              </div>
            </q-tab-panel>
          </q-tab-panels>

        </q-card>
      </div>
    </div>

    <!--  Cancion no seleccionada  -->
    <q-dialog v-model="noSongSelected">
      <q-card>
        <q-card-section>
          <div class="text-h6">Información</div>
        </q-card-section>

        <q-card-section>
          No has seleccionado ningúna canción
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Aceptar" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!--  Usuario no seleccionado  -->
    <q-dialog v-model="noUserSelected">
      <q-card>
        <q-card-section>
          <div class="text-h6">Información</div>
        </q-card-section>

        <q-card-section>
          No has seleccionado a ningún usuario
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Aceptar" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!--  Esta seguro dialog  -->
    <q-dialog v-model="areYouSure">
      <q-card>
        <q-card-section>
          <div class="text-h6">Alerta</div>
        </q-card-section>

        <q-card-section>
          ¿Está seguro de que sea eliminar la canción?
        </q-card-section>

        <q-card-actions align="center">
          <q-btn flat label="Sí" color="primary" @click="removeSong" v-close-popup />
          <q-btn flat label="No" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!--  Esta seguro user dialog  -->
    <q-dialog v-model="areYouSureUser">
      <q-card>
        <q-card-section>
          <div class="text-h6">Alerta</div>
        </q-card-section>

        <q-card-section>
          ¿Está seguro de que sea eliminar al usuario?
        </q-card-section>

        <q-card-actions align="center">
          <q-btn flat label="Sí" color="primary" @click="removeUser" v-close-popup />
          <q-btn flat label="No" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

  </q-page>
</template>

<script>
  import constants from '../../statics/js/configuration'
  import moment from 'moment'

  export default {
    name: 'Administrador',
    data () {
      return {
        /* MODALS */
        noSongSelected: false,
        noUserSelected: false,
        areYouSure: false,
        areYouSureUser: false,
        /* CANCIONES TABLE */
        selected: [],
        columns: [
          { name: 'nombre', field: "nombre", label: 'Nombre', align: 'center', sortable: true },
          { name: 'autor', field: row => row.autor.username, label: 'Autor', align: 'center', sortable: true },
          { name: 'reproducciones', field: 'reproducciones', label: 'Reproducciones', align: 'center', sortable: true },
          { name: 'genero', field: row => row.genero.nombre, label: 'Genero', align: 'center', sortable: true },
          { name: 'fechaRegistro', field: 'fechaRegistro', label: 'Fecha registro', align: 'center', sortable: true }
        ],
        loading: false,
        filter: '',
        rowCount: 10,
        /* USERS TABLE */
        selectedUser: [],
        columnsUser: [
          { name: 'username', field: "username", label: 'Nombre de usuario', align: 'center', sortable: true },
          { name: 'email', field: "email", label: 'Email', align: 'center', sortable: true },
          { name: 'fullName', field: row => row.nombre + ", " + row.apellidos, label: 'Nombre completo', align: 'center', sortable: true },
          { name: 'permiso', field: row => {
            if (row.permiso === 0) return "Baneado";
            else if (row.permiso === 1) return "Normal";
            else if (row.permiso === 2) return "Admin";
            }, label: 'permiso', align: 'center', sortable: true },
          { name: 'numeroSeguidores', field: 'numeroSeguidores', label: 'Seguidores', align: 'center', sortable: true },
          { name: 'numeroSeguidos', field: 'numeroSeguidos', label: 'Seguidos', align: 'center', sortable: true },
          { name: 'fechaRegistro', field: 'fechaRegistro', label: 'Fecha de registro', align: 'center', sortable: true },

        ],
        loadingUser: false,
        filterUser: '',
        color:"",
        /* Borrar cancion */
        removeSongExtra: () => {
          if (this.selected[0] === undefined) {
            this.noSongSelected = true;
          } else {
            this.areYouSure = true;

          }
        },
        removeSong: () => {
          this.$axios.put(constants.REST_API_URL + "/deleteSongById/" + this.selected[0].id)
            .catch(error => console.error(error));
        },
        /* Borrar usuario */
        removeUserExtra: () => {
          if (this.selectedUser[0] === undefined) {
            this.noUserSelected = true;
          } else {
            this.areYouSureUser = true;

          }
        },
        removeUser: () => {
          this.$axios.put(constants.REST_API_URL + "/deleteUserByUsername/" + this.selectedUser[0].username)
            .catch(error => console.error(error));
        },
        mySongs: [],

        user: [],
        tab: 'users',
        test: () => console.log(this.mySongs),
        getUserSongs: async () => {
          await this.$axios.get(constants.REST_API_URL + "/getCanciones/")
            .then(response => {
              this.mySongs = response.data;
              for (let item of this.mySongs) {
                item.fechaRegistro = moment(item.fechaRegistro).format(constants.DATE_FORMAT);
              }
            })
            .catch(error => console.error(error))
        },
        getUserData: async () => {
        await this.$axios.get(constants.REST_API_URL + "/getUsuarios/")
          .then(response => {
            this.user = response.data;
            for (let item of this.user) {
              item.fechaRegistro = moment(item.fechaRegistro).format(constants.DATE_FORMAT);
            }
          });
        },
        refreshData: () => {
          this.getUserSongs();
          this.getUserData();
        }
      }
    },
  async beforeMount(){
    await this.getUserData();
    await this.getUserSongs();
    this.color = this.$tools.randomColor();
  }
  }
</script>

<style scoped>

</style>
