<#import "spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Income report</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container text-center">

    <div class="row">
        <div class="col">
            <h2 class="mb-4">Enter data to see an income report</h2>
            <form action="/transaction/income" method="POST">

                <@spring.bind "form"/>
                <#if spring.status.error>
                    <div class="alert alert-danger">
                        <#list spring.status.errorMessages as err>
                            ${err}<br>
                        </#list>
                    </div>
                </#if>

                <div class="mb-3">
                    <label for="InputStartDate" class="form-label">Start Date</label>
                    <@spring.formInput "form.startDate" "class\"form-control\" id=\"InputStartDate\"" "date"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <div class="mb-3">
                    <label for="InputEndDate" class="form-label">End Date</label>
                    <@spring.formInput "form.endDate" "class\"form-control\" id=\"InputEndtDate\"" "date"/>
                    <div class="invalid-feedback d-block" style="min-height:1rem;">
                        <@spring.showErrors "<br>" />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Generate Report</button>
            </form>

            <#if transactions?? && (transactions?size > 0)>
                <h2 class="mt-4">Results</h2>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Category</th>
                        <th>Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list transactions as tx>
                        <tr>
                            <td>${tx.categoryName}</td>
                            <td>${tx.totalAmount}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            <#elseif transactions??>
                <div class="alert alert-info mt-4">
                    No income found for this period.
                </div>
            </#if>

            <div class="mt-3">
                <a href="/menu" class="btn btn-secondary">Back to Menu</a>
            </div>

        </div>
    </div>
</div>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>