<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <title>Login - Warehouse Clothing</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Warehouse Clothing Login Page"/>
    <meta name="author" content="Anh Bo adapt from Shreethemes"/>
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/assets/images/favicon.ico.png">
    <link href="<%= request.getContextPath() %>/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%= request.getContextPath() %>/assets/css/materialdesignicons.min.css" rel="stylesheet"
          type="text/css"/>
    <link href="<%= request.getContextPath() %>/assets/css/remixicon.css" rel="stylesheet" type="text/css"/>
    <link href="https://unicons.iconscout.com/release/v3.0.6/css/line.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/assets/css/style.min.css" rel="stylesheet" type="text/css"
          id="theme-opt"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <style>
        /* Thêm style cho message nếu cần, hoặc dùng class alert của Bootstrap */
        .custom-success-message {
            color: green;
            padding: 10px;
            border: 1px solid green;
            border-radius: 5px;
            margin-bottom: 15px;
            background-color: #e6ffe6;
        }

        .custom-error-message {
            color: red;
            padding: 10px;
            border: 1px solid red;
            border-radius: 5px;
            margin-bottom: 15px;
            background-color: #ffeeee;
        }


    </style>
</head>

<body>
<div id="preloader">
    <div id="status">
        <div class="spinner">
            <div class="double-bounce1"></div>
            <div class="double-bounce2"></div>
        </div>
    </div>
</div>
<div class="back-to-home rounded d-none d-sm-block">
    <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-icon btn-primary"><i data-feather="home"
                                                                                            class="icons"></i></a>
</div>

<section class="bg-home d-flex bg-light align-items-center"
         style="background: url('<%= request.getContextPath() %>/images/background_login.jpg') center;">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-8">

                <div class="card login-page bg-white shadow mt-4 rounded border-0">
                    <div class="card-body">
                        <h4 class="text-center" id="login-title" ><i class="fas fa-warehouse"> Warehouse Clothing</i></h4>
                        <p class="text-center"><i>Sign in to manage your inventory</i></p>
                        <form action="<%= request.getContextPath() %>/LoginServlet" method="post"
                              class="login-form mt-4" aria-labelledby="login-title">

                            <% if ("success".equals(request.getParameter("logout"))) { %>
                            <div class="alert alert-success" role="alert">
                                {/* Thay icon nếu muốn, ví dụ: <i class="uil uil-check-circle"></i> */}
                                You have successfully logged out.
                            </div>
                            <% } %>

                            <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                {/* Thay icon nếu muốn, ví dụ: <i class="uil uil-exclamation-octagon"></i> */}
                                <%= request.getAttribute("error") %>
                            </div>
                            <% } %>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="mb-3">
                                        <label class="form-label">Your Email <span class="text-danger">*</span></label>
                                        <input type="email" class="form-control" placeholder="Email" name="email"
                                               required
                                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>">
                                    </div>
                                </div>

                                <div class="col-lg-12">
                                    <div class="mb-3">
                                        <label class="form-label">Password <span class="text-danger">*</span></label>
                                        <input type="password" class="form-control" placeholder="Password"
                                               name="password" required
                                               value="<%= request.getAttribute("password") != null ? request.getAttribute("password") : "" %>">
                                    </div>
                                </div>

                                <div class="col-lg-12">
                                    <div class="d-flex justify-content-between">
                                        <div class="mb-3">
                                            <div class="form-check">
                                                <input class="form-check-input align-middle" type="checkbox"
                                                       name="remember" id="remember-check"
                                                    <%= request.getAttribute("remember") != null ? "checked" : "" %>>
                                                <label class="form-check-label" for="remember-check">Remember me</label>
                                            </div>
                                        </div>
                                        <a href="#" class="text-dark h6 mb-0">Forgot password ?</a>
                                    </div>
                                </div>
                                <div class="col-lg-12 mb-0">
                                    <div class="d-grid">
                                        <button class="btn btn-primary" type="submit"><i
                                                class="fas fa-sign-in-alt me-1"></i> Sign in
                                        </button>
                                    </div>
                                </div>

                                <div class="col-lg-12 mt-3 text-center">
                                    <h6 class="text-muted">Or</h6>
                                </div>



                                <div class="col-12 mt-3">
                                    <div class="d-grid">
                                        <a href="#" class="btn btn-soft-primary"><i class="uil uil-google"></i>
                                            Google</a>
                                    </div>
                                </div>

                                <div class="col-12 text-center">
                                    <p class="mb-0 mt-3"><small class="text-dark me-2">Don't have an account ? Contact with Admin!</small>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script src="<%= request.getContextPath() %>/assets/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/feather.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/app.js"></script>

</body>
</html>