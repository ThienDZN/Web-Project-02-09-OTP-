package vn.iotstar.controller.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;

@MultipartConfig
@WebServlet(urlPatterns = {
        "/admin/products",
        "/admin/product/add",
        "/admin/product/insert",
        "/admin/product/edit",
        "/admin/product/update",
        "/admin/product/delete"
})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/admin/products".equals(servletPath)) {
            showList(req, resp);
            return;
        }
        if ("/admin/product/add".equals(servletPath)) {
            showForm(req, resp, new Product(), "Create Catalog Entry", req.getContextPath() + "/admin/product/insert");
            return;
        }
        if ("/admin/product/edit".equals(servletPath)) {
            showEdit(req, resp);
            return;
        }
        if ("/admin/product/delete".equals(servletPath)) {
            deleteProduct(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/admin/product/insert".equals(servletPath)) {
            insertProduct(req, resp);
            return;
        }
        if ("/admin/product/update".equals(servletPath)) {
            updateProduct(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("products", productService.findAll());
        req.getRequestDispatcher("/views/admin/product-list.jsp").forward(req, resp);
    }

    private void showEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = productService.findById(parseId(req.getParameter("id")));
        if (product == null) {
            redirect(resp, req.getContextPath() + "/admin/products", "Track entry not found.");
            return;
        }
        showForm(req, resp, product, "Update Catalog Entry", req.getContextPath() + "/admin/product/update");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Product product, String formTitle, String formAction)
            throws ServletException, IOException {
        List<Category> categories = categoryService.findAll();
        req.setAttribute("product", product);
        req.setAttribute("categories", categories);
        req.setAttribute("formTitle", formTitle);
        req.setAttribute("formAction", formAction);
        req.getRequestDispatcher("/views/admin/product-form.jsp").forward(req, resp);
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = buildProductFromRequest(req, null);
        try {
            productService.insert(product);
            redirect(resp, req.getContextPath() + "/admin/products", "Catalog entry created successfully.");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            showForm(req, resp, product, "Create Catalog Entry", req.getContextPath() + "/admin/product/insert");
        }
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = parseId(req.getParameter("productId"));
        Product existing = productService.findById(id);
        if (existing == null) {
            redirect(resp, req.getContextPath() + "/admin/products", "Track entry not found.");
            return;
        }

        Product product = buildProductFromRequest(req, existing);
        product.setProductId(existing.getProductId());
        try {
            productService.update(product);
            redirect(resp, req.getContextPath() + "/admin/products", "Catalog entry updated successfully.");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            showForm(req, resp, product, "Update Catalog Entry", req.getContextPath() + "/admin/product/update");
        }
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = parseId(req.getParameter("id"));
        Product product = productService.findById(id);
        try {
            if (product != null && isLocalImage(product.getImage())) {
                Files.deleteIfExists(Paths.get(UploadConstants.DIR, product.getImage()));
            }
            productService.delete(id);
            redirect(resp, req.getContextPath() + "/admin/products", "Catalog entry deleted successfully.");
        } catch (Exception e) {
            redirect(resp, req.getContextPath() + "/admin/products", e.getMessage());
        }
    }

    private Product buildProductFromRequest(HttpServletRequest req, Product existing)
            throws IOException, ServletException {
        Product product = existing == null ? new Product() : existing;
        product.setProductName(req.getParameter("productName"));
        product.setDescription(req.getParameter("description"));
        product.setPrice(parsePrice(req.getParameter("price")));
        product.setQuantity(parseInt(req.getParameter("quantity")));
        product.setStatus(parseInt(req.getParameter("status")) == 1 ? 1 : 0);
        product.setCategory(resolveCategory(req.getParameter("categoryId")));
        product.setImage(resolveImage(req, existing == null ? null : existing.getImage()));
        return product;
    }

    private Category resolveCategory(String rawCategoryId) {
        Category category = categoryService.findById(parseInt(rawCategoryId));
        if (category == null) {
            throw new IllegalArgumentException("Selected category does not exist.");
        }
        return category;
    }

    private String resolveImage(HttpServletRequest req, String oldImage) throws IOException, ServletException {
        String imageLink = normalize(req.getParameter("image"));
        File uploadDir = new File(UploadConstants.DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        try {
            Part part = req.getPart("imageFile");
            if (part != null && part.getSize() > 0) {
                if (isLocalImage(oldImage)) {
                    Files.deleteIfExists(Paths.get(UploadConstants.DIR, oldImage));
                }
                String originalName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String extension = "";
                int index = originalName.lastIndexOf('.');
                if (index >= 0) {
                    extension = originalName.substring(index);
                }
                String savedName = System.currentTimeMillis() + extension;
                part.write(Paths.get(UploadConstants.DIR, savedName).toString());
                return savedName;
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Unable to save the uploaded image file.", e);
        }
        return imageLink != null ? imageLink : normalize(oldImage);
    }

    private void redirect(HttpServletResponse resp, String baseUrl, String message) throws IOException {
        resp.sendRedirect(baseUrl + "?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }

    private Long parseId(String raw) {
        try {
            return Long.parseLong(raw);
        } catch (Exception e) {
            return -1L;
        }
    }

    private int parseInt(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return 0;
        }
    }

    private BigDecimal parsePrice(String raw) {
        try {
            return new BigDecimal(raw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Price is not valid.");
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isLocalImage(String value) {
        return value != null && !value.isBlank() && !value.startsWith("http://") && !value.startsWith("https://");
    }
}
