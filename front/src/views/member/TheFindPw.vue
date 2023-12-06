<script setup>
//로그인하면못들어옴
//질문을 aes..? 어차피 답 검증은 해싱으로도가능.
import { ref, onMounted, watch } from "vue";
import { question, updatePW, joinMember, emailCheck } from "@/api/member";
import { useRouter } from "vue-router";

const isEmailValid = ref(false);
const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
const body = ref({
  email: "",
  question: "",
  answer: "",
  userPassword: "",
});
const emailCheckResult = ref("이메일는 6자 이상 50자 이하 입니다.");
const router = useRouter();

watch(
  () => body.value.email,
  () => {
    if (
      body.value.email.length < 6 ||
      body.value.email.length > 50 ||
      !emailRegex.test(body.value.email)
    ) {
      emailCheckResult.value = "이메일는 6자 이상 50자 이하 입니다. 양식도 준수해주세요";
    } else {
      emailCheckResult.value = null;
    }
  },
  { deep: true }
);

function getQuestion() {
  question(
    {
      email: body.value.email,
    },
    ({ data }) => {
      isEmailValid.value = true;
      body.value.question = data;
    },
    (err) => {
      emailCheckResult.value = "이메일이 존재하지 않습니다.";
    }
  );
}

function changePw() {
  updatePW(
    body.value,
    ({ data }) => {
      if (data) {
        alert("비밀번호 재설정 성공!");
        router.go(-1);
      } else {
        alert("답이달라유");
      }
    },
    ({ response }) => {
      alert("비밀번호 재설정 실패!");
      reset();
    }
  );
}

function reset() {
  body.value = {
    email: "",
    question: "",
    answer: "",
    userPassword: "",
  };
  isEmailValid.value = false;
}
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <h2 class="my-3 py-3 shadow-sm bg-light text-center">
        <mark class="orange">비밀번호찾기</mark>
      </h2>
    </div>
    <div class="col-lg-8 col-md-10 col-sm-12">
      <div class="mb-3">
        <div>이메일 입력</div>
        <input v-model="body.email" type="text" class="form-control" id="email" name="email" placeholder="email"
          :disabled="isEmailValid" />
        <div>{{ emailCheckResult }}</div>
        <button @click="getQuestion" :disabled="isEmailValid">질문 찾기</button>
      </div>
      <div v-if="isEmailValid">
        <div class="mb-3">
          <div>질문 :</div>
          <div id="question">{{ body.question }}</div>
        </div>
        <div class="mb-3">
          <div>답변 :</div>
          <input v-model="body.answer" type="text" class="form-control" id="answer" name="answer"
            placeholder="답을 입력하세요" />
        </div>
        <div class="mb-3">
          <div>재설정할 비밀번호 :</div>
          <input v-model="body.userPassword" type="password" class="form-control" id="userPassword" name="userPassword"
            placeholder="비밀번호..." />
        </div>
      </div>
      <div class="col-auto text-center">
        <button type="button" id="btn-join" class="btn btn-outline-primary mb-3" @click="changePw">
          비밀번호재설정하기
        </button>

        <button type="button" class="btn btn-outline-success mb-3" @click="reset">
          이메일 다시찾기
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
