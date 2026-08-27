# JPA Category CRUD Assignment 01

Project mới cho bài 01 ngày 24/08/2026, tách riêng hoàn toàn với project khác.

## Nhiệm vụ chính

1. Cấu hình JPA bằng `persistence.xml`.
2. Tạo entity `Category`, `Video`.
3. Viết DAO và Service cho CRUD `Category`.
4. Viết Servlet Controller + JSP để thêm, sửa, xóa, xem danh sách category.
5. Có lớp test JPA thủ công để kiểm tra kết nối và lưu dữ liệu mẫu.

## Công nghệ

- Java 17
- Maven WAR
- Jakarta Servlet/JSP/JSTL
- JPA (Hibernate)
- SQL Server

## Cấu trúc chính

- `src/main/java/vn/iotstar/entity`: entity JPA
- `src/main/java/vn/iotstar/dao`: DAO
- `src/main/java/vn/iotstar/service`: Service
- `src/main/java/vn/iotstar/controller`: Servlet
- `src/main/resources/META-INF/persistence.xml`: cấu hình JPA
- `src/main/webapp/views/admin`: JSP CRUD category
- `src/main/resources/database.sql`: script tạo bảng SQL Server

## Cấu hình database

Mặc định `persistence.xml` đang để:

- Database: `HelloCoAiKhongDB`
- User: `user123`
- Password: `User123@Aa1`
- Server: `localhost:1433`

Nếu máy bạn khác, sửa trực tiếp file `src/main/resources/META-INF/persistence.xml`.

## Chạy project

```bash
mvn clean package
```

Deploy file `target/jpa-category-crud-assignment01.war` lên Tomcat.

Sau khi chạy:

- `http://localhost:8080/jpa-category-crud-assignment01/`
- `http://localhost:8080/jpa-category-crud-assignment01/admin/categories`

## Test JPA thủ công

Chạy lớp:

- `vn.iotstar.config.JpaManualTest`

Lớp này sẽ tạo một `Category` và một `Video` mẫu để kiểm tra JPA hoạt động.
