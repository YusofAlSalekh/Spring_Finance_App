<#import "spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">

    <div class="row">
        <div class="col">
            <h2 class="mb-4">Enter data to create a bank account</h2>
            <form action="/register" method="POST">

                <div class="mb-3">
                    <label for="exampleInputLogin" class="form-label">Login</label>
                    <@spring.formInput "form.login" "class\"form-control\" id=\"exampleLogin\" placeholder=\"Enter login\"" "text"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputPassword" class="form-label">Password</label>
                    <@spring.formInput "form.password" "class\"form-control\" id=\"exampleInputPassword\" placeholder=\"Password\"" "number"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>