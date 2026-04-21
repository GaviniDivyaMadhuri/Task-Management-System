package com.taskflow.ui;

import com.taskflow.dao.TaskDAO;
import com.taskflow.model.Task;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TaskListPanel extends JPanel {

    private final TaskDAO dao;
    private final Frame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> filterBox;

    private static final String[] COLS = {"#", "Task Title", "Category", "Priority", "Status", "Due Date", "Overdue"};

    public TaskListPanel(TaskDAO dao, Frame parentFrame) {
        this.dao = dao;
        this.parentFrame = parentFrame;
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(25, 25, 25, 25));
        build();
    }

    private void build() {
        add(topBar(), BorderLayout.NORTH);
        add(tablePanel(), BorderLayout.CENTER);
        add(actionBar(), BorderLayout.SOUTH);
        refresh();
    }

    private JPanel topBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JLabel title = new JLabel("Task Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 30, 60));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        filterBox = new JComboBox<>(new String[]{"ALL", "PENDING", "IN_PROGRESS", "COMPLETED"});
        filterBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterBox.addActionListener(e -> refresh());

        JButton addBtn = styledButton("+ Add Task", new Color(67, 97, 238), Color.WHITE);
        addBtn.addActionListener(e -> openForm(null));

        right.add(new JLabel("Filter: "));
        right.add(filterBox);
        right.add(addBtn);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JScrollPane tablePanel() {
        model = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 205, 230), 1));
        scroll.getViewport().setBackground(new Color(252, 253, 255));
        return scroll;
    }

    private JPanel actionBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);

        JButton editBtn   = styledButton("Edit",      new Color(76, 175, 80),  Color.WHITE);
        JButton deleteBtn = styledButton("Delete",    new Color(239, 83, 80),  Color.WHITE);
        JButton doneBtn   = styledButton("Mark Done", new Color(33, 150, 243), Color.WHITE);

        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
        doneBtn.addActionListener(e -> markDone());

        bar.add(editBtn);
        bar.add(deleteBtn);
        bar.add(doneBtn);
        return bar;
    }

    public void refresh() {
        model.setRowCount(0);
        String filter = (String) filterBox.getSelectedItem();
        List<Task> tasks = "ALL".equals(filter) ? dao.getAllTasks() : dao.getByStatus(filter);
        for (Task t : tasks) {
            model.addRow(new Object[]{
                t.getId(), t.getTitle(), t.getCategory(),
                t.getPriority(), t.getStatus(),
                t.getDueDate() != null ? t.getDueDate().toString() : "-",
                t.isOverdue() ? "YES" : "No"
            });
        }
        colorRows();
    }

    private void openForm(Task existing) {
        TaskFormDialog dlg = new TaskFormDialog(parentFrame, existing);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            if (existing == null) dao.addTask(dlg.getTask());
            else dao.updateTask(dlg.getTask());
            refresh();
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showNoSelection(); return; }
        int id = (int) model.getValueAt(row, 0);
        Task t = dao.getAllTasks().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if (t != null) openForm(t);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showNoSelection(); return; }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this task?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) { dao.deleteTask(id); refresh(); }
    }

    private void markDone() {
        int row = table.getSelectedRow();
        if (row < 0) { showNoSelection(); return; }
        int id = (int) model.getValueAt(row, 0);
        dao.getAllTasks().stream().filter(x -> x.getId() == id).findFirst().ifPresent(t -> {
            t.setStatus("COMPLETED");
            dao.updateTask(t);
            refresh();
        });
    }

    private void showNoSelection() {
        JOptionPane.showMessageDialog(this, "Please select a task first.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleTable() {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 227, 255));
        table.setSelectionForeground(new Color(20, 20, 60));
        table.setBackground(new Color(252, 253, 255));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(50, 60, 110));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                lbl.setBackground(new Color(50, 60, 110));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lbl.setBorder(new EmptyBorder(0, 12, 0, 8));
                lbl.setOpaque(true);
                return lbl;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        table.getColumnModel().getColumn(0).setMaxWidth(45);
        table.getColumnModel().getColumn(0).setMinWidth(45);
        table.getColumnModel().getColumn(6).setMaxWidth(80);
        table.getColumnModel().getColumn(6).setMinWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
    }

    private void colorRows() {
        // Default renderer for all columns
        DefaultTableCellRenderer baseRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                c.setBorder(new EmptyBorder(0, 12, 0, 8));
                c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                c.setForeground(new Color(25, 25, 50));
                if (sel) {
                    c.setBackground(new Color(219, 227, 255));
                    c.setForeground(new Color(20, 20, 60));
                } else {
                    String overdue = (String) model.getValueAt(row, 6);
                    String status  = (String) model.getValueAt(row, 4);
                    if ("YES".equals(overdue))             c.setBackground(new Color(255, 241, 242));
                    else if ("COMPLETED".equals(status))   c.setBackground(new Color(240, 253, 244));
                    else if ("IN_PROGRESS".equals(status)) c.setBackground(new Color(255, 252, 235));
                    else if (row % 2 == 0)                 c.setBackground(new Color(247, 248, 255));
                    else                                   c.setBackground(Color.WHITE);
                }
                return c;
            }
        };

        // Badge renderer for Priority column (col 3)
        TableCellRenderer priorityRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(String.valueOf(val));
                lbl.setOpaque(true);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(new EmptyBorder(4, 10, 4, 10));
                String p = String.valueOf(val);
                switch (p) {
                    case "HIGH"   -> { lbl.setBackground(new Color(255, 235, 238)); lbl.setForeground(new Color(183, 28, 28)); }
                    case "MEDIUM" -> { lbl.setBackground(new Color(255, 248, 225)); lbl.setForeground(new Color(230, 81, 0)); }
                    case "LOW"    -> { lbl.setBackground(new Color(232, 245, 233)); lbl.setForeground(new Color(27, 94, 32)); }
                    default       -> { lbl.setBackground(Color.WHITE); lbl.setForeground(Color.DARK_GRAY); }
                }
                if (sel) { lbl.setBackground(new Color(219, 227, 255)); lbl.setForeground(new Color(20, 20, 60)); }
                return lbl;
            }
        };

        // Badge renderer for Status column (col 4)
        TableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(String.valueOf(val).replace("_", " "));
                lbl.setOpaque(true);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
                String s = String.valueOf(val);
                switch (s) {
                    case "COMPLETED"  -> { lbl.setBackground(new Color(209, 250, 229)); lbl.setForeground(new Color(6, 95, 70)); }
                    case "IN_PROGRESS"-> { lbl.setBackground(new Color(254, 243, 199)); lbl.setForeground(new Color(146, 64, 14)); }
                    case "PENDING"    -> { lbl.setBackground(new Color(224, 231, 255)); lbl.setForeground(new Color(55, 48, 163)); }
                    default           -> { lbl.setBackground(Color.WHITE); lbl.setForeground(Color.DARK_GRAY); }
                }
                if (sel) { lbl.setBackground(new Color(219, 227, 255)); lbl.setForeground(new Color(20, 20, 60)); }
                return lbl;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(baseRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(priorityRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);
    }

    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }
}
