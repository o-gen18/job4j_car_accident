<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link rel="stylesheet" href="css/styles.css">
    <title>Edit accident</title>
</head>
<body>
<div class="container-fluid">

</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" crossorigin="anonymous"></script>
</body>
<div class="container-fluid">
    <a href="<c:url value='/'/>" class="btn btn-outline-secondary" style="position: absolute">Home page</a>
    <div class="card mx-auto border-0" style="width: 50%">
        <h2 class="h2 mx-auto" style="display: inline">Editing the existing accident record</h2>
        <div class="card-body border border-primary rounded">
            <form action="<c:url value='/save'/>" method="post">
                <table class="table">
                    <input hidden name="id" value="<c:out value="${accident.id}"/>">
                    <tr>
                        <td>Name of accident: </td>
                        <td><input class="form-control" type="text" name="name" value="<c:out value="${accident.name}"/>"></td>
                    </tr>
                    <tr>
                        <td>Details: </td>
                        <td><textarea class="form-control" name="text"><c:out value="${accident.text}"/></textarea></td>
                    </tr>
                    <tr>
                        <td>Address of the accident: </td>
                        <td><input class="form-control" type="text" name="address" value="<c:out value="${accident.address}"/>"></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input class="btn btn-outline-primary" name="submit" type="submit" value="Submit"></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
</html>
