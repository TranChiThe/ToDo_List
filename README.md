# #ToDo List

### Giới thiệu

Ứng dụng ToDo List được xây dựng bằng Kotlin, Jetpack Compose và ObjectBox. Đây là một ứng dụng quản lý công việc đơn giản, giúp người dùng tạo, chỉnh sửa, xóa và đánh dấu hoàn thành các công việc, ...

**Công nghệ sử dụng**

- Kotlin: Ngôn ngữ lập trình chính.

- Jetpack Compose: Thư viện UI hiện đại của Android.

- ObjectBox: Cơ sở dữ liệu NoSQL hiệu suất cao.

- ViewModel & Hilt: Quản lý trạng thái và vòng đời dữ liệu.

- Navigation Component: Điều hướng giữa các màn hình.

### Cài đặt

#### 1. Yêu cầu hệ thống

- Android Studio Dolphin trở lên

- JDK 11 hoặc mới hơn

- Kotlin 1.7+

- ObjectBox 3.5+

#### 2. Cách chạy ứng dụng

**Clone repo**:

- git clone https://github.com/TranChiThe/ToDo_List.git
- cd ToDo_List

Mở dự án bằng Android Studio.

Đồng bộ dependencies.

Chạy ứng dụng trên thiết bị ảo hoặc thật.

**Các tính năng chính**

- Thêm công việc: Nhập tiêu đề và mô tả để tạo task mới.

- Sửa công việc: Chỉnh sửa thông tin của task.

- Xóa công việc: Xóa task với xác nhận trước khi thực hiện.

- Đánh dấu hoàn thành: Toggle trạng thái task giữa hoàn thành và chưa hoàn thành.

- Xem lịch: Chọn ngày bất kì muốn xem, hệ thống hiển thị tất cả các task bắt đầu từ ngày được chọn (nếu có task được tạo).

- Tìm kiếm: Nhập title của task vào ô tìm kiếm để tìm task tương ứng.

- Yêu thích task: Click vào heart icon để thêm 1 task vào danh sách yêu thích và có thể xem lại danh sách các task được thêm vào yêu thích.

- Lưu trữ dữ liệu cục bộ: Sử dụng ObjectBox để lưu trữ.
