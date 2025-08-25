<#import "spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Change account name</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">
    <div class="row">
        <div class="col">
            <h2 class="mb-4">Enter data to change the name of the account</h2>
            <form action="/account/update" method="POST">
                <@spring.bind "form"/>
                <#if spring.status.error>
                    <div class="alert alert-danger">
                        <#list spring.status.errorMessages as err>
                            ${err}<br>
                        </#list>
                    </div>
                </#if>

                <div class="mb-3">
                    <label for="exampleInputAccountId" class="form-label">Account id</label>
                    <@spring.formInput "form.accountId" "class\"form-control\" id=\"exampleInputAccountName\" placeholder=\"Enter account id\"" "number"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputAccountName" class="form-label">Account Name</label>
                    <@spring.formInput "form.name" "class\"form-control\" id=\"exampleInputAccountName\" placeholder=\"Enter account name\"" "text"/>
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