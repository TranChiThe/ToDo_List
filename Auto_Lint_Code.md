### Giới thiệu lint code.

- Lint code là quá trình kiểm tra mã nguồn để phát hiện các lỗi tiềm ẩn, vấn đề về hiệu suất, bảo
  mật
  hoặc các vi phạm quy tắc coding mà không cần chạy ứng dụng.
- Trong Kotlin, đặc biệt khi phát triển ứng dụng Android, lint code giúp đảm bảo chất lượng mã nguồn
  và tuân thủ các tiêu chuẩn lập trình.

### Các công cụ phổ biến cho lint code bao gồm:

**klint**

- Là công cụ lint và formatter cho Kotlin, giúp kiểm tra và định dạng mã nguồn theo các quy tắc
  chuẩn của Kotlin.
- Có thể tự động sửa chữa một số lỗi đơn giản.
- Dễ tích hợp vào các dự án thông qua Gradle

**Detekt**

- Công cụ phân tích mã tĩnh cho Kotlin, tập trung vào chất lượng mã và phát hiện "code smell" (các
  đoạn mã có vấn đề tiềm ẩn).
- Cung cấp báo cáo chi tiết và hỗ trợ tích hợp vào quy trình CI/CD.

**Android Lint**

- Công cụ lint tích hợp sẵn trong Android Studio, hỗ trợ cả Kotlin và Java.
- Tập trung vào các vấn đề liên quan đến Android như hiệu suất, bảo mật và tính tương thích.

**Plugin ktmft**

- Định dạng mã nguồn: ktfmt tự động sắp xếp lại mã Kotlin (ví dụ: thụt lề, xuống dòng, khoảng cách)
  theo các quy tắc chuẩn.
- Nhất quán phong cách: Đảm bảo mọi thành viên trong nhóm hoặc dự án sử dụng cùng một phong cách mã
  hóa (code style), tránh sự khác biệt do thói quen cá nhân.
- Tự động hóa: Giảm công sức chỉnh sửa định dạng thủ công, giúp lập trình viên tập trung vào logic
  thay vì hình thức.

### Nhận xét:

- Dùng plugin ktmft chỉ có tác dụng làm sạch mã nguồn nhưng không có tác dụng kiểm tra các lỗi
  logic.
- Phương pháp dùng tốt nhất là ktlint.

### Tích hợp ktlint:

**Bước 1: Cài đặt plugin ktlint**

- Mở IntelliJ IDEA hoặc Android Studio.
- Vào Settings/Preferences > Plugins.
- Trong tab Marketplace, tìm kiếm "ktlint".
- Cài đặt plugin ktlint và khởi động lại IDE.

**Bước 2: Cấu hình plugin**

- Sau khi cài đặt, plugin sẽ tự động nhận diện các file Kotlin trong dự án.
- Vào Settings/Preferences > Tools > ktlint.
- Bật các tùy chọn như "Enable ktlint" hoặc "Format on save" (định dạng khi lưu file).

**Bước 3: Sử dụng plugin**

- Kiểm tra mã: Nhấp chuột phải vào file hoặc thư mục > ktlint > Check with ktlint.
- Định dạng mã: Nhấp chuột phải > ktlint > Format with ktlint.
- Nếu bật "Format on save", mã sẽ tự động được định dạng mỗi khi lưu file.