<template>
  <div>
    <h1>password</h1>
    <q-input v-model="text.email" type="email" suffix="@gmail.com" stack-label="Email" />
    <q-btn color="primary" @click="">Submit</q-btn>
  </div>

</template>

<script>
  import { QBtn, QInput } from 'quasar'

  export default {
    data () {
      return {
        text: {
          email: ''
        },
      }
    },
    components: {
      QBtn,
      QInput
    },
    name: 'password'
  }
</script>

<style scoped>

</style>
