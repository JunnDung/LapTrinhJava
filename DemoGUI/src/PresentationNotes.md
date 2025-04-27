# Chương 5 - Graphic User Interface (GUI) trong Java
## Ghi chú thuyết trình với ứng dụng TodoList Demo

### I. Giới thiệu về GUI trong Java

#### 1. GUI là gì?
- **Định nghĩa**: GUI (Giao diện đồ họa người dùng) là phương tiện tương tác trực quan giữa người dùng và máy tính thông qua các thành phần đồ họa như cửa sổ, nút bấm, menu, biểu tượng...
- **Demo**: Khi bạn mở ứng dụng TodoList, bạn đang tương tác với một GUI gồm các thành phần như cửa sổ (JFrame), nút bấm (JButton), ô nhập liệu (JTextField)...

#### 2. Lịch sử phát triển ngắn gọn
- **CLI (Command Line Interface)**: Giao diện dòng lệnh, người dùng phải nhớ và gõ các câu lệnh
- **GUI**: Trực quan, dễ sử dụng, sử dụng chuột và bàn phím để tương tác
- **Cú hích**: Phát triển bởi Xerox PARC (1970s), phổ biến với Apple Macintosh (1984) và Windows

#### 3. Tầm quan trọng của GUI
- **Khả năng tiếp cận**: Giúp người dùng không chuyên có thể sử dụng phần mềm
- **Trực quan hóa**: Giúp người dùng dễ dàng hiểu và tương tác với chương trình
- **Trải nghiệm người dùng (UX)**: Nâng cao sự hài lòng và hiệu quả khi sử dụng
- **Ví dụ**: So sánh việc quản lý công việc bằng dòng lệnh và ứng dụng TodoList

#### 4. Các thư viện GUI phổ biến trong Java
- **AWT (Abstract Window Toolkit)**: Thư viện gốc, dựa trên nền tảng
- **Swing**: Xây dựng trên AWT, thuần Java, nhẹ hơn, tùy biến tốt hơn
- **JavaFX**: Thế hệ mới, hiệu ứng đồ họa tốt, hỗ trợ CSS, FXML
- **SWT (Standard Widget Toolkit)**: Sử dụng bởi Eclipse, hiệu năng tốt, tích hợp với hệ thống

### II. Tổng quan các thư viện xây dựng GUI trong Java

#### 1. Lịch sử phát triển GUI trong Java
- **AWT (JDK 1.0, 1995)**: Thư viện đầu tiên, sử dụng các thành phần gốc của hệ điều hành
- **Swing (JDK 1.2, 1998)**: Nâng cấp của AWT, viết bằng thuần Java
- **JavaFX (2008)**: Ban đầu là dự án riêng, sau đó được tích hợp vào JDK từ Java 8

#### 2. Ưu điểm và nhược điểm của các thư viện
- **AWT**:
  * **Ưu điểm**: Nhanh, đơn giản, tích hợp với hệ thống
  * **Nhược điểm**: Hạn chế về tùy biến, khác nhau trên các nền tảng

- **Swing**:
  * **Ưu điểm**: Nhất quán giữa các nền tảng, nhiều thành phần, tùy biến cao
  * **Nhược điểm**: Chậm hơn AWT, giao diện không hiện đại

- **JavaFX**:
  * **Ưu điểm**: Hiện đại, hỗ trợ hiệu ứng, đa phương tiện, CSS
  * **Nhược điểm**: Cong học, tài liệu ít hơn, cần JRE mới

#### 3. So sánh các thư viện (bảng)
| Tiêu chí | AWT | Swing | JavaFX |
|----------|-----|-------|--------|
| Loại | Nặng | Nhẹ | Hiện đại |
| Thành phần | Hạn chế | Phong phú | Phong phú + Đa phương tiện |
| Hiệu năng | Tốt | Trung bình | Tốt |
| Giao diện | Phụ thuộc nền tảng | Độc lập nền tảng | Độc lập + CSS |
| Tùy biến | Thấp | Cao | Rất cao |
| Hỗ trợ | Đầy đủ | Đầy đủ | Phát triển |

#### 4. Khi nào nên sử dụng thư viện nào
- **AWT**: Ứng dụng đơn giản, yêu cầu hiệu năng cao, tích hợp với hệ thống
- **Swing**: Ứng dụng desktop truyền thống, cần chạy trên nhiều nền tảng
- **JavaFX**: Ứng dụng hiện đại, đa phương tiện, giao diện đẹp

### III. Các thành phần cơ bản trong Swing
> *DEMO: Trong ứng dụng TodoList, mở từng thành phần và giải thích*

#### 1. JFrame
- Cửa sổ chính của ứng dụng
- Có thanh tiêu đề, nút đóng/thu nhỏ/phóng to
- Trong demo: `TodoAppDemo extends JFrame`

#### 2. JPanel
- Container chứa các thành phần khác
- Tổ chức giao diện theo nhóm logic
- Trong demo: `inputPanel`, `taskPanel`, `buttonPanel`

#### 3. JLabel
- Hiển thị văn bản, hình ảnh
- Không tương tác với người dùng
- Trong demo: "Công việc:", "Độ ưu tiên:", v.v.

#### 4. JTextField
- Ô nhập liệu văn bản một dòng
- Trong demo: `taskInput`

