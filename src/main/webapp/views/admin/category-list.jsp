<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 32px; }
        .topbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .message { padding: 12px; background: #edf7ed; color: #1e4620; border: 1px solid #b7dfb9; margin-bottom: 16px; }
        .actions { display: flex; gap: 8px; align-items: center; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; vertical-align: middle; }
        th { background: #f5f5f5; }
        img { width: 180px; height: 110px; object-fit: cover; border-radius: 6px; }
        .button, button {
            display: inline-block; padding: 8px 12px; border: 1px solid #1d4ed8; background: #2563eb;
            color: white; text-decoration: none; border-radius: 6px; cursor: pointer;
        }
        .button-danger { background: #dc2626; border-color: #b91c1c; }
        .search-input { min-width: 260px; padding: 8px; }
    </style>
</head>
<body>
<div class="topbar">
    <div>
        <h1>Category Management</h1>
        <p>Tổng số category: ${fn:length(listcate)}</p>
    </div>
    <a class="button" href="<c:url value='/admin/category/add'/>">Add Category</a>
</div>

<c:if test="${not empty param.message}">
    <div class="message">${param.message}</div>
</c:if>

<form action="<c:url value='/admin/categories'/>" method="get" style="margin-bottom: 20px;">
    <div class="actions">
        <input class="search-input" type="text" name="keyword" value="${keyword}" placeholder="Search by category name">
        <button type="submit">Search</button>
        <a class="button" href="<c:url value='/admin/categories'/>">Reset</a>
    </div>
</form>

<table>
    <tr>
        <th>STT</th>
        <th>Image</th>
        <th>Category name</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${listcate}" var="cate" varStatus="stt">
        <tr>
            <td>${stt.index + 1}</td>
            <td>
                <c:choose>
                    <c:when test="${empty cate.images}">
                        <img src="<c:url value='/assets/no-image.svg'/>" alt="No image">
                    </c:when>
                    <c:when test="${fn:startsWith(cate.images, 'http://') or fn:startsWith(cate.images, 'https://')}">
                        <img src="${cate.images}" alt="${cate.categoryname}">
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value='/image?fname=${cate.images}'/>" alt="${cate.categoryname}">
                    </c:otherwise>
                </c:choose>
            </td>
            <td>${cate.categoryname}</td>
            <td>
                <c:choose>
                    <c:when test="${cate.status == 1}">Hoạt động</c:when>
                    <c:otherwise>Khóa</c:otherwise>
                </c:choose>
            </td>
            <td>
                <a class="button" href="<c:url value='/admin/category/edit?id=${cate.categoryid}'/>">Sửa</a>
                <a class="button button-danger"
                   href="<c:url value='/admin/category/delete?id=${cate.categoryid}'/>"
                   onclick="return confirm('Bạn có chắc muốn xóa category này?');">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
