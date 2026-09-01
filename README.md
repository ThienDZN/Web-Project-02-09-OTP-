# Assignment 02 OTP Product Shop

Project Jakarta Servlet/JSP cho bai tap 02, bo sung xac thuc tai khoan bang OTP qua email va quan ly san pham.

## Muc tieu bai tap

1. Kich hoat tai khoan bang OTP gui qua email khi dang ky.
2. Thuc hien chuc nang dang nhap, dang xuat.
3. Ho tro quen mat khau va xac nhan OTP qua email de dat lai mat khau.
4. Bo sung bang `products` voi moi lien he 1-n voi `categories`.
5. CRUD san pham trong trang quan tri.
6. Hien thi 10 san pham moi nhat o trang chu.
7. Hien thi danh sach san pham co phan trang 6 san pham/trang tai URL `/product`.
8. Hien thi chi tiet san pham tai `/product/detail?id=...`.
9. Ho tro upload anh san pham bang Multipart.

## Cong nghe

- Java 17
- Maven WAR
- Jakarta Servlet / JSP / JSTL
- JPA (Hibernate)
- SQL Server
- Jakarta Mail
- BCrypt

## Chuc nang da co

### Xac thuc va OTP

- Dang ky tai khoan voi `fullName`, `username`, `email`, `password`.
- Gui OTP kich hoat tai khoan qua email.
- Xac thuc OTP de mo khoa tai khoan moi.
- Dang nhap bang username hoac email.
- Dang xuat khoi he thong.
- Gui OTP quen mat khau qua email.
- Xac thuc OTP quen mat khau va dat lai mat khau moi.

### Product Shop

- Trang chu `/home` hien thi 10 san pham moi nhat.
- Trang `/product` hien thi danh sach san pham co phan trang.
- Trang `/product/detail` hien thi thong tin chi tiet san pham.
- Trang admin `/admin/products` cho phep them, sua, xoa san pham.
- Ho tro upload anh san pham tu may tinh hoac dung link anh.

## Cau truc chinh

- `src/main/java/vn/iotstar/controller`: servlet cho auth, home, product.
- `src/main/java/vn/iotstar/controller/admin`: CRUD san pham trong trang admin.
- `src/main/java/vn/iotstar/entity`: `UserAccount`, `OtpVerification`, `Product`, `Category`.
- `src/main/java/vn/iotstar/service`: xu ly auth, OTP, product, category.
- `src/main/resources/database.sql`: script tao va cap nhat bang du lieu.
- `src/main/resources/application.properties`: cau hinh upload, mail, phan trang.
- `src/main/webapp/views/auth`: giao dien login/register/verify OTP/forgot password.
- `src/main/webapp/views/product`: giao dien danh sach va chi tiet san pham.
- `src/main/webapp/views/admin`: giao dien quan tri category va product.

## Route chinh

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

## Co so du lieu

Project su dung cac bang chinh:

- `users`
- `otp_verifications`
- `categories`
- `products`

Script tao bang nam trong file `src/main/resources/database.sql`.

## Cau hinh mail

Mac dinh file `src/main/resources/application.properties` dang de:

```properties
app.mail.mock=true
```

Che do nay khong gui email that. OTP se duoc tra ve trong thong bao hoac in ra console de test nhanh.

Neu muon gui email that, sua:

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

## Chay project

Build project:

```bash
mvn clean package
```

WAR sau khi build:

```text
target/assignment02-otp-productshop.war
```

Deploy len Tomcat, sau do truy cap:

- `http://localhost:8080/assignment02-otp-productshop/home`
- `http://localhost:8080/assignment02-otp-productshop/product`
- `http://localhost:8080/assignment02-otp-productshop/login`
- `http://localhost:8080/assignment02-otp-productshop/admin/products`

## Tai khoan seed mac dinh

Project co seed san tai khoan admin:

- Username: `admin`
- Email: `admin@example.com`
- Password: `Admin@123`

## Ghi chu

- `app.product.page-size=6` quy dinh so san pham moi trang.
- Thu muc upload mac dinh duoc cau hinh boi `app.upload.dir`.
- Ten display name cua web app la `Assignment 02 OTP Product Shop`.
