import {serviceKey} from "./key.js"

    // 선택하면..
    // 지역, 유형, 검색어 얻기.
    // 위 데이터를 가지고 공공데이터에 요청.
    // 받은 데이터를 이용하여 화면 구성.
    document.getElementById("search-area").addEventListener("change", () => {
        let baseUrl = `https://apis.data.go.kr/B551011/KorService1/areaBasedList1`;

        let queryString = `serviceKey=${serviceKey}&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A`;
        let areaCode = document.getElementById("search-area").value;
        let sigunguCode = document.getElementById("search-sigungu").value;
        let contentTypeId = document.getElementById("search-content-id").value;

        if (parseInt(areaCode)) queryString += `&areaCode=${areaCode}`;
        if (parseInt(sigunguCode)) queryString += `&sigunguCode=${sigunguCode}`;
        if (parseInt(contentTypeId)) queryString += `&contentTypeId=${contentTypeId}`;

        let searchUrl = baseUrl + "?" + queryString;

        fetch(searchUrl)
            .then((response) => response.json())
            .then((data) => makeList(data));
    });

    document.getElementById("search-sigungu").addEventListener("change", () => {
        let baseUrl = `https://apis.data.go.kr/B551011/KorService1/areaBasedList1`;

        let queryString = `serviceKey=${serviceKey}&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A`;
        let areaCode = document.getElementById("search-area").value;
        let sigunguCode = document.getElementById("search-sigungu").value;
        let contentTypeId = document.getElementById("search-content-id").value;

        if (parseInt(areaCode)) queryString += `&areaCode=${areaCode}`;
        if (parseInt(sigunguCode)) queryString += `&sigunguCode=${sigunguCode}`;
        if (parseInt(contentTypeId)) queryString += `&contentTypeId=${contentTypeId}`;

        let searchUrl = baseUrl + "?" + queryString;

        fetch(searchUrl)
            .then((response) => response.json())
            .then((data) => makeList(data));
    });

    document.getElementById("search-content-id").addEventListener("change", () => {
        let baseUrl = `https://apis.data.go.kr/B551011/KorService1/areaBasedList1`;

        let queryString = `serviceKey=${serviceKey}&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A`;
        let areaCode = document.getElementById("search-area").value;
        let sigunguCode = document.getElementById("search-sigungu").value;
        let contentTypeId = document.getElementById("search-content-id").value;

        if (parseInt(areaCode)) queryString += `&areaCode=${areaCode}`;
        if (parseInt(sigunguCode)) queryString += `&sigunguCode=${sigunguCode}`;
        if (parseInt(contentTypeId)) queryString += `&contentTypeId=${contentTypeId}`;

        let searchUrl = baseUrl + "?" + queryString;

        fetch(searchUrl)
            .then((response) => response.json())
            .then((data) => makeList(data));
    });

    var positions; // marker 배열.
    var tripList;
    function makeList(data) {
        document.querySelector("table").setAttribute("style", "display: ;");
        let trips = data.response.body.items.item;
        tripList = [];
        positions = [];
        trips.forEach((area) => {
            tripList.push(`
            <nav class="navbar navbar-expand-lg navbar-light bg-light">${area.title}</nav>
            <div id="infoBOX">
                <div id="infoImage"><img src="${area.firstimage}" width="100px"></div>
                <div id="infoData">
                    <div id="infoAdd">${area.addr1}${area.addr2}</div>
                    <div id="infoZip">(우) ${area.zipcode}</div>
                    <div id="infoTel">(전화번호) ${area.tel}</div>
                </div>   
            </div>
      `);

            let markerInfo = {
                title: area.title,
                latlng: new kakao.maps.LatLng(area.mapy, area.mapx),
                contenttypeid: area.contenttypeid
            };
            positions.push(markerInfo);
        });
        displayMarker(tripList);
    }

    // 카카오지도
    var mapContainer = document.getElementById("map"), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
            level: 8, // 지도의 확대 레벨
        };
    mapContainer.style.height = '600px';

    // 지도를 표시할 div와 지도 옵션으로 지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
    var mapTypeControl = new kakao.maps.MapTypeControl();

    // 지도에 컨트롤을 추가해야 지도위에 표시됩니다
    // kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
    map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

    // 지도 확대 축소를 제어할 수 있는 줌 컨트롤을 생성합니다
    var zoomControl = new kakao.maps.ZoomControl();
    map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

    // 지도에 표시된 마커 객체를 가지고 있을 배열입니다
    var markers = [];

    // 기존 마커들을 삭제하는 함수입니다
    function hideMarkers() {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
    }

    // 마커 클러스터러를 생성합니다
    var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 6, // 클러스터 할 최소 지도 레벨
    });

    function displayMarker(tripList) {
        markers = [];
        clusterer.clear();


        // 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
        var bounds = new kakao.maps.LatLngBounds();

        for (var i = 0; i < positions.length; i++) {
            // LatLngBounds 객체에 좌표를 추가합니다
            bounds.extend(positions[i].latlng);

            // 마커 이미지의 이미지 크기 입니다
            var imageSize = new kakao.maps.Size(30, 30);

            // 마커 이미지의 이미지 주소입니다
            var imageSrc = "../assets/img/marker" + positions[i].contenttypeid + ".png";

            // 마커 이미지를 생성합니다
            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

            // 마커를 생성합니다
            var marker = new kakao.maps.Marker({
                map: map, // 마커를 표시할 지도
                position: positions[i].latlng, // 마커를 표시할 위치
                title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                image: markerImage, // 마커 이미지
            });

            // 생성된 마커를 배열에 추가합니다
            markers.push(marker);

            // 클러스터러에 마커들을 추가합니다
            clusterer.addMarker(marker);

            // 마커에 표시할 인포윈도우를 생성합니다
            var infowindow = new kakao.maps.InfoWindow({
                content: positions[i].content // 인포윈도우에 표시할 내용
            });

            // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
            // 이벤트 리스너로는 클로저를 만들어 등록합니다
            // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
            kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow, i));
            kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
        }

        // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
        function makeOverListener(map, marker, infowindow, i) {
            return function () {
                infowindow.setContent(tripList[i]);
                infowindow.open(map, marker);
            };
        }

        // 인포윈도우를 닫는 클로저를 만드는 함수입니다
        function makeOutListener(infowindow) {
            return function () {
                infowindow.close();
            };
        }

        function setBounds() {
            // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
            // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
            map.setBounds(bounds);
        }

        // 지도 중심을 이동 시킵니다
        map.setBounds(bounds);
    }

