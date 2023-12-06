<script setup>
import { ref, onMounted, inject } from "vue";
import { useRouter } from "vue-router";
import { listReview } from "@/api/review.js";

import VSelect from "@/components/common/VSelect.vue";
import ReviewListItem from "@/components/review/item/ReviewListItem.vue";
import VPageNavigation from "@/components/common/VPageNavigation.vue";

const router = useRouter();

//props로 VSelect로 내릴거임.
const selectOption = ref([
  { text: "검색조건", value: "" },
  { text: "글번호", value: "reviewNo" },
  { text: "제목", value: "subject" },
  { text: "작성자이메일", value: "email" },
]);
const reviews = ref([]);
const currentPage = inject("currentPage");
const totalPage = ref(0);
const { VITE_ARTICLE_LIST_SIZE } = import.meta.env;

const param = ref({
  pgno: currentPage.value,
  spp: VITE_ARTICLE_LIST_SIZE,
  key: "",
  word: "",
});

onMounted(() => {
  getReviewList();
});

//emit으로 날라오면 값 바꾸기
const changeKey = (val) => {
  param.value.key = val;
};

const moveWrite = () => {
  router.push({ name: "reviewSelect" });
};

//얘도 emit이라고 봐야하나? 페이지이동.
const onPageChange = (val) => {
  currentPage.value = val;
  param.value.pgno = val;

  getReviewList();
};

const getReviewList = () => {
  listReview(
    param.value,
    ({ data }) => {
      reviews.value = data.reviews;
      currentPage.value = data.currentPage;
      totalPage.value = data.totalPageCount;
    },
    ({ response }) => {
      alert(response.data);
    }
  );
};
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="sky">여행 후기 게시판</mark>
        </h2>
      </div>
      <div class="col-lg-10">
        <div class="row align-self-center mb-2">
          <div class="col-md-2 text-start">
            <button type="button" class="btn btn-outline-primary btn-sm" @click="moveWrite">
              후기 작성
            </button>
          </div>
          <div @submit.prevent="getReviewList" class="col-md-5 offset-5">
            <form class="d-flex">
              <VSelect :selectOption="selectOption" @onKeySelect="changeKey" />
              <div class="input-group input-group-sm ms-1">
                <input type="text" class="form-control" v-model="param.word" placeholder="후기 검색.." />
                <button class="btn btn-dark" type="button" @click="getReviewList">검색</button>
              </div>
            </form>
          </div>
        </div>
        <table class="table table-hover">
          <thead>
            <tr class="text-center">
              <th scope="col">글번호</th>
              <th scope="col">제목</th>
              <th scope="col">작성자</th>
              <th scope="col">여행지</th>
              <th scope="col">조회수</th>
              <th scope="col">작성일</th>
            </tr>
          </thead>
          <tbody>
            <ReviewListItem v-for="review in reviews" :key="review.reviewNo" :review="review"></ReviewListItem>
          </tbody>
        </table>
      </div>
      <VPageNavigation :currentPage="currentPage" :totalPage="totalPage" @pageChange="onPageChange"></VPageNavigation>
    </div>
  </div>
  <div>{{ totalPage }}</div>
</template>

<style scoped></style>
