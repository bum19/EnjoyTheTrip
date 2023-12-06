<script setup>
import { ref, onMounted, inject, toRaw } from "vue";
import { useRoute, useRouter } from "vue-router";
import { detailReview, deleteReview } from "@/api/review";
import $cookies from "vue-cookies";

const route = useRoute();
const router = useRouter();
const { reviewNo } = route.params;
const review = ref({});

onMounted(() => {
  getReviewDetail();
});

const getReviewDetail = () => {
  // const { reviewNo } = route.params;
  detailReview(
    reviewNo,
    ({ data }) => {
      review.value = data;
    },
    ({ response }) => {
      alert(response.data);
    }
  );
};

function moveList() {
  router.push({ name: "reviewList" });
}

function moveModify() {
  router.push({
    name: "reviewWrite",
    state: {
      review: { ...toRaw(review.value) },
      type: "modify",
    },
  });
}

function onDeleteReview() {
  const flag = confirm("정말 삭제하시겠습니까?");
  if (flag)
    deleteReview(
      reviewNo,
      (response) => {
        if (response.status == 200) {
          alert("글 삭제 성공!");
          moveList();
        }
      },
      ({ response }) => {
        alert(response.data);
      }
    );
}
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="sky">글보기</mark>
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <div class="row my-2">
          <h2 class="mb-3 text-center">여행지 : {{ review.attractionTitle }}</h2>
          <h2 class="text-secondary px-5">{{ review.reviewNo }}. {{ review.subject }}</h2>
        </div>
        <div class="row">
          <div class="col-md-8">
            <div class="clearfix align-content-center">
              <img class="avatar me-2 float-md-start bg-light p-2"
                src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg" />
              <p>
                <span class="fw-bold">작성자 : {{ review.email }} </span>
                <br />
                <span class="text-secondary fw-light">
                  {{ review.registerTime }} 조회 : {{ review.hit }}
                </span>
              </p>
            </div>
          </div>
          <!-- <div class="col-md-4 align-self-center text-end">댓글 : 17</div> -->
          <div class="divider mb-3"></div>
          <div class="text-secondary">
            {{ review.content }}
          </div>
          <div>점수 : {{ review.score }}</div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-outline-primary mb-3" @click="moveList">
              글목록
            </button>

            <button v-if="review.email === $cookies.get('email')" type="button" class="btn btn-outline-success mb-3 ms-1"
              @click="moveModify">
              글수정
            </button>
            <button v-if="review.email === $cookies.get('email')" type="button" class="btn btn-outline-danger mb-3 ms-1"
              @click="onDeleteReview">
              글삭제
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
