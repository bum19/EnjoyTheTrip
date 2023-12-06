<script setup>
import { ref, onMounted, inject } from "vue";
import { loginMember } from "@/api/member";
import { useRouter } from "vue-router";
import $cookies from "vue-cookies";
import { getCookie, setCookie, deleteCookie } from "@/assets/js/cookie";

const redirectFromLogin = inject("redirectFromLogin");
const router = useRouter();
const param = ref({
  email: "",
  userPassword: "",
  saveid: false,
});

// 로그인 성공 후 실행되는 코드
// 로그인 페이지에서 로그인 성공 시 redirectFromLogin이 설정되어 있다면 해당 페이지로 이동
function handleLoginSuccess() {
  if (redirectFromLogin.value) {
    router.push({
      name: redirectFromLogin.value,
      state: {
        selectAttraction: history.state.selectAttraction,
        type: "regist",
      },
    });
    redirectFromLogin.value = null; // 리다이렉트 후 변수 초기화
  } else {
    router.go(-1);
  }
}

function login() {
  if (param.value.email == "" || param.value.email == null) {
    alert("이메일 입력!!");
    return;
  }
  else if (param.value.userPassword == "" || param.value.userPassword == null) {
    alert("비밀번호 입력!!");
    return;
  } else {
    loginMember(
      param.value,
      ({ data }) => {
        setCookie("userName", data.userName);
        setCookie("email", data.email);

        // 이메일 저장하기
        if (param.value.saveid == true) {
          setCookie("saveEmail", data.email);
        } else {
          deleteCookie("saveEmail");
        }
        handleLoginSuccess();
      },
      ({ response }) => {
        alert(response.data);
        param.value.email = $cookies.get("saveEmail");
        param.value.userPassword = "";
      }
    );
  }
}

onMounted(() => {
  document.querySelector("#btn-mv-join").addEventListener("click", function () {
    location.href = "${root}/user/join";
  });

  const saveEmail = $cookies.get("saveEmail");
  if (saveEmail) {
    param.value.email = saveEmail;
    param.value.saveid = true;
  }
});
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
      <h2 class="my-3 py-3 shadow-sm bg-light text-center">
        <mark class="orange">로그인</mark>
      </h2>
    </div>
    <div class="col-lg-8 col-md-10 col-sm-12">
      <form id="form-login" @submit.prevent="login">
        <div class="form-check mb-3 float-end">
          <input class="form-check-input" type="checkbox" v-model="param.saveid" param.saveid />
          <label class="form-check-label" for="saveid"> 이메일저장 </label>
        </div>
        <div class="mb-3">
          <label for="email" class="form-label">이메일 : </label>
          <input type="text" class="form-control" id="email" placeholder="이메일..." v-model="param.email" />
        </div>
        <div class="mb-3">
          <label for="userPassword" class="form-label">비밀번호 : </label>
          <input type="password" class="form-control" id="userPassword" placeholder="비밀번호..."
            v-model="param.userPassword" />
        </div>
        <div class="col-auto text-center">
          <button type="submit" id="btn-login" class="btn btn-outline-primary mb-3">로그인</button>
          <button type="button" id="btn-mv-join" class="btn btn-outline-success mb-3">
            회원가입
          </button>
          <!-- <a
            href="https://kauth.kakao.com/oauth/authorize?client_id=90a73a1cf1fc7f7fd1013e54e96747a7&redirect_uri=http://localhost/user/kakaoLogin&response_type=code">
            <img src="@/assets/img/kakao_login_large.png" alt="" style="width: 50px; height: auto" />
          </a> -->
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
