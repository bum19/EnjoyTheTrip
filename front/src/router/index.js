import { createRouter, createWebHistory } from "vue-router";
import TheIndexView from "@/views/TheIndexView.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "index",
      component: TheIndexView,
    },
    {
      path: "/review",
      name: "review",
      component: () => import("@/views/TheReviewView.vue"),
      redirect: { name: "reviewList" },
      children: [
        {
          path: "list",
          name: "reviewList",
          component: () => import("@/components/review/reviewList.vue"),
        },
        {
          path: "detail/:reviewNo",
          name: "reviewDetail",
          component: () => import("@/components/review/reviewDetail.vue"),
        },
        {
          path: "select",
          name: "reviewSelect",
          component: () => import("@/components/review/item/ReviewSelect.vue"),
        },
        {
          path: "write",
          name: "reviewWrite",
          component: () => import("@/components/review/ReviewWrite.vue"),
          props: (route) => ({
            selectAttraction: JSON.parse(route.query.selectAttraction || "{}"),
          }),
          // props: ["selectAttraction"],
        },
      ],
    },
    {
      path: "/travel",
      name: "travel",
      component: () => import("@/views/TheTravelView.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/member/TheLoginView.vue"),
    },
    {
      path: "/join",
      name: "join",
      component: () => import("@/views/member/TheJoinView.vue"),
    },
    {
      path: "/findPw",
      name: "findPw",
      component: () => import("@/views/member/TheFindPw.vue"),
    },
    {
      path: "/myPage",
      name: "myPage",
      component: () => import("@/views/member/TheMyPage.vue"),
    },
  ],
});

export default router;
