import { localAxios } from "@/utils/http-commons";

const local = localAxios;

function listReview(param, success, fail) {
  local.get(`/review`, { params: param }).then(success).catch(fail);
}

function detailReview(reviewNo, success, fail) {
  local.get(`/review/${reviewNo}`).then(success).catch(fail);
}

function writeReview(body, success, fail) {
  local.post(`/review`, body).then(success).catch(fail);
}

function updateReview(param, body, success, fail) {
  local.post(`/review/${param}`, body).then(success).catch(fail);
}

function deleteReview(param, success, fail) {
  local.get(`review/delete/${param}`).then(success).catch(fail);
}

export { listReview, writeReview, detailReview, updateReview, deleteReview };
