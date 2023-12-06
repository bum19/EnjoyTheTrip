<script setup>
import { ref, onMounted } from "vue";
import $cookies from "vue-cookies";
import { updateMember, myPage, deleteMember } from "@/api/member";
import { useRouter } from "vue-router";
import { setCookie } from "@/assets/js/cookie";

const router = useRouter();

const body = ref({
  userName: "",
  question: "",
  answer: "",
});

function update() {
  updateMember(
    body.value,
    () => {
      alert("수정완료!");
      setCookie("userName", body.value.userName);
      location.reload();
    },
    ({ response }) => {
      alert(response.data);
    }
  );
}

function requestDelete() {
  const check = confirm("정말 탈퇴하시겠습니까?");
  if (check) {
    deleteMember(() => {
      alert("삭제완료.");
      $cookies.remove("userName");
      $cookies.remove("email");
      router.push({ name: "index" });
    }),
      ({ response }) => {
        alert(response.data);
      };
  }
}

onMounted(() => {
  myPage(({ data }) => {
    body.value.userName = data.userName;
    body.value.question = data.question;
    body.value.answer = data.answer;
  });
});
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <h2 class="my-3 py-3 shadow-sm bg-light text-center">
        <mark class="orange">내정보 페이지</mark>
      </h2>
    </div>

    <div @submit.prevent="joinRequest" class="col-lg-8 col-md-10 col-sm-12">
      <div>이메일 : {{ $cookies.get("email") }}</div>
      <form id="form-join">
        <div class="mb-3">
          <label for="user_name" class="form-label">이름 : </label>
          <input v-model="body.userName" type="text" class="form-control" id="user_name" name="user_name"
            placeholder="이름..." />
        </div>
        <div class="mb-3">
          <label for="question" class="form-label">비밀번호 찾기 질문 : </label>
          <input v-model="body.question" type="text" class="form-control" id="question" name="question"
            placeholder="비밀번호..." />
        </div>
        <div class="mb-3">
          <label for="answer" class="form-label">비밀번호 찾기 답변 : </label>
          <input v-model="body.answer" type="text" class="form-control" id="answer" name="answer" placeholder="비밀번호..." />
        </div>
        <div class="col-auto text-center">
          <button type="button" id="btn-join" class="btn btn-outline-primary mb-3" @click="update">
            내정보 수정하기
          </button>
          <button type="button" class="btn btn-outline-success mb-3" @click="requestDelete">
            탈퇴
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
