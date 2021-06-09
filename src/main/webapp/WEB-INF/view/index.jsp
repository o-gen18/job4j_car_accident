<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    <script type="text/javascript" src="<c:url value='/js/scripts.js'/>"></script>
    <title>Accident</title>
</head>
<body>
<header class="container-fluid">
    <h1 class="h1" align="center" style="display: inline">
        Accident table
    </h1>
    <a class="btn btn-outline-primary" style="float: right" href="<c:url value='/create'/>">Submit a new accident</a>
</header>
<div class="container-fluid">
    <table class="table table-hover table-bordered table-sm">
        <thead>
            <tr class="table-primary">
                <th scope="col">№</th>
                <th scope="col">Accident</th>
                <th scope="col">Statement</th>
                <th scope="col">Address</th>
                <th scope="col">Type</th>
                <th scope="col">Rule</th>
            </tr>
        </thead>
        <tbody>
            <c:set var="counter" value="0" scope="page"/>
            <c:forEach items="${accidents}" var="accident">
                <c:set var="counter" value="${counter + 1}" scope="page"/>
            <tr class="table-secondary" >
                <th scope="row"><c:out value="${counter}"/></th>
                <td><a title="Click to edit this record" style="text-decoration: none; color: black" href='<c:url value="/edit?id=${accident.id}"/>'><c:out value="${accident.name}"/></a></td>
                <td><div style="height:150px; overflow: auto"><c:out value="${accident.text}"/></div></td>
                <td><c:out value="${accident.address}"/></td>
                <td><c:out value="${accident.type.name}"/></td>
                <td>
                    <table>
                    <c:forEach items="${accident.rules}" var="rule">
                        <tr>
                            <td><c:out value="${rule.name}"/></td>
                        </tr>
                    </c:forEach>
                    </table>
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" crossorigin="anonymous"></script>
</body>
</html>