<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <link rel="stylesheet" href="<c:url value='/assets/app-theme.css'/>">
</head>
<body class="theme-music">
<div class="theme-shell">
    <div class="panel form-shell">
        <div class="eyebrow">Admin Category</div>
        <h1>Update Category</h1>
        <c:if test="${not empty error}"><div class="error-box">${error}</div></c:if>
        <form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
            <input type="hidden" name="categoryid" value="${cate.categoryid}">
            <div class="form-group">
                <label>Category Name</label>
                <input class="form-input" type="text" name="categoryname" value="${cate.categoryname}" placeholder="Enter category name">
            </div>
            <div class="form-group">
                <label>Image URL</label>
                <input class="form-input" type="text" name="images" value="${cate.images}" placeholder="https://...">
            </div>
            <c:choose>
                <c:when test="${empty cate.images}"><img class="preview-image" src="<c:url value='/assets/no-image.svg'/>" alt="No image"></c:when>
                <c:when test="${fn:startsWith(cate.images, 'http://') or fn:startsWith(cate.images, 'https://')}"><img class="preview-image" src="${cate.images}" alt="${cate.categoryname}"></c:when>
                <c:otherwise><img class="preview-image" src="<c:url value='/image?fname=${cate.images}'/>" alt="${cate.categoryname}"></c:otherwise>
            </c:choose>
            <div class="form-group">
                <label>Upload New Image</label>
                <input class="form-file" type="file" name="images1" accept="image/*">
            </div>
            <div class="form-group">
                <label>Status</label>
                <div class="radio-row">
                    <label><input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> Visible</label>
                    <label><input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''}> Locked</label>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Update Category</button>
                <a class="btn btn-secondary" href="<c:url value='/admin/categories'/>">Back to Categories</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
