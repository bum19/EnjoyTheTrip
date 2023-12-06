import { localAxios } from "@/utils/http-commons";

const local = localAxios;

function loginMember(param, success, fail) {
  local.post(`/user/login`, param).then(success).catch(fail);
}

function JWTlogin(body, success, fail) {
  local.post(`/user/jwtlogin`, body).then(success).catch(fail);
}

function logoutMember(success, fail) {
  local.get(`/user/logout`).then(success).catch(fail);
}

function joinMember(body, success, fail) {
  local.post(`/user/join`, body).then(success).catch(fail);
}

function emailCheck(param, success, fail) {
  local.get(`/user/emailCheck/${param}`).then(success).catch(fail);
}

function question(param, success, fail) {
  local.get(`/user/question`, { params: param }).then(success).catch(fail);
}

function updatePW(body, success, fail) {
  local.post(`/user/updatePW`, body).then(success).catch(fail);
}

function myPage(success, fail) {
  local.get(`/user/myPage`).then(success).catch(fail);
}

function updateMember(body, success, fail) {
  local.post(`/user/update`, body).then(success).catch(fail);
}

function deleteMember(success, fail) {
  local.get(`/user/delete`).then(success).catch(fail);
}

export {
  loginMember,
  logoutMember,
  JWTlogin,
  joinMember,
  emailCheck,
  question,
  updatePW,
  myPage,
  updateMember,
  deleteMember,
};
