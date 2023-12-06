import { localAxios } from "@/utils/http-commons";

const local = localAxios;

function listSido(success, fail) {
  local.get(`/map/sido`).then(success).catch(fail);
}

function listGugun(param, success, fail) {
  local.get(`/map/gugun`, { params: param }).then(success).catch(fail);
}

function listAttraction(param, success, fail) {
  local.post(`/map/attractions`, param).then(success).catch(fail);
}

function detailAttraction(param, success, fail) {
  local.get(`/map/attractions/detail`, { params: param }).then(success).catch(fail);
}

export { listSido, listGugun, listAttraction, detailAttraction };
