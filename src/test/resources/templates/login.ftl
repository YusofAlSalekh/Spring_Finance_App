<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Authorise</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">
    <div class="row">
        <div class="col">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-4">
                    <h2 class="mb-4">Log in to your account</h2>
                    <form action="/login" method="POST">

                        <#if success??>
                            <div class="alert alert-success">${success}</div>
                        </#if>

                        <#if (RequestParameters.error)??>
                            <div class="alert alert-danger">Invalid email or password.</div>
                        </#if>

                        <#if (RequestParameters.logout)??>
                            <div class="alert alert-success">You have been logged out.</div>
                        </#if>

                        <div class="mb-3">
                            <label for="exampleInputEmail" class="form-label">Email</label>
                            <input name="email" class="form-control" id="exampleInputEmail" placeholder="Enter email"
                                   type="email">
                        </div>

                        <div class="mb-3">
                            <label for="exampleInputPassword" class="form-label">Password</label>
                            <input name="password" class="form-control" id="exampleInputPassword"
                                   placeholder="Enter password" type="password">
                        </div>

                        <button type="submit" class="btn btn-primary">Submit</button>

                        <div class="mt-3">
                            <p>If you do not have a bank account, create a new one.</p>
                            <a href="/register" class="btn btn-primary">Registration</a>
                        </div>

                    </form>
                </div>
            </div>
        </div>
        <script src="js/bootstrap.min.js"></script>
    </div>
</div>
</body>
</html>