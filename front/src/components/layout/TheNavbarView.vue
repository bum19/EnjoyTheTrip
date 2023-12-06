<script setup>
import { ref, inject } from "vue";
import $cookies from "vue-cookies";
import { logoutMember } from "@/api/member";
import { useRouter } from "vue-router";

const router = useRouter();
const hasUserNameCookie = inject("hasUserNameCookie");
const redirectFromLogin = inject("redirectFromLogin");


router.beforeEach((to, from, next) => {
  // 네비게이션 이전에 쿠키를 확인하고 필요한 동작을 수행
  hasUserNameCookie.value = $cookies.isKey("userName");

  if (["reviewSelect", "reviewWrite"].includes(to.name) && !hasUserNameCookie.value) {
    // 로그인이 필요한 페이지에 접근하려고 할 때
    // 현재 페이지 정보 저장하고 로그인 페이지로 이동
    redirectFromLogin.value = to.name;
    next({ name: 'login' });
  } else {
    // 그 외의 경우에는 계속 진행
    next();
  }
});

const logout = () => {
  // 로그아웃 처리 로직
  logoutMember();
  $cookies.remove("userName");
  $cookies.remove("email");

  // 현재 라우트 정보 가져오기
  const currentRoute = router.currentRoute.value;

  // 만약 현재 위치가 홈이라면 새로고침
  if (currentRoute.name === "index") {
    window.location.reload();
  } else {
    // 아니면 홈으로 이동
    router.push({ name: "index" });
  }
};
</script>

<template>
  <header class="header__layout">
    <div class="header">
      <div class="header__left">
        <nav id="navbar" class="navbar">
          <ul>
            <li>
              <router-link :to="{ name: 'index' }"
                ><img id="logo" src="@/assets/img/logo.png" alt="logo"
              /></router-link>
            </li>
            <li>
              <router-link class="nav-link scrollto" :to="{ name: 'travel' }"
                >지역별여행지</router-link
              >
            </li>
            <li>
              <router-link class="nav-link scrollto" :to="{ name: 'review' }"
                >리뷰게시판</router-link
              >
            </li>
          </ul>
          <i class="bi bi-list mobile-nav-toggle"></i>
        </nav>
      </div>

      <div class="header__right">
        <!-- "userName" 쿠키가 있을 때 -->
        <template v-if="hasUserNameCookie">
          <img
            id="profile"
            src="@/assets/img/profile.png"
            alt="profile"
            style="width: 50px; height: auto"
          />

          <a id="useridnim" disabled>{{ $cookies.get("userName") }}님 환영합니다.</a>|
          <!-- <router-link :to="{ name: 'todos' }" class="a_link">오늘 할일</router-link>| -->
          <router-link :to="{ name: 'myPage' }">회원정보</router-link>|
          <button @click="logout" id="logoutButton">로그아웃</button>
        </template>
        <!-- "userName" 쿠키가 없을 때 -->
        <template v-else>
          <router-link :to="{ name: 'login' }">로그인</router-link>
          <router-link :to="{ name: 'join' }">회원가입</router-link>
          <router-link :to="{ name: 'findPw' }">비밀번호찾기</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped></style>
