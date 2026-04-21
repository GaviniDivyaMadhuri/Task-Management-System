package com.taskflow.ui;

import com.taskflow.model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TaskFormDialog extends JDialog {

    private JTextField titleField, descField, categoryField, dueDateField;
    private JComboBox<String> priorityBox, statusBox;
    private boolean confirmed = false;
    private Task task;

    public TaskFormDialog(Frame parent, Task existing) {
        super(parent, existing == null ? "Add New Task" : "Edit Task", true);
        this.task = existing;
        setSize(460, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
        if (existing != null) populate(existing);
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 12));
        form.setOpaque(false);

        titleField    = new JTextField();
        descField     = new JTextField();
        categoryField = new JTextField();
        dueDateField  = new JTextField(LocalDate.now().plusDays(3).toString());
        priorityBox   = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});
        statusBox     = new JComboBox<>(new String[]{"PENDING", "IN_PROGRESS", "COMPLETED"});

        form.add(label("Title *"));        form.add(titleField);
        form.add(label("Description"));    form.add(descField);
        form.add(label("Category"));       form.add(categoryField);
        form.add(label("Priority"));       form.add(priorityBox);
        form.add(label("Status"));         form.add(statusBox);
        form.add(label("Due Date (YYYY-MM-DD)")); form.add(dueDateField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton save   = styledButton("Save", new Color(67, 97, 238), Color.WHITE);
        JButton cancel = styledButton("Cancel", new Color(240, 240, 245), new Color(60, 60, 80));

        save.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());

        buttons.add(cancel);
        buttons.add(save);

        main.add(form, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);
        setContentPane(main);
    }

    private void onSave() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate due = null;
        try {
            if (!dueDateField.getText().trim().isEmpty())
                due = LocalDate.parse(dueDateField.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (task == null) task = new Task(title, descField.getText().trim(),
                (String) priorityBox.getSelectedItem(), (String) statusBox.getSelectedItem(),
                categoryField.getText().trim(), due);
        else {
            task.setTitle(title);
            task.setDescription(descField.getText().trim());
            task.setPriority((String) priorityBox.getSelectedItem());
            task.setStatus((String) statusBox.getSelectedItem());
            task.setCategory(categoryField.getText().trim());
            task.setDueDate(due);
        }
        confirmed = true;
        dispose();
    }

    private void populate(Task t) {
        titleField.setText(t.getTitle());
        descField.setText(t.getDescription());
        categoryField.setText(t.getCategory());
        priorityBox.setSelectedItem(t.getPriority());
        statusBox.setSelectedItem(t.getStatus());
        dueDateField.setText(t.getDueDate() != null ? t.getDueDate().toString() : "");
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 35));
        return btn;
    }

    public boolean isConfirmed() { return confirmed; }
    public Task getTask() { return task; }
}
