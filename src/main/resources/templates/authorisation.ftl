<#import "spring.ftl" as spring />

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
            <h2 class="mb-4">Log in to your account</h2>
            <form action="/login" method="POST">

                <#if success??>
                    <div class="alert alert-success">${success}</div>
                </#if>

                <@spring.bind "form"/>
                <#if spring.status.error>
                    <div class="alert alert-danger">
                        <#list spring.status.errorMessages as err>
                            ${err}<br>
                        </#list>
                    </div>
                </#if>

                <div class="mb-3">
                    <label for="exampleInputEmail" class="form-label">Email</label>
                    <@spring.formInput "form.email" "class\"form-control\" id=\"exampleInputEmail\" placeholder=\"Enter email\"" "email"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputPassword" class="form-label">Password</label>
                    <@spring.formInput "form.password" "class\"form-control\" id=\"exampleInputPassword\" placeholder=\"Password\"" "password"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
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
</body>
</html>