import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class TodoAppDemo extends JFrame {
    // Các thành phần GUI
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel taskPanel;
    private JTextField taskInput;
    private JButton addButton;
    private JComboBox<String> priorityCombo;
    private JRadioButton personalRadio;
    private JRadioButton workRadio;
    private ButtonGroup categoryGroup;
    private JCheckBox reminderCheck;
    private ArrayList<Task> tasks;

    public TodoAppDemo() {
        setTitle("Ứng dụng Quản lý Công việc");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Khởi tạo danh sách nhiệm vụ
        tasks = new ArrayList<>();
        
        // Khởi tạo các thành phần GUI
        initComponents();
        
        // Bố trí các thành phần bằng Layout Manager
        setupLayout();
        
        // Đăng ký các sự kiện
        registerEvents();
    }
    
    // Khởi tạo các thành phần GUI
    private void initComponents() {
        mainPanel = new JPanel();
        inputPanel = new JPanel();
        taskPanel = new JPanel();

        // Tạo các thành phần input
        taskInput = new JTextField(20);
        addButton = new JButton("Thêm");

        // ComboBox cho mức độ ưu tiên
        priorityCombo = new JComboBox<>(new String[]{"Cao", "Trung bình", "Thấp"});
        
        // RadioButton cho phân loại
        personalRadio = new JRadioButton("Cá nhân", true);
        workRadio = new JRadioButton("Công việc");
        categoryGroup = new ButtonGroup();
        categoryGroup.add(personalRadio);
        categoryGroup.add(workRadio);

        // Checkbox cho nhắc nhở
        reminderCheck = new JCheckBox("Nhắc nhở");
    }
    
    // Thiết lập layout cho ứng dụng
    private void setupLayout() {
        // Sử dụng BorderLayout cho frame chính
        setLayout(new BorderLayout());
        
        // Thiết lập panel nhập liệu sử dụng GridBagLayout
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Thêm JLabel và JTextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Công việc:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 3;
        inputPanel.add(taskInput, gbc);
        
        // Thêm JComboBox cho độ ưu tiên
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Độ ưu tiên:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(priorityCombo, gbc);
        
        // Thêm RadioButton
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Phân loại:"), gbc);
        
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(personalRadio);
        categoryPanel.add(workRadio);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(categoryPanel, gbc);
        
        // Thêm Checkbox
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Tùy chọn:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(reminderCheck, gbc);
        
        // Thêm Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);
        
        // Thiết lập panel hiển thị danh sách công việc
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Thêm JScrollPane để cuộn khi có nhiều công việc
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Đặt các panel vào frame chính
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel thông tin phía dưới
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Demo ứng dụng Java Swing - Minh họa cho bài thuyết trình Chương 5"));
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    // Đăng ký các sự kiện
    private void registerEvents() {
        // Sự kiện cho nút Thêm
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        
        // Sự kiện khi nhấn Enter trong ô nhập liệu
        taskInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
    }
    
    // Phương thức thêm công việc mới
    private void addTask() {
        String taskText = taskInput.getText().trim();
        if (taskText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập nội dung công việc!", 
                "Lỗi", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy thông tin từ các thành phần
        String priority = (String) priorityCombo.getSelectedItem();
        String category = personalRadio.isSelected() ? "Cá nhân" : "Công việc";
        boolean hasReminder = reminderCheck.isSelected();
        
        // Tạo task mới
        Task task = new Task(taskText, priority, category, hasReminder);
        tasks.add(task);
        
        // Tạo panel hiển thị task
        JPanel taskItemPanel = createTaskPanel(task);
        taskPanel.add(taskItemPanel);
        
        // Cập nhật giao diện
        taskPanel.revalidate();
        taskPanel.repaint();
        
        // Xóa nội dung trong ô nhập liệu
        taskInput.setText("");
        
        // Focus lại vào ô nhập liệu
        taskInput.requestFocus();
    }
    
    // Tạo panel hiển thị một công việc
    private JPanel createTaskPanel(Task task) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Tạo label hiển thị thông tin task
        String colorCode = task.getPriority().equals("Cao") ? "red" : 
                           task.getPriority().equals("Trung bình") ? "orange" : "green";
        
        StringBuilder htmlText = new StringBuilder();
        htmlText.append("<html><body>");
        htmlText.append("<span style='font-size: 14pt;'>").append(task.getText()).append("</span><br>");
        htmlText.append("<span style='color: ").append(colorCode).append("; font-size: 10pt;'>");
        htmlText.append("Độ ưu tiên: ").append(task.getPriority()).append("</span>");
        htmlText.append(" | <span style='font-size: 10pt;'>Phân loại: ").append(task.getCategory()).append("</span>");
        if (task.hasReminder()) {
            htmlText.append(" | <span style='color: blue; font-size: 10pt;'>Có nhắc nhở</span>");
        }
        htmlText.append("</body></html>");
        
        JLabel taskLabel = new JLabel(htmlText.toString());
        panel.add(taskLabel, BorderLayout.CENTER);
        
        // Tạo nút xóa task
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tasks.remove(task);
                taskPanel.remove(panel);
                taskPanel.revalidate();
                taskPanel.repaint();
            }
        });
        
        // Tạo nút hoàn thành task
        JButton doneButton = new JButton("Hoàn thành");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                task.setCompleted(true);
                taskLabel.setText(htmlText.toString().replace(task.getText(), 
                                   "<span style='text-decoration: line-through;'>" + task.getText() + "</span>"));
                doneButton.setEnabled(false);
            }
        });
        
        // Tạo panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(doneButton);
        buttonPanel.add(deleteButton);
        
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    // Lớp Task lưu trữ thông tin công việc
    private class Task {
        private String text;
        private String priority;
        private String category;
        private boolean hasReminder;
        private boolean completed;
        
        public Task(String text, String priority, String category, boolean hasReminder) {
            this.text = text;
            this.priority = priority;
            this.category = category;
            this.hasReminder = hasReminder;
            this.completed = false;
        }
        
        public String getText() {
            return text;
        }
        
        public String getPriority() {
            return priority;
        }
        
        public String getCategory() {
            return category;
        }
        
        public boolean hasReminder() {
            return hasReminder;
        }
        
        public boolean isCompleted() {
            return completed;
        }
        
        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
} 