#### 5. JButton
- Nút bấm để thực hiện hành động
- Phát sinh sự kiện khi được nhấn
- Trong demo: `addButton`, `deleteButton`, `doneButton`

#### 6. JCheckBox và JRadioButton
- **JCheckBox**: Lựa chọn nhiều tùy chọn (có/không)
  * Trong demo: `reminderCheck`
- **JRadioButton**: Lựa chọn một trong nhiều tùy chọn
  * Cần đặt trong ButtonGroup
  * Trong demo: `personalRadio`, `workRadio` trong `categoryGroup`

#### 7. JComboBox
- Danh sách thả xuống cho phép chọn một mục
- Tiết kiệm không gian
- Trong demo: `priorityCombo` với các mức độ ưu tiên

#### 8. JTextArea
- Vùng văn bản nhiều dòng
- Không có trong demo, nhưng thường dùng cho ghi chú

### IV. Layout Managers
> *DEMO: Chỉ ra từng layout trong ứng dụng*

#### 1. FlowLayout
- **Đặc điểm**: Sắp xếp thành phần từ trái sang phải, trên xuống dưới
- **Trong demo**: Panel chứa các nút radio, panel thông tin

#### 2. BorderLayout
- **Đặc điểm**: Chia thành 5 vùng: North, South, East, West, Center
- **Trong demo**: Layout chính của JFrame, layout của task panel

#### 3. GridLayout
- **Đặc điểm**: Sắp xếp theo lưới đều các hàng và cột
- **Không có trong demo**: Nhưng thường dùng cho calculator, game board

#### 4. BoxLayout
- **Đặc điểm**: Sắp xếp thành phần theo chiều dọc hoặc ngang
- **Trong demo**: Panel chứa danh sách công việc

#### 5. CardLayout
- **Đặc điểm**: Hiển thị một thành phần tại một thời điểm (như bộ bài)
- **Không có trong demo**: Nhưng thường dùng cho wizard, tab panel

#### 6. GridBagLayout
- **Đặc điểm**: Layout phức tạp nhất, linh hoạt nhất
- **Trong demo**: Panel nhập liệu, cho phép điều chỉnh kích thước, vị trí chi tiết

### V. Xử lý sự kiện (Event Handling)
> *DEMO: Thêm một task và giải thích cơ chế xử lý sự kiện*

#### 1. Khái niệm về sự kiện
- **Sự kiện**: Hành động của người dùng hoặc hệ thống (nhấn nút, nhập text...)
- **Event Source**: Thành phần phát sinh sự kiện (JButton, JTextField...)
- **Event Listener**: Đối tượng lắng nghe và xử lý sự kiện

#### 2. Các loại sự kiện và Listener Interface
- **ActionEvent & ActionListener**: Nhấn nút, nhấn Enter...
  * Trong demo: `addButton.addActionListener(...)`
- **MouseEvent & MouseListener**: Sự kiện chuột (click, hover...)
- **KeyEvent & KeyListener**: Sự kiện bàn phím
- **WindowEvent & WindowListener**: Sự kiện cửa sổ

#### 3. Cơ chế lắng nghe sự kiện
- **Mô hình Observer**: Nguồn sự kiện - Người lắng nghe
- **Đăng ký listener**: Gắn listener vào thành phần GUI
- **Callback method**: Phương thức được gọi khi sự kiện xảy ra
- **Trong demo**: Xem phương thức `registerEvents()`

### VI. Các bước xây dựng một ứng dụng GUI Java
> *DEMO: Giải thích cấu trúc tổng thể của ứng dụng TodoList*

#### 1. Xác định yêu cầu của ứng dụng
- Mục tiêu: Quản lý danh sách công việc
- Chức năng: Thêm, xóa, đánh dấu hoàn thành
- Thông tin cần lưu trữ: Tên, độ ưu tiên, phân loại, nhắc nhở

#### 2. Thiết kế giao diện
- Bố cục chung: Input ở trên, danh sách ở dưới
- Các thành phần cần thiết: TextField, Button, RadioButton...
- Luồng tương tác: Nhập -> Chọn tùy chọn -> Thêm -> Hiển thị

#### 3. Lựa chọn các thành phần Swing phù hợp
- JTextField cho nhập tên công việc
- JComboBox cho mức độ ưu tiên
- JRadioButton cho phân loại
- JCheckBox cho nhắc nhở

#### 4. Sắp xếp các thành phần bằng Layout Managers
- BorderLayout cho tổng thể
- GridBagLayout cho panel nhập liệu
- BoxLayout cho danh sách công việc

#### 5. Implement xử lý sự kiện
- ActionListener cho các nút
- Xử lý logic khi thêm, xóa, đánh dấu hoàn thành

### Tổng kết
- **Swing**: Mạnh mẽ, linh hoạt, đủ thành phần cho hầu hết ứng dụng
- **Layout Management**: Quan trọng để tạo giao diện chuyên nghiệp
- **Event Handling**: Trụ cột của tương tác người dùng
- **Java GUI**: Còn phổ biến cho ứng dụng desktop doanh nghiệp

### Câu hỏi thảo luận
1. Làm thế nào để tùy biến giao diện Swing đẹp hơn?
2. Khi nào nên chọn JavaFX thay vì Swing?
3. Làm thế nào để thiết kế giao diện responsive với Swing?
4. Làm thế nào để tối ưu hiệu năng của ứng dụng GUI? 