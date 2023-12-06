<script setup>
import { ref, provide } from "vue";
import { RouterLink, RouterView } from "vue-router";
import Navbar from "./components/layout/TheNavbarView.vue";

const hasUserNameCookie = ref(false);
provide("hasUserNameCookie", hasUserNameCookie);

// 로그인 후 리다이렉트할 때 사용할 변수
const redirectFromLogin = ref(null);
provide("redirectFromLogin", redirectFromLogin);
</script>

<template>
  <Navbar></Navbar>
  <RouterView></RouterView>
</template>

<style scoped></style>
