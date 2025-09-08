<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Menu</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container"></div>

<div class="row">
    <h1>User id: ${id}</h1>
    <h1>Hello, ${name}!</h1>
</div>

<#if success??>
    <div class="alert alert-success">${success}</div>
</#if>

<div class="d-flex flex-wrap gap-2 mt-5">
    <a href="/account/create" class="btn btn-primary">Create a new account</a>
    <a href="/account/delete" class="btn btn-primary">Delete account</a>
    <a href="/account/update" class="btn btn-primary">Change account name</a>
    <a href="/menu?show=accounts" class="btn btn-secondary">Show accounts</a>
    <a href="/category/create" class="btn btn-primary">Create category</a>
    <a href="/category/delete" class="btn btn-primary">Delete category</a>
    <a href="/category/update" class="btn btn-primary">Update category</a>
    <a href="/menu?show=categories" class="btn btn-secondary">Show categories</a>
    <a href="/transaction/create" class="btn btn-primary">Create transaction</a>
    <a href="/transaction/expense" class="btn btn-primary">Show expense report</a>
    <a href="/transaction/income" class="btn btn-primary">Show income report</a>
</div>

<div class="mt-3">
    <a href="/logout" class="btn btn-secondary">Logout</a>
</div>

<#if accounts?? && (accounts?size > 0)>
    <h3>Your Accounts</h3>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Balance</th>
            <th>Client Id</th>
        </tr>
        </thead>
        <tbody>
        <#list accounts as a>
            <tr>
                <td>${a.id}</td>
                <td>${a.name}</td>
                <td>${a.balance}</td>
                <td>${a.clientId}</td>
            </tr>
        </#list>
        </tbody>
    </table>
<#elseif accounts??>
    <div class="alert alert-info">You have no accounts yet.</div>
</#if>

<#if categories?? && (categories?size > 0)>
    <h3>Your Categories</h3>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Client Id</th>
        </tr>
        </thead>
        <tbody>
        <#list categories as c>
            <tr>
                <td>${c.id}</td>
                <td>${c.name}</td>
                <td>${c.clientId}</td>
            </tr>
        </#list>
        </tbody>
    </table>
<#elseif categories??>
    <div class="alert alert-info">You have no categories yet.</div>
</#if>

<script src="js/bootstrap.min.js"></script>
</body>
</html>