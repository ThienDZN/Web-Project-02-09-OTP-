package vn.iotstar.controller.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.config.UploadConstants;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@MultipartConfig
@WebServlet(urlPatterns = {
        "/admin/categories",
        "/admin/category/add",
        "/admin/category/insert",
        "/admin/category/edit",
        "/admin/category/update",
        "/admin/category/delete"
})
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/categories")) {
            showList(req, resp);
            return;
        }
        if (url.contains("/admin/category/add")) {
            req.setAttribute("cate", new Category());
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
            return;
        }
        if (url.contains("/admin/category/edit")) {
            showEditForm(req, resp);
            return;
        }
        if (url.contains("/admin/category/delete")) {
            deleteCategory(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String url = req.getRequestURI();
        if (url.contains("/admin/category/insert")) {
            insertCategory(req, resp);
            return;
        }
        if (url.contains("/admin/category/update")) {
            updateCategory(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = normalize(req.getParameter("keyword"));
        List<Category> list = (keyword == null)
                ? categoryService.findAll()
                : categoryService.searchByName(keyword);
        req.setAttribute("listcate", list);
        req.setAttribute("keyword", keyword == null ? "" : keyword);
        req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = parseId(req.getParameter("id"));
        Category category = categoryService.findById(id);
        if (category == null) {
            redirectWithMessage(resp, req.getContextPath() + "/admin/categories",
                    "Không tìm thấy category cần sửa.");
            return;
        }
        req.setAttribute("cate", category);
        req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
    }

    private void insertCategory(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category category = new Category();
        category.setCategoryname(req.getParameter("categoryname"));
        category.setStatus(parseStatus(req.getParameter("status")));
        category.setImages(resolveImage(req, null));

        try {
            categoryService.insert(category);
            redirectWithMessage(resp, req.getContextPath() + "/admin/categories",
                    "Thêm category thành công.");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
        }
    }

    private void updateCategory(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int categoryid = parseId(req.getParameter("categoryid"));
        Category category = categoryService.findById(categoryid);
        if (category == null) {
            redirectWithMessage(resp, req.getContextPath() + "/admin/categories",
                    "Category không tồn tại.");
            return;
        }

        category.setCategoryname(req.getParameter("categoryname"));
        category.setStatus(parseStatus(req.getParameter("status")));
        category.setImages(resolveImage(req, category.getImages()));

        try {
            categoryService.update(category);
            redirectWithMessage(resp, req.getContextPath() + "/admin/categories",
                    "Cập nhật category thành công.");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
        }
    }

    private void deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = parseId(req.getParameter("id"));
        try {
            Category category = categoryService.findById(id);
            if (category != null && isLocalImage(category.getImages())) {
                deleteFile(Paths.get(UploadConstants.DIR, category.getImages()));
            }
            categoryService.delete(id);
            redirectWithMessage(resp, req.getContextPath() + "/admin/categories",
                    "Xóa category thành công.");
        } catch (Exception e) {
            redirectWithMessage(resp, req.getContextPath() + "/admin/categories", e.getMessage());
        }
    }

    private String resolveImage(HttpServletRequest req, String oldImage)
            throws IOException, ServletException {
        String imageLink = normalize(req.getParameter("images"));
        File uploadDir = new File(UploadConstants.DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            Part part = req.getPart("images1");
            if (part != null && part.getSize() > 0) {
                if (isLocalImage(oldImage)) {
                    deleteFile(Paths.get(UploadConstants.DIR, oldImage));
                }
                String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String extension = "";
                int index = filename.lastIndexOf('.');
                if (index >= 0) {
                    extension = filename.substring(index);
                }
                String savedName = System.currentTimeMillis() + extension;
                part.write(Paths.get(UploadConstants.DIR, savedName).toString());
                return savedName;
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Không thể lưu file ảnh.", e);
        }

        if (imageLink != null) {
            return imageLink;
        }
        return normalize(oldImage);
    }

    private void redirectWithMessage(HttpServletResponse resp, String baseUrl, String message)
            throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(baseUrl + "?message=" + encoded);
    }

    private int parseId(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id không hợp lệ.");
        }
    }

    private int parseStatus(String raw) {
        return "1".equals(raw) ? 1 : 0;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isLocalImage(String value) {
        return value != null
                && !value.isBlank()
                && !value.startsWith("http://")
                && !value.startsWith("https://");
    }

    private void deleteFile(Path path) throws IOException {
        Files.deleteIfExists(path);
    }
}
