<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>登陆</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="../../../assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="../../../assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="../../../assets/css/ace.min.css" />
<link rel="stylesheet" href="../../../assets/css/ace-rtl.min.css" />
<script src="../../../assets/js/jquery-2.0.3.min.js"></script>
</head>
<body class="login-layout">
	<div class="main-container">
		<div class="main-content">
			<div class="row">
				<div class="col-sm-10 col-sm-offset-1">
					<div class="login-container">
						<div class="center">
							<h1>
								<i class="icon-leaf green"></i> <span class="red">Think</span> <span
									class="white">CMS管理系统</span>
							</h1>
						</div>
						<div class="space-6"></div>
						<div class="position-relative">
							<div id="login-box" class="login-box visible widget-box">
								<div class="widget-body">
									<div class="widget-main">
										<h4 class="header blue lighter bigger">
											<i class="icon-coffee green"></i> 请输入用户名密码
										</h4>
										<div class="space-6"></div>
										<div style="color: red">${errmsg}</div>
										<form action="/sys/login.do" method="post">
											<fieldset>
												<label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="text" class="form-control" placeholder="用户名"
														name="loginCode" /> <i class="icon-user"></i>
												</span>
												</label> <label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="password" class="form-control" placeholder="密码"
														name="loginPsw" /> <i class="icon-lock"></i>
												</span>
												</label>
												<div class="space"></div>
												<div class="clearfix">
													<!-- <label class="inline"> <input type="checkbox"
														class="ace" /> <span class="lbl"> 记住我</span>
													</label> -->
													<button type="submit"
														class="width-35 pull-right btn btn-sm btn-primary">
														<i class="icon-key"></i> 点击登陆
													</button>
												</div>
												<div class="space-4"></div>
											</fieldset>
										</form>
									</div>
									<!-- /widget-main -->
									<div class="toolbar clearfix">
										<div>
											<a href="#" onclick="" class="user-signup-link"> </a>
										</div>
										<div>
											<a href="" class="user-signup-link"> </a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
