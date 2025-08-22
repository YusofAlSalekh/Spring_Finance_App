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
            <h2 class="mb-4">Enter data to send money</h2>
            <form action="/transaction/create" method="POST">

                <@spring.bind "form"/>
                <#if spring.status.error>
                    <div class="alert alert-danger">
                        <#list spring.status.errorMessages as err>
                            ${err}<br>
                        </#list>
                    </div>
                </#if>

                <div class="mb-3">
                    <label for="InputCategoryIds" class="form-label">Category Ids</label>
                    <select name="categoryIds" id="InputCategoryIds" class="form-select" multiple size="5">
                        <#list categories as c>
                            <option value="${c.id}"
                                    <#if form.categoryIds?? && form.categoryIds?seq_contains(c.id)>selected</#if>>
                                ${c.name} (${c.id})
                            </option>
                        </#list>
                    </select>
                    <@spring.bind "form.categoryIds"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>"/>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputAmount" class="form-label">Amount</label>
                    <@spring.formInput "form.amount" "class\"form-control\" id=\"exampleInputAmount\" placeholder=\"Enter amount\"" "number"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputSenderId" class="form-label">SenderId</label>
                    <@spring.formInput "form.senderAccountId" "class\"form-control\" id=\"exampleInputSenderId\" placeholder=\"Enter sender id\"" "number"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="exampleInputReceiverId" class="form-label">ReceiverId</label>
                    <@spring.formInput "form.receiverAccountId" "class\"form-control\" id=\"exampleInputReceiverId\" placeholder=\"Enter receiver id\"" "number"/>
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