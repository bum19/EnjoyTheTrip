<script setup>
import { ref, onMounted } from "vue";
import ReviewKakaoMap from "@/components/review/item/ReviewKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";
import { listSido, listGugun, listAttraction } from "@/api/map";

const attractions = ref([]);
const selectAttraction = ref({});
const sidoList = ref([]);
const gugunList = ref([{ text: "구군선택", value: "" }]);
const selectOption = ref([
  { text: "검색조건", value: "" },
  { text: "제목", value: "title" },
  { text: "주소", value: "addr1" },
]);
const categories = [
  { label: "관광지", value: 12 },
  { label: "문화시설", value: 14 },
  { label: "행사/공연/축제", value: 15 },
  { label: "여행코스", value: 25 },
  { label: "레포츠", value: 28 },
  { label: "숙박", value: 32 },
  { label: "쇼핑", value: 38 },
  { label: "음식점", value: 39 },
];

const param = ref({
  sidoCode: "",
  gugunCode: "",
  contentTypeId: [],
  key: "",
  word: "",
});

onMounted(() => {
  getSidoList();
});

const getSidoList = () => {
  listSido(
    ({ data }) => {
      let options = [];
      options.push({ text: "시도선택", value: "" });
      data.forEach((sido) => {
        options.push({ text: sido.sidoName, value: sido.sidoCode });
      });
      sidoList.value = options;
    },
    ({ response }) => {
      alert(response.data);
    }
  );
};

const onChangeSido = (val) => {
  //초기화
  param.value.key = "";
  param.value.word = "";
  param.value.gugunCode = "";
  selectOption.value = [
    { text: "검색조건", value: "" },
    { text: "제목", value: "title" },
    { text: "주소", value: "addr1" },
  ];
  //초기화 끝
  param.value.sidoCode = val;
  listGugun(
    { sido: val },
    ({ data }) => {
      let options = [];
      options.push({ text: "구군선택", value: "" });
      data.forEach((gugun) => {
        options.push({ text: gugun.gugunName, value: gugun.gugunCode });
      });
      gugunList.value = options;
    },
    ({ response }) => {
      alert(response.data);
    }
  );
  getAttractions();
};

const onChangeGugun = (val) => {
  //구군 하위 검색코드(체크박스 제외) 초기화
  param.value.key = "";
  param.value.word = "";
  param.value.gugunCode = val;
  selectOption.value = [
    { text: "검색조건", value: "" },
    { text: "제목", value: "title" },
    { text: "주소", value: "addr1" },
  ];
  getAttractions();
};

const onChangeOption = (val) => {
  param.value.key = val;
};

const onChangeContent = () => {
  getAttractions();
};

function getAttractions() {
  listAttraction(
    {
      sidoCode: param.value.sidoCode,
      gugunCode: param.value.gugunCode,
      contentTypeId: param.value.contentTypeId.join(","), // 배열을 문자열로 변환
      key: param.value.key,
      word: param.value.word,
    },
    ({ data }) => {
      attractions.value = data;
      if (data.length == 0 && param.value.word != "") {
        //맵 깨짐 수정해야함.
        //key랑 word만 초기화
        alert("검색 똑바로 해 이놈아!! 네녀석 검색에 해당하는 여행지따위는 존재하지 않는다구?");
        param.value.word = "";
        getAttractions();
      }
    },
    ({ response }) => {
      alert(response.data);
    }
  );
}

const changeSelectAttraction = (attraction) => {
  selectAttraction.value = attraction;
};
</script>

<template>
  <!-- 중앙 컨텐츠 시작 -->
  <div class="col-md-8 text-center mx-auto">
    <h1>지역별 관광정보</h1>
    <p><span class="placeholder col-4"></span></p>
    <!-- 관광지 검색 start -->
    <form class="d-flex my-3" onsubmit="return false;" role="search">
      <VSelect :selectOption="sidoList" @onKeySelect="onChangeSido" />
      <VSelect :selectOption="gugunList" @onKeySelect="onChangeGugun" />
      <VSelect :selectOption="selectOption" @onKeySelect="onChangeOption" />
      <input type="text" v-model="param.word" placeholder="검색어를 입력하세요.." />
      <button @click="getAttractions">검색</button>
    </form>
    <div class="d-flex justify-content-between my-3">
      <div v-for="category in categories" :key="category.value">
        <input type="checkbox" :id="`chk${category.label.replace(/\s/g, '')}`" :value="category.value"
          @change="onChangeContent" v-model="param.contentTypeId" />
        <label :for="`chk${category.label.replace(/\s/g, '')}`">{{ category.label }}</label>
      </div>
    </div>
    <!-- kakao map start -->
    <ReviewKakaoMap :attractions="attractions" :selectAttraction="selectAttraction" @getAttractions="getAttractions"
      @changeSelectAttraction="changeSelectAttraction">
    </ReviewKakaoMap>
    <div id="map" class="mt-3" style="width: 100%; height: 500px"></div>
    <!-- kakao map end -->
    <div class="row">
      <table class="table table-striped" style="display: none">
        <tbody id="trip-list"></tbody>
      </table>
    </div>
    <!-- 관광지 검색 end -->
  </div>
  <!-- 중앙 content end -->
</template>

<style scoped></style>
