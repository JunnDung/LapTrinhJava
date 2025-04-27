public class Main {
    public static void main(String[] args) {
        // Sử dụng SwingUtilities để đảm bảo ứng dụng chạy trong EDT
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Sử dụng look and feel của hệ thống
                    javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // Khởi tạo và hiển thị ứng dụng
                new TodoAppDemo().setVisible(true);
            }
        });
    }
}
