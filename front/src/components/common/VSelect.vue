<script setup>
import { ref, watch } from "vue";
const props = defineProps({ selectOption: Array });
const emit = defineEmits(["onKeySelect"]);

const i = ref("");
const onSelect = () => {
  emit("onKeySelect", i.value);
};

watch(
  () => props.selectOption.value,
  () => {
    i.value = "";
  },
  { deep: true }
);
</script>

<template>
  <select v-model="i" class="form-select form-select-sm w-50" @change="onSelect">
    <option v-for="option in selectOption" :key="option.value" :value="option.value"
      :disabled="option.value === '' ? true : false">
      {{ option.text }}
    </option>
  </select>
</template>

<style scoped></style>
