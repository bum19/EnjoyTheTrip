<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<c:if test="${cookie.ssafy_id.value ne null}">
	<c:set var="idck" value=" checked" />
	<c:set var="saveid" value="${cookie.ssafy_id.value}" />
</c:if>
<!DOCTYPE html>
<html lang="ko">
<style>
.container {
	width: 80%; /* 부모 요소의 80% 너비를 가짐 */
	margin: 0 auto; /* 가운데 정렬을 위한 마진 설정 */
	text-align: center; /* 내용 가운데 정렬 */
}

.container img {
	width: 100%; /* 이미지의 너비를 부모 요소의 100%로 설정하여 부모에 맞춤 */
	max-width: 100%; /* 이미지가 원본보다 큰 경우에도 부모 요소 너비를 넘지 않도록 설정 */
	height: auto; /* 이미지의 원래 비율을 유지 */
}
</style>

<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous" />
<link href="${root}/assets/css/app.css" rel="stylesheet" />
<title>SSAFY</title>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
</head>

<body>
	<%@ include file="/WEB-INF/views/common/nav.jsp"%>
	<div class="container">
		<img src="${root}/assets/img/sea.png" alt="sea" />
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>
