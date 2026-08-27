<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 32px; }
        form { max-width: 500px; }
        label { display: block; margin-top: 12px; margin-bottom: 6px; font-weight: 600; }
        input[type="text"], input[type="file"] { width: 100%; padding: 10px; box-sizing: border-box; }
        .error { padding: 12px; background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; margin-bottom: 16px; }
        .actions { margin-top: 20px; display: flex; gap: 10px; }
        img { width: 220px; height: 140px; object-fit: cover; border-radius: 6px; margin-top: 10px; }
        .button, button {
            display: inline-block; padding: 10px 14px; border: 1px solid #1d4ed8; background: #2563eb;
            color: white; text-decoration: none; border-radius: 6px; cursor: pointer;
        }
        .button-secondary { background: #4b5563; border-color: #374151; }
    </style>
</head>
<body>
<h1>Edit Category</h1>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
    <input type="hidden" name="categoryid" value="${cate.categoryid}">

    <label for="categoryname">Category name</label>
    <input type="text" id="categoryname" name="categoryname" value="${cate.categoryname}">

    <label for="images">Link image</label>
    <input type="text" id="images" name="images" value="${cate.images}">

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

    <label for="images1">Upload image mới</label>
    <input type="file" id="images1" name="images1" accept="image/*">

    <label>Status</label>
    <label><input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> Hoạt động</label>
    <label><input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''}> Khóa</label>

    <div class="actions">
        <button type="submit">Update</button>
        <a class="button button-secondary" href="<c:url value='/admin/categories'/>">Back</a>
    </div>
</form>
</body>
</html>
