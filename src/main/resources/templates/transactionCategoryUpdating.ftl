<#import "spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update category name</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">

    <div class="row">
        <div class="col">
            <h2 class="mb-4">Enter data to change the transaction category name</h2>
            <form action="/category/update" method="POST">
                <@spring.bind "form"/>
                <#if spring.status.error>
                    <div class="alert alert-danger">
                        <#list spring.status.errorMessages as err>
                            ${err}<br>
                        </#list>
                    </div>
                </#if>

                <div class="mb-3">
                    <label for="exampleInputCategoryId" class="form-label">Category id</label>
                    <@spring.formInput "form.id" "class\"form-control\" id=\"exampleInputCategoryName\" placeholder=\"Enter category id\"" "number"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputAccountName" class="form-label">Category Name</label>
                    <@spring.formInput "form.name" "class\"form-control\" id=\"exampleInputCategoryName\" placeholder=\"Enter category name\"" "text"/>
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