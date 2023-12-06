<script setup>
import { watch, toRaw } from "vue";
import { useRouter } from "vue-router";
import { detailAttraction } from "@/api/map";

const router = useRouter();
const props = defineProps({ selectAttraction: Object }); //관광지정보

watch(
  () => props.selectAttraction.value,
  () => {
    detailAttraction(
      { contentId: props.selectAttraction.contentId },
      ({ data }) => {
        document.getElementById("title").innerText = `${props.selectAttraction.title}`;
        document.getElementById("body").innerHTML = `
        <div class="img">
          <img src="${props.selectAttraction.firstImage || "/src/assets/img/noimage.png"}" class="img-fluid">
        </div>
        <div style="font-size: 24px;"> 주소 : ${props.selectAttraction.addr1}</div>
        <hr>
        <div style="font-size: 24px;"> ${data}</div>
        `;
      },
      ({ response }) => {
        alert(response.data);
      }
    );
  },
  { deep: true } //배열안에있는 내용이 바뀔때에는 깊은 감시를 해야합니다.
);

function goToReviewWrite() {
  router.push({
    name: "reviewWrite",
    state: {
      selectAttraction: { ...toRaw(props.selectAttraction) },
      type: "regist",
    },
  });
  // 모달을 닫는 JavaScript 함수 호출
  document.querySelector(".btn-close").click();
}
</script>

<template>
  <!-- Button to Open the Modal -->
  <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#myModal" id="open-modal"
    hidden></button>

  <!-- The Modal -->
  <div class="modal" id="myModal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <!-- Modal Header -->
        <div class="modal-header">
          <h3 class="modal-title" id="title">제목</h3>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>

        <!-- Modal body -->
        <div class="modal-body" id="body">내용</div>

        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" @click="goToReviewWrite">
            리뷰 작성하기
          </button>
          <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
