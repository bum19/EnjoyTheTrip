<script setup>
import { ref, onMounted, watch } from "vue";
import { joinMember, emailCheck } from "@/api/member";
import { useRouter } from "vue-router";
const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
const checkPassword = ref("");
const body = ref({
  email: "",
  userName: "",
  userPassword: "",
  question: "",
  answer: "",
});
const emailCheckResult = ref("이메일는 6자 이상 50자 이하 입니다.");
const router = useRouter();
const isButtonEnabled = ref(false); // 동적으로 버튼 활성화 여부를 조절할 데이터

watch(
  () => body.value.email,
  () => {
    if (body.value.email.length < 6 || body.value.email.length > 50) {
      emailCheckResult.value = "이메일는 6자 이상 50자 이하 입니다.";
    } else {
      emailCheck(
        body.value.email,
        ({ data }) => {
          if (data == 0 && emailRegex.test(body.value.email)) {
            emailCheckResult.value = "사용가능한 아이디입니다!";
            isButtonEnabled.value = true;
          } else {
            emailCheckResult.value = "아이디가 중복이거나 양식에 맞지않습니다.";
            isButtonEnabled.value = false;
          }
        },
      );
    }
  },
  { deep: true }
);

function joinRequest() {
  if (checkPassword.value != body.value.userPassword) {
    alert("비밀번호가 일치하지않습니다.");
  } else {
    if (body.value.email == "" || body.value.email == null) {
      alert("이메일 입력!!");
      return;
    } else if (body.value.userName == "" || body.value.userName == null) {
      alert("이름 입력!!");
      return;
    } else if (body.value.userPassword == "" || body.value.userPassword == null) {
      alert("비밀번호 입력!!");
      return;
    } else if (body.value.question == "" || body.value.question == null) {
      alert("질문 입력!!");
      return;
    } else if (body.value.answer == "" || body.value.answer == null) {
      alert("답변 입력!!");
      return;
    } else {
      joinMember(
        body.value,
        () => {
          //회원가입 성공시
          alert("회원가입 성공!");
          router.push({ name: "index" });
        },
        ({ response }) => {
          alert(response.data);
        }
      );
    }
  }
}

function reset() {
  body.value = {
    email: "",
    userName: "",
    userPassword: "",
    question: "",
    answer: "",
  };
  checkPassword.value = "";
}
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <h2 class="my-3 py-3 shadow-sm bg-light text-center">
        <mark class="orange">회원가입</mark>
      </h2>
    </div>
    <div @submit.prevent="joinRequest" class="col-lg-8 col-md-10 col-sm-12">
      <form id="form-join">
        <div class="mb-3">
          <label for="user_name" class="form-label">이름 : </label>
          <input v-model="body.userName" type="text" class="form-control" id="user_name" name="user_name"
            placeholder="이름..." />
        </div>
        <div class="mb-3">
          <label for="email" class="form-label">이메일 : </label>
          <input v-model="body.email" type="text" class="form-control" id="email" name="email" placeholder="이메일..." />
        </div>
        <div class="mb-3 text-dark">{{ emailCheckResult }}</div>
        <div class="mb-3">
          <label for="user_password" class="form-label">비밀번호 : </label>
          <input v-model="body.userPassword" type="password" class="form-control" id="user_password" name="user_password"
            placeholder="비밀번호..." />
        </div>
        <div class="mb-3">
          <label for="pwdcheck" class="form-label">비밀번호확인 : </label>
          <input v-model="checkPassword" type="password" class="form-control" id="pwdcheck" placeholder="비밀번호확인..." />
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
          <button type="button" id="btn-join" class="btn btn-outline-primary mb-3"
            :class="{ 'disabled': !isButtonEnabled }" :disabled="!isButtonEnabled" @click="joinRequest">
            회원가입
          </button>
          <button type="button" class="btn btn-outline-success mb-3" @click="reset">초기화</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
