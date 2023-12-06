<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<header class="header__layout">
	<div class="header">
		<div class="header__left">
			<nav id="navbar" class="navbar">
				<ul>
					<li><a href="${root}/"><img id="logo"
							src="${root}/assets/img/logo.png" alt="logo" /></a></li>
					<li><a class="nav-link scrollto " href="${root}/travel/search">지역별여행지</a></li>
					<li><a class="nav-link scrollto"
						href="${root}/article/notReady">나의여행계획</a></li>
					<li><a class="nav-link scrollto"
						href="${root}/article/notReady">핫플자랑하기</a></li>
					<li><a class="nav-link scrollto "
						href="${root}/article/list?pgno=1&key=&word=">여행정보공유</a></li>
				</ul>
				<i class="bi bi-list mobile-nav-toggle"></i>
			</nav>
		</div>

		<div class="header__right">
			<c:if test="${empty sessionScope.userinfo}">
				<%-- <c:if test="${empty cookie['ssafy_id'].value}"> --%>
				<a href="${root}/user/login" id="loginText">로그인</a>
				<a href="${root}/user/join" id="registerText">회원가입</a>
			</c:if>
			<c:if test="${not empty sessionScope.userinfo}">
				<div id="profile-dropdown" class="hidden">
					<img id="profile" src="${root}/assets/img/profile.png"
						alt="profile" />
					<!-- <div class="profile-menu" id="profile-menu">
						<ul>
							<li><a href="#" id="userInfoLink" class="hidden">회원정보</a></li>
							<li><a href="#" id="logoutButton" class="hidden">로그아웃</a></li>
						</ul>
					</div> -->
				</div>
				<span id="useridnim"><c:out value="${userinfo['user_name']}" />님
					환영합니다.</span>
				<a href="${root}/" id="userInfoLink">회원정보</a>
				<form action='${root}/user/logout' method='get'>
					<input type='hidden' name='action' value='logout'> <input
						type='submit' id="logoutButton" value='로그아웃'><br>
				</form>
			</c:if>
		</div>
	</div>
</header>

<!-- 회원가입 모달 -->
<!-- <div id="join-modal" class="modal-overlay">
	<div class="modal-window">
		join modal header
		<header id="join-modal__modal_header_" class="modal-header">
			<h3 id="join-modal__modal_title_" class="modal-title">회원가입</h3>
			<button type="button" aria-label="Close" class="close-area">X</button>
		</header>

		join modal body

		<div id="join-modal___modal_body_" class="modal-body">
			<form id="form-join" method="POST" action="">
				<input type="hidden" name="action" value="join" />
				<div class="container-fluid">
					<div class="row mb-1 text-center">
						<div class="col-3">이름</div>
						<div class="col">
							<input type="text" placeholder="이름" class="form-control"
								id="mem_name" name="mem_name">
						</div>
					</div>
					<div class="row mb-1 text-center">
						<div class="col-3">아이디</div>
						<div class="col">
							<input type="text" placeholder="아이디" class="form-control"
								id="mem_id" name="mem_id">
						</div>
						<div class="col-3">
							<button type="button" class="btn btn-secondary btn-sm"
								id="idCheckBtn" name="idCheckBtn">중복확인</button>
						</div>
					</div>
					<div class="row mb-1 text-center">
						<div class="col-3">비밀번호</div>
						<div class="col">
							<input type="password" placeholder="비밀번호" class="form-control"
								id="mem_pw" name="mem_pw">
						</div>
					</div>
					<div class="row mb-1 text-center">
						<div class="col-3">비밀번호확인</div>
						<div class="col">
							<input type="password" placeholder="비밀번호확인" class="form-control"
								id="re_pw">
						</div>
					</div>
				</div>
			</form>
		</div>

		join modal footer
		<footer id="join-modal__modal_footer_" class="modal-footer">
			<div>
				<button type="button" class="btn btn-danger btn-sm"
					id="register-cancel-button">취소</button>
				<button type="button" class="btn mr-2 btn-primary btn-sm"
					id="memberInsertBtn">회원가입</button>
			</div>
		</footer>
	</div>
</div> -->

<!-- 로그인 모달 -->
<!-- <div id="login-modal" class="modal-overlay">
	<div class="modal-window">
		login modal header
		<header id="login-modal__modal_header_" class="modal-header">
			<h3 id="login-modal__modal_title_" class="modal-title">로그인</h3>
			<button type="button" aria-label="Close" class="close-area">X</button>
		</header>

		login modal body
		<form id="login-modal___modal_body_" class="modal-body">
			<input type="hidden" name="action" value="login" />
			<div class="container-fluid">
				<div class="row mb-1">
					<div class="col-12 col-md-3 text-md-right">
						아이디 라벨
						<label for="login-id">아이디</label>
					</div>
					<div class="col-12 col-md-9">
						아이디 입력란
						<input type="text" placeholder="아이디" class="form-control"
							id="loginid" name="loginid">
					</div>
				</div>
				<div class="row mb-1">
					<div class="col-12 col-md-3 text-md-right">
						비밀번호 라벨
						<label for="login-password">비밀번호</label>
					</div>
					<div class="col-12 col-md-9">
						비밀번호 입력란
						<input type="password" placeholder="비밀번호" class="form-control"
							id="loginpwd" name="loginpwd">
					</div>
				</div>
				<div class="row">
					<div class="col-12 col-md-9 offset-md-3">
						아이디 저장 체크박스
						<div class="form-check text-md-left">
							<label class="form-check-label" for="saveid">아이디 저장</label> <input
								type="checkbox" class="form-check-input" id="saveid"
								name="saveid" value="ok">
						</div>
					</div>
				</div>
			</div>
		</form>

		login modal footer
		<footer id="login-modal__modal_footer_" class="modal-footer">
			<div>
				<button type="button" class="btn btn-danger btn-sm"
					id="cancel-button">취소</button>
				<button type="button" class="btn mr-2 btn-primary btn-sm"
					id="loginBtn">로그인</button>
			</div>
		</footer>
	</div>
</div> -->
