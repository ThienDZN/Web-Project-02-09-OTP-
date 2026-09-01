# Assignment 02 OTP Product Shop

Project Jakarta Servlet/JSP cho bài tập 02, bổ sung xác thực tài khoản bằng OTP qua email và quản lý sản phẩm.

## Mục tiêu bài tập

1. Kích hoạt tài khoản bằng OTP gửi qua email khi đăng ký.
2. Thực hiện chức năng đăng nhập, đăng xuất.
3. Hỗ trợ quên mật khẩu và xác nhận OTP qua email để đặt lại mật khẩu.
4. Bổ sung bảng `products` với mối liên hệ 1-n với `categories`.
5. CRUD sản phẩm trong trang quản trị.
6. Hiển thị 10 sản phẩm mới nhất ở trang chủ.
7. Hiển thị danh sách sản phẩm có phân trang 6 sản phẩm/trang tại URL `/product`.
8. Hiển thị chi tiết sản phẩm tại `/product/detail?id=...`.
9. Hỗ trợ upload ảnh sản phẩm bằng Multipart.

## Công nghệ

- Java 17
- Maven WAR
- Jakarta Servlet / JSP / JSTL
- JPA (Hibernate)
- SQL Server
- Jakarta Mail
- BCrypt

## Chức năng đã có

### Xác thực và OTP

- Đăng ký tài khoản với `fullName`, `username`, `email`, `password`.
- Gửi OTP kích hoạt tài khoản qua email.
- Xác thực OTP để mở khóa tài khoản mới.
- Đăng nhập bằng username hoặc email.
- Đăng xuất khỏi hệ thống.
- Gửi OTP quên mật khẩu qua email.
- Xác thực OTP quên mật khẩu và đặt lại mật khẩu mới.

### Product Shop

- Trang chủ `/home` hiển thị 10 sản phẩm mới nhất.
- Trang `/product` hiển thị danh sách sản phẩm có phân trang.
- Trang `/product/detail` hiển thị thông tin chi tiết sản phẩm.
- Trang admin `/admin/products` cho phép thêm, sửa, xóa sản phẩm.
- Hỗ trợ upload ảnh sản phẩm từ máy tính hoặc dùng link ảnh.

## Cấu trúc chính

- `src/main/java/vn/iotstar/controller`: servlet cho auth, home, product.
- `src/main/java/vn/iotstar/controller/admin`: CRUD sản phẩm trong trang admin.
- `src/main/java/vn/iotstar/entity`: `UserAccount`, `OtpVerification`, `Product`, `Category`.
- `src/main/java/vn/iotstar/service`: xử lý auth, OTP, product, category.
- `src/main/resources/database.sql`: script tạo và cập nhật bảng dữ liệu.
- `src/main/resources/application.properties`: cấu hình upload, mail, phân trang.
- `src/main/webapp/views/auth`: giao diện login/register/verify OTP/forgot password.
- `src/main/webapp/views/product`: giao diện danh sách và chi tiết sản phẩm.
- `src/main/webapp/views/admin`: giao diện quản trị category và product.

## Route chính

- `/register`
- `/verify-otp`
- `/resend-otp`
- `/login`
- `/logout`
- `/forgot-password`
- `/reset-password`
- `/home`
- `/product`
- `/product/detail?id=...`
- `/admin/products`

## Cơ sở dữ liệu

Project sử dụng các bảng chính:

- `users`
- `otp_verifications`
- `categories`
- `products`

Script tạo bảng nằm trong file `src/main/resources/database.sql`.

## Cấu hình mail

Mặc định file `src/main/resources/application.properties` đang để:

```properties
app.mail.mock=true
```

Chế độ này không gửi email thật. OTP sẽ được trả về trong thông báo hoặc in ra console để test nhanh.

Nếu muốn gửi email thật, sửa:

```properties
app.mail.mock=false
app.mail.host=...
app.mail.port=587
app.mail.username=...
app.mail.password=...
app.mail.from=...
app.mail.auth=true
app.mail.starttls=true
app.mail.ssl=false
```

## Chạy project

Build project:

```bash
mvn clean package
```

WAR sau khi build:

```text
target/assignment02-otp-productshop.war
```

Deploy lên Tomcat, sau đó truy cập:

- `http://localhost:8080/assignment02-otp-productshop/home`
- `http://localhost:8080/assignment02-otp-productshop/product`
- `http://localhost:8080/assignment02-otp-productshop/login`
- `http://localhost:8080/assignment02-otp-productshop/admin/products`

## Tài khoản seed mặc định

Project có seed sẵn tài khoản admin:

- Username: `admin`
- Email: `admin@example.com`
- Password: `Admin@123`

## Ghi chú

- `app.product.page-size=6` quy định số sản phẩm mỗi trang.
- Thư mục upload mặc định được cấu hình bởi `app.upload.dir`.
- Tên display name của web app là `Assignment 02 OTP Product Shop`.
