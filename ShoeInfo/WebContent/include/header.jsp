<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<title>SHOE INFO.</title>
<link href="./css/include/header.css" rel="stylesheet">
<script src="https://kit.fontawesome.com/febeeb992c.js" crossorigin="anonymous"></script>
<link href="https://fonts.googleapis.com/css?family=Anton|Noto+Sans+KR:700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Source+Sans+Pro&display=swap" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<nav class="navbar">
	
		<!-- 로고 -->
		<div class="navbar_logo">
			<a href="./SneakerList.go"> SHOE Info. </a>
		</div>
	
		<!-- 메인메뉴  -->
		<ul class="navbar_menu">
			<li> <a href="./SneakerList.go"> 런칭 캘린더 </a> </li>
			<li> <a href="#"> 발매 예정 </a> </li>
			<li> <a href="#"> 기습 발매 </a> </li>
		</ul>
		
		<!-- 로그인, 마이페이지, 로그아웃 링크 -->
		<ul class="navbar_userlinks">
			<%
				//로그인 되었는지	
				String user = (String) session.getAttribute("email");
				//로그인 된 사용자의 position 가져오기
				String usr_position = (String) session.getAttribute("usr_position");
				if(usr_position == null){
					usr_position = "";
				}
				
				if(user == null){
			%>
				<li> <a href="./MemberLogin.me"> LOGIN </a> </li>
			<%
				}else if(usr_position.equals("admin")){ 
			%>
				<li> <a href="./Main.ad"> ADMIN_PAGE </a> </li>
				<li> <a href="./MemberDrawInfo.me"> MYPAGE </a> </li>
				<li> <a href="./MemberLogout.me"> LOGOUT </a> </li>
			<% 
				}else{ 
			%>
				<li> <a href="./MemberDrawInfo.me"> MYPAGE </a> </li>
				<li> <a href="./MemberLogout.me"> LOGOUT </a> </li>
			<% 
				} 
			%> 
		</ul>

		
		
		<!-- 모바일 버전일때 메뉴 버튼 -->
		<a href="#" class="navbar_toggleBtn">
			<i class="fas fa-bars"></i>
		</a>
		
	</nav>

</body>
<script type="text/javascript">

	
// 		if($(".navbar_toggleBtn").css("display") == "none"){
// 			$(".navbar_menu").css("display", "flex");
// 			$(".navbar_userlinks").css("display", "flex");
// 		}
	

		$(".navbar_toggleBtn").click(function(){
			if($(".navbar_menu").css("display") == "none"){
				$(".navbar_menu").slideDown();
				$(".navbar_userlinks").slideDown();
			}
			else if($(".navbar_menu").css("display") == "block"){
				$(".navbar_menu").slideUp();
				$(".navbar_userlinks").slideUp();
			}
//	 		$(".navbar_menu").slideToggle("fast");
//	 		$(".navbar_userlinks").slideToggle("fast");
		});

	
</script>
</html>