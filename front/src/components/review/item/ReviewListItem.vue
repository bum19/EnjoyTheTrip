<script setup>
defineProps({
  review: Object,
});
</script>

<template>
  <tr class="text-center">
    <th scope="col">{{ review.reviewNo }}</th>
    <td scope="col" class="text-center">
      <router-link :to="{ name: 'reviewDetail', params: { reviewNo: review.reviewNo } }" class="article-title link-dark">
        {{ review.subject }}
      </router-link>
    </td>
    <!-- <td scope="col">{{ review.subject }}</td> -->
    <td scope="col">{{ review.email }}</td>
    <td scope="col">{{ review.attractionTitle }}</td>
    <td scope="col">{{ review.hit }}</td>
    <td scope="col">{{ review.registerTime }}</td>
  </tr>
</template>

<style scoped></style>
