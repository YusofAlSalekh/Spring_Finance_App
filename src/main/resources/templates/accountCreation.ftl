<#import "spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Account</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">
    <div class="row">
        <div class="col">
            <h2 class="mb-4">Enter data to create an account</h2>
            <form action="/account/create" method="POST">

                <div class="mb-3">
                    <label for="exampleInputAccountName" class="form-label">Account Name</label>
                    <@spring.formInput "form.name" "class\"form-control\" id=\"exampleInputAccountName\" placeholder=\"Enter account name\"" "text"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputAccountBalance" class="form-label">Account Balance</label>
                    <@spring.formInput "form.balance" "class\"form-control\" id=\"exampleInputAccountName\" placeholder=\"Account Balance\"" "number"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Create Account</button>
            </form>
        </div>
    </div>
</div>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>