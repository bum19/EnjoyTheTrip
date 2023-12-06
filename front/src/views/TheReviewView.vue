<script setup>
import { ref, provide } from "vue";
const currentPage = ref(1);
provide("currentPage", currentPage);
</script>

<template>
  <router-view></router-view>
</template>

<style scoped></style>

