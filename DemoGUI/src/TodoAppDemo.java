import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TodoAppDemo extends JFrame {
    // Các thành phần GUI
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel taskPanel;
    private JTextField taskInput;
    private JTextArea descriptionArea;
    private JButton addButton;
    private JButton clearButton;
    private JComboBox<String> priorityCombo;
    private JRadioButton personalRadio;
    private JRadioButton workRadio;
    private JRadioButton studyRadio;
    private ButtonGroup categoryGroup;
    private JCheckBox reminderCheck;
    private JSpinner dateSpinner;
    private JTabbedPane tabbedPane;
    private JPanel allTasksPanel;
    private JPanel activeTasksPanel;
    private JPanel completedTasksPanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JTextField searchField;
    private ArrayList<Task> tasks;
    private JLabel statusLabel;
    private JPanel filterPanel;
    private JComboBox<String> filterCombo;
    private Color[] priorityColors = {
        new Color(255, 102, 102), // Cao (đỏ nhạt)
        new Color(255, 204, 102), // Trung bình (cam nhạt)
        new Color(102, 204, 102)  // Thấp (xanh lá nhạt)
    };
    
    // Constants
    private static final String[] PRIORITIES = {"Cao", "Trung bình", "Thấp"};
    private static final String[] CATEGORIES = {"Tất cả", "Cá nhân", "Công việc", "Học tập"};
    private static final int MAX_TASKS = 100;

    public TodoAppDemo() {
        setTitle("Ứng dụng Quản lý Công việc");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Khởi tạo danh sách nhiệm vụ
        tasks = new ArrayList<>();
        
        // Khởi tạo các thành phần GUI
        initComponents();
        
        // Tạo menu và toolbar
        createMenuAndToolbar();
        
        // Bố trí các thành phần bằng Layout Manager
        setupLayout();
        
        // Đăng ký các sự kiện
        registerEvents();
        
        // Thêm một số công việc mẫu
        addSampleTasks();
    }
    
    // Khởi tạo các thành phần GUI
    private void initComponents() {
        mainPanel = new JPanel();
        inputPanel = new JPanel();
        taskPanel = new JPanel();
        
        // Tạo các thành phần input
        taskInput = new JTextField(20);
        taskInput.setToolTipText("Nhập tên công việc tại đây");
        
        // TextArea cho mô tả
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createTitledBorder("Mô tả chi tiết"));
        
        // Các nút thao tác
        addButton = new JButton("Thêm");
        addButton.setToolTipText("Thêm công việc mới");
        
        clearButton = new JButton("Xóa form");
        clearButton.setToolTipText("Xóa tất cả dữ liệu đã nhập");

        // ComboBox cho mức độ ưu tiên
        priorityCombo = new JComboBox<>(PRIORITIES);
        priorityCombo.setRenderer(new PriorityListCellRenderer());
        
        // RadioButton cho phân loại
        personalRadio = new JRadioButton("Cá nhân", true);
        workRadio = new JRadioButton("Công việc");
        studyRadio = new JRadioButton("Học tập");
        categoryGroup = new ButtonGroup();
        categoryGroup.add(personalRadio);
        categoryGroup.add(workRadio);
        categoryGroup.add(studyRadio);

        // Checkbox cho nhắc nhở
        reminderCheck = new JCheckBox("Đặt nhắc nhở");
        
        // Date spinner cho ngày hạn
        SpinnerDateModel dateModel = new SpinnerDateModel(
            new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(
            dateSpinner, "dd/MM/yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);
        
        // Tabbed Pane để phân loại công việc
        tabbedPane = new JTabbedPane();
        activeTasksPanel = new JPanel();
        completedTasksPanel = new JPanel();
        
        activeTasksPanel.setLayout(new BoxLayout(activeTasksPanel, BoxLayout.Y_AXIS));
        completedTasksPanel.setLayout(new BoxLayout(completedTasksPanel, BoxLayout.Y_AXIS));
        
        tabbedPane.addTab("Đang thực hiện", new JScrollPane(activeTasksPanel));
        tabbedPane.addTab("Đã hoàn thành", new JScrollPane(completedTasksPanel));
        
        // Panel tìm kiếm và lọc
        filterPanel = new JPanel();
        searchField = new JTextField(15);
        searchField.setToolTipText("Tìm kiếm công việc");
        filterCombo = new JComboBox<>(CATEGORIES);
        
        // Status bar
        statusLabel = new JLabel("Sẵn sàng | Tổng số công việc: 0");
    }
    
    // Tạo menu và toolbar
    private void createMenuAndToolbar() {
        // Menu
        menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Tệp");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem newItem = new JMenuItem("Mới", KeyEvent.VK_N);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newItem.addActionListener(e -> clearAllTasks());
        
        JMenuItem exportItem = new JMenuItem("Xuất ra tệp", KeyEvent.VK_E);
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        exportItem.addActionListener(e -> exportTasks());
        
        JMenuItem exitItem = new JMenuItem("Thoát", KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu editMenu = new JMenu("Chỉnh sửa");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem deleteAllItem = new JMenuItem("Xóa tất cả", KeyEvent.VK_D);
        deleteAllItem.addActionListener(e -> clearAllTasks());
        
        editMenu.add(deleteAllItem);
        
        JMenu helpMenu = new JMenu("Trợ giúp");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem aboutItem = new JMenuItem("Giới thiệu", KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        // Toolbar
        toolBar = new JToolBar("Thanh công cụ");
        toolBar.setFloatable(false);
        
        JButton newButton = new JButton("Tạo mới");
        newButton.setToolTipText("Tạo mới");
        newButton.addActionListener(e -> clearAllTasks());
        
        JButton deleteAllButton = new JButton("Xóa tất cả");
        deleteAllButton.setToolTipText("Xóa tất cả");
        deleteAllButton.addActionListener(e -> clearAllTasks());
        
        JButton exportButton = new JButton("Xuất");
        exportButton.setToolTipText("Xuất ra tệp");
        exportButton.addActionListener(e -> exportTasks());
        
        toolBar.add(newButton);
        toolBar.add(deleteAllButton);
        toolBar.addSeparator();
        toolBar.add(exportButton);
        
        add(toolBar, BorderLayout.NORTH);
    }
    
    // Thiết lập layout cho ứng dụng
    private void setupLayout() {
        // Sử dụng BorderLayout cho frame chính
        setLayout(new BorderLayout(5, 5));
        
        // Tạo panel chính với khoảng cách giữa các thành phần
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel nhập liệu sử dụng GridBagLayout
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thêm công việc mới"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Thêm JLabel và JTextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Tên công việc:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        inputPanel.add(taskInput, gbc);
        
        // Thêm JTextArea cho mô tả
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Mô tả:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        JScrollPane scrollDesc = new JScrollPane(descriptionArea);
        scrollDesc.setPreferredSize(new Dimension(300, 80));
        inputPanel.add(scrollDesc, gbc);
        
        // Reset gridheight
        gbc.gridheight = 1;
        
        // Thêm JComboBox cho độ ưu tiên
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Độ ưu tiên:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        inputPanel.add(priorityCombo, gbc);
        
        // Thêm JSpinner cho ngày hạn
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Hạn chót:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        inputPanel.add(dateSpinner, gbc);
        
        // Thêm RadioButton
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Phân loại:", SwingConstants.RIGHT), gbc);
        
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        categoryPanel.add(personalRadio);
        categoryPanel.add(workRadio);
        categoryPanel.add(studyRadio);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        inputPanel.add(categoryPanel, gbc);
        
        // Thêm Checkbox
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Tùy chọn:", SwingConstants.RIGHT), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        inputPanel.add(reminderCheck, gbc);
        
        // Thêm Button trong một panel riêng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        inputPanel.add(buttonPanel, gbc);
        
        // Thiết lập panel tìm kiếm và lọc
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        filterPanel.add(new JLabel("Tìm kiếm:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Lọc theo:"));
        filterPanel.add(filterCombo);
        
        // Khởi tạo TabbedPane và thiết lập border
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Thêm các thành phần vào main panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(filterPanel, BorderLayout.SOUTH);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Thêm status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        // Thêm vào frame chính
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
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
        
        // Sự kiện cho nút Xóa form
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        // Sự kiện khi nhấn Enter trong ô nhập liệu
        taskInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        
        // Sự kiện cho ô tìm kiếm
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTasks();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTasks();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTasks();
            }
        });
        
        // Sự kiện cho combobox lọc
        filterCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTasks();
            }
        });
        
        // Sự kiện khi chuyển tab
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateStatusBar();
            }
        });
    }
    
    // Phương thức thêm công việc mới
    private void addTask() {
        String taskText = taskInput.getText().trim();
        if (taskText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên công việc!", 
                "Lỗi", 
                JOptionPane.WARNING_MESSAGE);
            taskInput.requestFocus();
            return;
        }
        
        // Lấy thông tin từ các thành phần
        String description = descriptionArea.getText().trim();
        String priority = (String) priorityCombo.getSelectedItem();
        
        String category = "Cá nhân";
        if (workRadio.isSelected()) {
            category = "Công việc";
        } else if (studyRadio.isSelected()) {
            category = "Học tập";
        }
        
        boolean hasReminder = reminderCheck.isSelected();
        Date dueDate = (Date) dateSpinner.getValue();
        
        // Tạo task mới
        Task task = new Task(taskText, description, priority, category, hasReminder, dueDate);
        tasks.add(task);
        
        // Tạo panel hiển thị task
        JPanel taskItemPanel = createTaskPanel(task);
        
        // Thêm vào tab tương ứng
        activeTasksPanel.add(taskItemPanel);
        
        // Cập nhật giao diện
        updatePanels();
        
        // Cập nhật thanh trạng thái
        updateStatusBar();
        
        // Xóa nội dung trong ô nhập liệu
        clearForm();
        
        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(this,
            "Đã thêm công việc mới thành công!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Tạo panel hiển thị một công việc
    private JPanel createTaskPanel(Task task) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Đổi màu nền dựa trên độ ưu tiên
        Color bgColor = Color.WHITE;
        if (task.getPriority().equals("Cao")) {
            bgColor = priorityColors[0];
        } else if (task.getPriority().equals("Trung bình")) {
            bgColor = priorityColors[1];
        } else {
            bgColor = priorityColors[2];
        }
        panel.setBackground(bgColor);
        
        // Tạo panel chứa thông tin task
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setBackground(bgColor);
        
        // Tạo label hiển thị tiêu đề task
        JLabel titleLabel = new JLabel(task.getText());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        if (task.isCompleted()) {
            titleLabel.setText("<html><s>" + task.getText() + "</s></html>");
            titleLabel.setForeground(Color.GRAY);
        }
        
        // Tạo label hiển thị mô tả task
        JLabel descLabel = new JLabel();
        if (!task.getDescription().isEmpty()) {
            descLabel.setText("<html><p>" + task.getDescription() + "</p></html>");
            descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        
        // Tạo label hiển thị thông tin phụ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        StringBuilder detailsBuilder = new StringBuilder("<html><font size='2'>");
        detailsBuilder.append("<b>Phân loại:</b> ").append(task.getCategory()).append(" | ");
        detailsBuilder.append("<b>Độ ưu tiên:</b> ").append(task.getPriority()).append(" | ");
        detailsBuilder.append("<b>Thời hạn:</b> ").append(dateFormat.format(task.getDueDate()));
        
        if (task.hasReminder()) {
            detailsBuilder.append(" | <font color='blue'><b>Có nhắc nhở</b></font>");
        }
        
        detailsBuilder.append("</font></html>");
        
        JLabel detailsLabel = new JLabel(detailsBuilder.toString());
        
        // Thêm các thành phần vào info panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(bgColor);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        infoPanel.add(titlePanel, BorderLayout.NORTH);
        if (!task.getDescription().isEmpty()) {
            infoPanel.add(descLabel, BorderLayout.CENTER);
        }
        infoPanel.add(detailsLabel, BorderLayout.SOUTH);
        
        // Tạo panel chứa các nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(bgColor);
        
        // Tạo nút hoàn thành task
        JButton doneButton = new JButton("Hoàn thành");
        doneButton.setEnabled(!task.isCompleted());
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeTask(task, panel, doneButton, titleLabel);
            }
        });
        
        // Tạo nút chỉnh sửa task
        JButton editButton = new JButton("Sửa");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTask(task);
            }
        });
        
        // Tạo nút xóa task
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask(task, panel);
            }
        });
        
        // Thêm các nút vào button panel
        buttonPanel.add(doneButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Thêm các panel vào panel chính
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    // Đánh dấu một công việc là hoàn thành
    private void completeTask(Task task, JPanel taskPanel, JButton doneButton, JLabel titleLabel) {
        task.setCompleted(true);
        
        titleLabel.setText("<html><s>" + task.getText() + "</s></html>");
        titleLabel.setForeground(Color.GRAY);
        doneButton.setEnabled(false);
        
        // Cập nhật các panel
        activeTasksPanel.remove(taskPanel);
        if (!completedTasksPanel.isAncestorOf(taskPanel)) {
            completedTasksPanel.add(taskPanel);
        }
        
        updatePanels();
        updateStatusBar();
    }
    
    // Xóa một công việc
    private void deleteTask(Task task, JPanel taskPanel) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa công việc này?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            tasks.remove(task);
            
            activeTasksPanel.remove(taskPanel);
            completedTasksPanel.remove(taskPanel);
            
            updatePanels();
            updateStatusBar();
        }
    }
    
    // Chỉnh sửa một công việc
    private void editTask(Task task) {
        // Tạo dialog để chỉnh sửa task
        JDialog editDialog = new JDialog(this, "Chỉnh sửa công việc", true);
        editDialog.setLayout(new BorderLayout(10, 10));
        
        // Tạo form chỉnh sửa
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Text field cho tên công việc
        JTextField editTaskInput = new JTextField(task.getText(), 25);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tên công việc:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(editTaskInput, gbc);
        
        // Text area cho mô tả
        JTextArea editDescArea = new JTextArea(task.getDescription(), 3, 25);
        editDescArea.setLineWrap(true);
        editDescArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(editDescArea);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(descScrollPane, gbc);
        
        // Combobox cho độ ưu tiên
        JComboBox<String> editPriorityCombo = new JComboBox<>(PRIORITIES);
        editPriorityCombo.setSelectedItem(task.getPriority());
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Độ ưu tiên:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(editPriorityCombo, gbc);
        
        // Radio buttons cho phân loại
        JRadioButton editPersonalRadio = new JRadioButton("Cá nhân");
        JRadioButton editWorkRadio = new JRadioButton("Công việc");
        JRadioButton editStudyRadio = new JRadioButton("Học tập");
        
        ButtonGroup editCategoryGroup = new ButtonGroup();
        editCategoryGroup.add(editPersonalRadio);
        editCategoryGroup.add(editWorkRadio);
        editCategoryGroup.add(editStudyRadio);
        
        // Chọn radio button mặc định dựa trên category
        if (task.getCategory().equals("Công việc")) {
            editWorkRadio.setSelected(true);
        } else if (task.getCategory().equals("Học tập")) {
            editStudyRadio.setSelected(true);
        } else {
            editPersonalRadio.setSelected(true);
        }
        
        JPanel editCategoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        editCategoryPanel.add(editPersonalRadio);
        editCategoryPanel.add(editWorkRadio);
        editCategoryPanel.add(editStudyRadio);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phân loại:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(editCategoryPanel, gbc);
        
        // Date spinner cho ngày hạn
        SpinnerDateModel editDateModel = new SpinnerDateModel(
            task.getDueDate(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner editDateSpinner = new JSpinner(editDateModel);
        JSpinner.DateEditor editDateEditor = new JSpinner.DateEditor(
            editDateSpinner, "dd/MM/yyyy HH:mm");
        editDateSpinner.setEditor(editDateEditor);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Thời hạn:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(editDateSpinner, gbc);
        
        // Checkbox cho nhắc nhở
        JCheckBox editReminderCheck = new JCheckBox("Đặt nhắc nhở", task.hasReminder());
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tùy chọn:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(editReminderCheck, gbc);
        
        // Panel chứa các nút
        JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Lưu");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cập nhật thông tin task
                if (editTaskInput.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog,
                        "Tên công việc không được để trống!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                task.setText(editTaskInput.getText().trim());
                task.setDescription(editDescArea.getText().trim());
                task.setPriority((String) editPriorityCombo.getSelectedItem());
                
                if (editWorkRadio.isSelected()) {
                    task.setCategory("Công việc");
                } else if (editStudyRadio.isSelected()) {
                    task.setCategory("Học tập");
                } else {
                    task.setCategory("Cá nhân");
                }
                
                task.setDueDate((Date) editDateSpinner.getValue());
                task.setReminder(editReminderCheck.isSelected());
                
                // Cập nhật giao diện
                filterTasks();
                
                // Đóng dialog
                editDialog.dispose();
            }
        });
        
        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
        
        editButtonPanel.add(saveButton);
        editButtonPanel.add(cancelButton);
        
        // Thêm tất cả vào dialog
        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(editButtonPanel, BorderLayout.SOUTH);
        
        editDialog.pack();
        editDialog.setLocationRelativeTo(this);
        editDialog.setResizable(false);
        editDialog.setVisible(true);
    }
    
    // Lớp Task lưu trữ thông tin công việc
    private class Task {
        private String text;
        private String description;
        private String priority;
        private String category;
        private boolean hasReminder;
        private boolean completed;
        private Date dueDate;
        
        public Task(String text, String description, String priority, String category, boolean hasReminder, Date dueDate) {
            this.text = text;
            this.description = description;
            this.priority = priority;
            this.category = category;
            this.hasReminder = hasReminder;
            this.completed = false;
            this.dueDate = dueDate;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getPriority() {
            return priority;
        }
        
        public void setPriority(String priority) {
            this.priority = priority;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public boolean hasReminder() {
            return hasReminder;
        }
        
        public void setReminder(boolean hasReminder) {
            this.hasReminder = hasReminder;
        }
        
        public boolean isCompleted() {
            return completed;
        }
        
        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
        
        public Date getDueDate() {
            return dueDate;
        }
        
        public void setDueDate(Date dueDate) {
            this.dueDate = dueDate;
        }
    }
    
    // Lớp renderer tùy chỉnh cho combobox độ ưu tiên
    private class PriorityListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            String priority = (String) value;
            
            if (priority.equals("Cao")) {
                setForeground(Color.RED);
            } else if (priority.equals("Trung bình")) {
                setForeground(Color.ORANGE.darker());
            } else {
                setForeground(new Color(0, 153, 0));
            }
            
            return this;
        }
    }
    
    // Cập nhật tất cả các panel
    private void updatePanels() {
        activeTasksPanel.revalidate();
        activeTasksPanel.repaint();
        completedTasksPanel.revalidate();
        completedTasksPanel.repaint();
    }
    
    // Xóa nội dung form nhập liệu
    private void clearForm() {
        taskInput.setText("");
        descriptionArea.setText("");
        priorityCombo.setSelectedIndex(0);
        personalRadio.setSelected(true);
        reminderCheck.setSelected(false);
        dateSpinner.setValue(new Date());
        taskInput.requestFocus();
    }
    
    // Lọc danh sách công việc theo từ khóa tìm kiếm và bộ lọc
    private void filterTasks() {
        String searchText = searchField.getText().toLowerCase().trim();
        String filterCategory = (String) filterCombo.getSelectedItem();
        
        // Xóa tất cả các panel hiện tại
        activeTasksPanel.removeAll();
        completedTasksPanel.removeAll();
        
        // Thêm lại các task phù hợp với điều kiện lọc
        for (Task task : tasks) {
            // Kiểm tra điều kiện tìm kiếm
            boolean matchesSearch = task.getText().toLowerCase().contains(searchText) || 
                                  task.getDescription().toLowerCase().contains(searchText);
            
            // Kiểm tra điều kiện lọc danh mục
            boolean matchesCategory = filterCategory.equals("Tất cả") || 
                                    task.getCategory().equals(filterCategory);
            
            if (matchesSearch && matchesCategory) {
                JPanel panel = createTaskPanel(task);
                
                // Thêm vào tab tương ứng trạng thái
                if (task.isCompleted()) {
                    completedTasksPanel.add(panel);
                } else {
                    activeTasksPanel.add(panel);
                }
            }
        }
        
        // Cập nhật giao diện
        updatePanels();
        updateStatusBar();
    }
    
    // Cập nhật thanh trạng thái
    private void updateStatusBar() {
        int totalTasks = tasks.size();
        int completedTasks = 0;
        
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks++;
            }
        }
        
        int activeTasks = totalTasks - completedTasks;
        
        // Cập nhật text cho status bar
        statusLabel.setText(String.format("Tổng số: %d | Đang thực hiện: %d | Đã hoàn thành: %d", 
                                         totalTasks, activeTasks, completedTasks));
    }
    
    // Thêm một số công việc mẫu
    private void addSampleTasks() {
        // Tạo công việc mẫu 1
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Task task1 = new Task(
            "Hoàn thành bài tập Java Swing", 
            "Hoàn thiện ứng dụng Quản lý công việc cho bài thuyết trình", 
            "Cao", 
            "Học tập", 
            true,
            cal.getTime()
        );
        tasks.add(task1);
        
        // Tạo công việc mẫu 2
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Task task2 = new Task(
            "Mua sắm cuối tuần", 
            "Đi siêu thị mua thực phẩm và đồ dùng cá nhân", 
            "Trung bình", 
            "Cá nhân", 
            false,
            cal.getTime()
        );
        tasks.add(task2);
        
        // Tạo công việc mẫu đã hoàn thành
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Task task3 = new Task(
            "Gửi email báo cáo", 
            "Gửi báo cáo tiến độ công việc cho quản lý", 
            "Cao", 
            "Công việc", 
            true,
            cal.getTime()
        );
        task3.setCompleted(true);
        tasks.add(task3);
        
        // Cập nhật giao diện - Hiển thị tất cả các nhiệm vụ trong giao diện
        for (Task task : tasks) {
            JPanel taskItemPanel = createTaskPanel(task);
            
            // Thêm vào tab tương ứng trạng thái
            if (task.isCompleted()) {
                completedTasksPanel.add(taskItemPanel);
            } else {
                activeTasksPanel.add(taskItemPanel);
            }
        }
        
        // Cập nhật giao diện
        updatePanels();
        updateStatusBar();
    }
    
    // Xóa tất cả công việc
    private void clearAllTasks() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa tất cả công việc?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            tasks.clear();
            activeTasksPanel.removeAll();
            completedTasksPanel.removeAll();
            updatePanels();
            updateStatusBar();
            
            JOptionPane.showMessageDialog(this,
                "Đã xóa tất cả công việc!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Xuất danh sách công việc
    private void exportTasks() {
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất danh sách công việc đang được phát triển!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Hiển thị dialog giới thiệu
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "Giới thiệu", true);
        aboutDialog.setLayout(new BorderLayout(10, 10));
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Ứng dụng Quản lý Công việc");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTextArea infoArea = new JTextArea(
            "Ứng dụng demo minh họa cho bài thuyết trình Chương 5 - GUI trong Java\n\n" +
            "Các thành phần được sử dụng:\n" +
            "- JFrame, JPanel, JLabel, JTextField, JTextArea\n" +
            "- JButton, JCheckBox, JRadioButton, JComboBox\n" +
            "- JTabbedPane, JMenuBar, JToolBar, JSpinner\n" +
            "- Các loại Layout: BorderLayout, GridBagLayout, FlowLayout, BoxLayout\n" +
            "- Xử lý sự kiện: ActionListener, DocumentListener, ChangeListener\n\n"
        );
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBackground(contentPanel.getBackground());
        
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> aboutDialog.dispose());
        
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        contentPanel.add(closeButton, BorderLayout.SOUTH);
        
        aboutDialog.add(contentPanel);
        aboutDialog.setSize(500, 350);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setResizable(false);
        aboutDialog.setVisible(true);
    }
} 