<script setup>
import { ref, inject } from "vue";
import { useRouter } from "vue-router";
import $cookies from "vue-cookies";
import { writeReview, updateReview } from "@/api/review";
const router = useRouter();

const type = history.state.type; //regist or modify 값 넘겨받기. 반응형은 아님.
const selectAttraction = ref({});
const review = ref({});
const currentPage = inject("currentPage");

if (type === "regist") {
  selectAttraction.value = history.state.selectAttraction;
} else if (type === "modify") {
  review.value = history.state.review;
}

const body = ref({});
if (type === "modify") {
  body.value.email = review.value.email;
  body.value.subject = review.value.subject;
  body.value.content = review.value.content;
  body.value.contentId = review.value.contentId;
  body.value.attractionTitle = review.value.attractionTitle;
  body.value.score = review.value.score;
} else {
  body.value = {
    email: "",
    subject: "",
    content: "",
    contentId: 0,
    attractionTitle: "",
    score: 1,
  };
}
const onSubmit = () => {
  //프론트단에서 유효성검사.
  if (body.value.score < 0 || body.value.score > 5) {
    alert("점수는 0점이상 5점 이하여야 합니다.");
    return;
  }
  if (body.value.subject == "" || body.value.subject == null) {
    alert("제목을 입력해주세요");
    return;
  }
  if (body.value.content == "" || body.value.content == null) {
    alert("내용을 입력해주세요");
    return;
  }

  if (type === "regist") {
    body.value.contentId = selectAttraction.value.contentId;
    body.value.attractionTitle = selectAttraction.value.title;
    body.value.email = $cookies.get("email");
    writeReview(
      body.value,
      ({ data }) => {
        alert("글작성 성공!");
        currentPage.value = 1;
        moveList();
      },
      ({ response }) => {
        alert("글작성 중 에러 발생!");
        moveList();
      }
    );
  } else if (type === "modify") {
    updateReview(
      review.value.reviewNo,
      body.value,
      ({ data }) => {
        alert("글수정 완료!");
        router.go(-1);
      },
      ({ response }) => {
        alert("글수정 중 에러발생");
        router.go(-1);
      }
    );
  }
};

const moveList = () => {
  router.push({ name: "reviewList" });
};
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-10">
      <h2 class="my-3 py-3 shadow-sm bg-light text-center">
        <mark class="sky">후기 쓰기 </mark>
      </h2>
    </div>
    <form @submit.prevent="onSubmit">
      <h2 v-if="type === 'regist'" class="mb-3 text-center">
        여행지 : {{ selectAttraction.title }}
      </h2>
      <h2 v-else class="mb-3 text-center">여행지 : {{ review.attractionTitle }}</h2>
      <div class="mb-3">
        <label for="subject" class="form-label">제목 : </label>
        <input type="text" class="form-control" v-model="body.subject" placeholder="제목..." />
      </div>
      <div class="mb-3">
        <label for="content" class="form-label">내용 : </label>
        <textarea class="form-control" v-model="body.content" rows="10"></textarea>
      </div>
      <div class="mb-3">
        <label for="content" class="form-label">점수 : </label>
        <input type="number" class="form-control-sm" v-model="body.score" min="1" max="5" />
      </div>
      <div class="col-auto text-center">
        <button type="submit" class="btn btn-outline-primary mb-3" v-if="type === 'regist'">
          글작성
        </button>
        <button type="submit" class="btn btn-outline-success mb-3" v-else>글수정</button>
        <button type="button" class="btn btn-outline-danger mb-3 ms-1" @click="moveList">
          목록으로이동...
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped></style>
