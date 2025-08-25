<#import "spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Transaction Category</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">

    <div class="row">
        <div class="col">
            <h2 class="mb-4">Enter data to create a transaction category</h2>
            <form action="/category/create" method="POST">

                <div class="mb-3">
                    <label for="exampleInputAccountName" class="form-label">Category Name</label>
                    <@spring.formInput "form.name" "class\"form-control\" id=\"exampleInputCategoryName\" placeholder=\"Enter category name\"" "text"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Create Category</button>
            </form>
        </div>
    </div>
</div>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>