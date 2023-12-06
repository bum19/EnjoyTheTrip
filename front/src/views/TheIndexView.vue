<script setup>

</script>

<template>
  <div class="container">
		<img src="@/assets/img/sea.png" alt="sea" />
	</div>
</template>

<style scoped>
.container {
	width: 80%; /* 부모 요소의 80% 너비를 가짐 */
	margin: 0 auto; /* 가운데 정렬을 위한 마진 설정 */
	text-align: center; /* 내용 가운데 정렬 */
}

.container img {
	width: 100%; /* 이미지의 너비를 부모 요소의 100%로 설정하여 부모에 맞춤 */
	max-width: 100%; /* 이미지가 원본보다 큰 경우에도 부모 요소 너비를 넘지 않도록 설정 */
	height: auto; /* 이미지의 원래 비율을 유지 */
}
</style>