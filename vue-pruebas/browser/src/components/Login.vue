<template>
  <div>
    <h1>Login</h1>
    <q-input v-model="text.user" stack-label="Usuario" placeholder="usuario" />
    <q-input v-model="text.password" float-label="Password" placeholder="password" />
    <q-btn color="primary" @click="">Submit</q-btn>
  </div>
</template>

<script>
  import { QBtn, QInput } from 'quasar'

  export default {
    data () {
      return {
        text: {
          user: '',
          password:''
        },
      }
    },
    components: {
      QBtn,
      QInput
    },
    name: 'login'
  }

</script>

<style scoped>

</style>
