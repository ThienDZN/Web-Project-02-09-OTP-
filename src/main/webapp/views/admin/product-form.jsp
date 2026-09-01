<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${formTitle}</title>
    <link rel="stylesheet" href="<c:url value='/assets/app-theme.css'/>">
</head>
<body class="theme-music">
<div class="theme-shell">
    <div class="panel form-shell">
        <div class="eyebrow">Admin Catalog Entry</div>
        <h1>${formTitle}</h1>
        <p class="inline-note">This form still fulfills the product CRUD requirement while the sample content now follows Thang's Cai Thu Hai track list.</p>
        <c:if test="${not empty error}"><div class="error-box">${error}</div></c:if>
        <form action="${formAction}" method="post" enctype="multipart/form-data">
            <c:if test="${product.productId != null}">
                <input type="hidden" name="productId" value="${product.productId}">
            </c:if>
            <div class="form-group">
                <label>Title</label>
                <input class="form-input" type="text" name="productName" value="${product.productName}" placeholder="Enter title">
            </div>
            <div class="form-group">
                <label>Category</label>
                <select class="form-select" name="categoryId">
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.categoryid}" ${product.category != null and product.category.categoryid == category.categoryid ? 'selected' : ''}>${category.categoryname}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Price</label>
                <input class="form-input" type="number" step="0.01" name="price" value="${product.price}" placeholder="0.00">
            </div>
            <div class="form-group">
                <label>Quantity</label>
                <input class="form-input" type="number" name="quantity" value="${product.quantity}" placeholder="0">
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea class="form-textarea" name="description" placeholder="Write a description">${product.description}</textarea>
            </div>
            <div class="form-group">
                <label>Image URL</label>
                <input class="form-input" type="text" name="image" value="${product.image}" placeholder="https://... or keep empty to upload a file">
            </div>
            <c:if test="${not empty product.image}">
                <c:choose>
                    <c:when test="${fn:startsWith(product.image, 'http://') or fn:startsWith(product.image, 'https://')}"><img class="preview-image" src="${product.image}" alt="${product.productName}"></c:when>
                    <c:otherwise><img class="preview-image" src="<c:url value='/image?fname=${product.image}'/>" alt="${product.productName}"></c:otherwise>
                </c:choose>
            </c:if>
            <div class="form-group">
                <label>Upload Image</label>
                <input class="form-file" type="file" name="imageFile" accept="image/*">
            </div>
            <div class="form-group">
                <label>Status</label>
                <div class="radio-row">
                    <label><input type="radio" name="status" value="1" ${product.status != 0 ? 'checked' : ''}> Visible</label>
                    <label><input type="radio" name="status" value="0" ${product.status == 0 ? 'checked' : ''}> Hidden</label>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Save Entry</button>
                <a class="btn btn-secondary" href="<c:url value='/admin/products'/>">Back to Catalog</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
