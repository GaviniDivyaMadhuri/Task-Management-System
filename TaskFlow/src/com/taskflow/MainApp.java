package com.taskflow;

import com.taskflow.dao.TaskDAO;
import com.taskflow.ui.MainWindow;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            TaskDAO dao = new TaskDAO();
            MainWindow window = new MainWindow(dao);
            window.setVisible(true);
        });
    }
}
