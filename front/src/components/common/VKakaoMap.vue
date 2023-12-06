<script setup>
import { ref, watch, onMounted } from "vue";
const emit = defineEmits(["getAttractions", "changeSelectAttraction"]);

var map;
var clusterer;
const positions = ref([]);
const markers = ref([]); // 지도에 표시된 마커 객체를 가지고 있을 배열입니다
const props = defineProps({ attractions: Array, selectAttraction: Object }); //관광지정보

//지도뿌리기
onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
    emit("getAttractions");
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
      }&libraries=services,clusterer,drawing`;
    /* global kakao */
    script.onload = () =>
      kakao.maps.load(() => {
        initMap();
        emit("getAttractions");
      });
    document.head.appendChild(script);
  }
});

watch(
  () => props.attractions.value,
  () => {
    positions.value = [];
    props.attractions.forEach((attraction) => {
      let obj = {};
      obj = attraction;
      obj.latlng = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);

      positions.value.push(obj);
    });
    loadMarkers();
  },
  { deep: true }
);

watch(
  () => props.selectAttraction.value,
  () => {
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(
      props.selectAttraction.latitude,
      props.selectAttraction.longitude
    );

    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);
  },
  { deep: true } //배열안에있는 내용이 바뀔때에는 깊은 감시를 해야합니다.
);

// 카카오지도
const initMap = () => {
  const container = document.getElementById("map"); // 지도를 표시할 div
  const options = {
    center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
    level: 8, // 지도의 확대 레벨
  };
  // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
  map = new kakao.maps.Map(container, options);

  // 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
  var mapTypeControl = new kakao.maps.MapTypeControl();

  // 지도에 컨트롤을 추가해야 지도위에 표시됩니다
  // kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

  // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
  var zoomControl = new kakao.maps.ZoomControl();
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

  // 마커 클러스터러를 생성합니다
  clusterer = new kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
    minLevel: 8, // 클러스터 할 최소 지도 레벨
  });

  // loadMarkers();
};

const loadMarkers = () => {
  // 현재 표시되어있는 marker들이 있다면 map에 등록된 marker를 제거한다.
  deleteMarkers();
  clusterer.clear();

  // 마커를 생성합니다
  markers.value = [];
  positions.value.forEach((position) => {
    // 마커 이미지를 생성합니다
    var imgSrc = `/src/assets/img/marker${position.contentTypeId}.png`;
    // 마커 이미지의 이미지 크기 입니다
    const imgSize = new kakao.maps.Size(30, 30);
    const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);

    const marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: position.latlng, // 마커를 표시할 위치
      title: position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됨.
      clickable: true, // // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
      image: markerImage, // 마커의 이미지
    });

    // 생성된 마커를 배열에 추가합니다
    markers.value.push(marker);
    // 클러스터러에 마커들을 추가합니다
    clusterer.addMarker(marker);

    // 인포윈도우에 표시할 내용
    var content = `
  <div class="wrap">
    <div class="info">
      <div class="title">${position.title}</div>
      <div class="body">
        <div class="img">
          <img src="${position.firstImage || '/src/assets/img/noimage.png'}" width="73" height="70">
        </div>
        <div class="desc">
          <div class="ellipsis">${position.addr1}</div>
          <div class="jibun ellipsis">(우) ${position.zipcode} (지번) ${position.addr2}</div>
          <div class="ellipsis">${position.tel}</div>
        </div>
      </div>
    </div>
  </div>
`;

    // 마커에 표시할 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
      content: content, // 인포윈도우에 표시할 내용
    });

    // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
    // 이벤트 리스너로는 클로저를 만들어 등록합니다
    // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
    kakao.maps.event.addListener(marker, "mouseover", makeOverListener(map, marker, infowindow));
    kakao.maps.event.addListener(marker, "mouseout", makeOutListener(infowindow));

    // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
    function makeOverListener(map, marker, infowindow) {
      return function () {
        infowindow.open(map, marker);
      };
    }

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다
    function makeOutListener(infowindow) {
      return function () {
        infowindow.close();
      };
    }

    // 마커에 클릭이벤트를 등록합니다
    kakao.maps.event.addListener(marker, "click", function () {
      // 마커 위에 인포윈도우를 표시합니다
      emit("changeSelectAttraction", position);

      // 모달창 열기
      const modal = document.getElementById("open-modal");
      modal.click();
    });
  });

  // 4. 지도를 이동시켜주기
  // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
  const bounds = positions.value.reduce(
    (bounds, position) => bounds.extend(position.latlng),
    new kakao.maps.LatLngBounds()
  );
  if (positions.value.length == 0) {
    // LatLngBounds 객체에 디폴트 좌표를 추가합니다
    bounds.extend(new kakao.maps.LatLng(37.96440164, 124.6766833));
    bounds.extend(new kakao.maps.LatLng(33.32540095, 126.2551754));
    bounds.extend(new kakao.maps.LatLng(37.43565522, 129.1844784));
  }
  map.setBounds(bounds); //3레벨이아니라, 마커가 한화면에 다 나오게 지도조정
};

const deleteMarkers = () => {
  if (markers.value.length > 0) {
    markers.value.forEach((marker) => marker.setMap(null));
  }
};
</script>

<template>
  <div id="map"></div>
</template>

<style>
#map {
  width: 100%;
  height: 700px;
}

.wrap {
  text-align: left;
  overflow: hidden;
  font-size: 15px;
  font-family: "Malgun Gothic", dotum, "돋움", sans-serif;
  line-height: 1.5;
}

.wrap * {
  padding: 0;
  margin: 0;
}

.wrap .info {
  width: 286px;
  height: 120px;
  border-radius: 5px;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
}

.wrap .info:nth-child(1) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}

.info .title {
  padding: 5px 0 0 10px;
  height: 30px;
  background: #eee;
  border-bottom: 1px solid #ddd;
  font-size: 18px;
  font-weight: bold;
}

.info .close {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #888;
  width: 17px;
  height: 17px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png");
}

.info .close:hover {
  cursor: pointer;
}

.info .body {
  position: relative;
  overflow: hidden;
}

.info .desc {
  position: relative;
  margin: 13px 0 0 90px;
  height: 75px;
}

.desc .ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.desc .jibun {
  font-size: 13px;
  color: #888;
  margin-top: -2px;
}

.info .img {
  position: absolute;
  top: 6px;
  left: 5px;
  width: 73px;
  height: 71px;
  border: 1px solid #ddd;
  color: #888;
  overflow: hidden;
}

.info:after {
  content: "";
  position: absolute;
  margin-left: -12px;
  left: 50%;
  bottom: 0;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}

.info .link {
  color: #5085bb;
}
</style>
