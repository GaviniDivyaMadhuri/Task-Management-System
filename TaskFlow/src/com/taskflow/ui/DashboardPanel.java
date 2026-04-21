package com.taskflow.ui;

import com.taskflow.dao.TaskDAO;
import com.taskflow.model.Task;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final TaskDAO dao;
    private static final Color PAGE_BG = new Color(248, 249, 255);

    public DashboardPanel(TaskDAO dao) {
        this.dao = dao;
        setBackground(PAGE_BG);
        setLayout(new BorderLayout(0, 0));
        build();
    }

    private void build() {
        JPanel inner = new JPanel(new BorderLayout(0, 20));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(28, 28, 28, 28));

        inner.add(pageHeader(),   BorderLayout.NORTH);
        inner.add(bodyPanel(),    BorderLayout.CENTER);
        add(inner, BorderLayout.CENTER);
    }

    // ── Page header ──────────────────────────────────────────────────────────
    private JPanel pageHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(15, 23, 42));

        JLabel sub = new JLabel("Welcome back! Here is your task summary.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 116, 139));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(title);
        left.add(sub);
        p.add(left, BorderLayout.WEST);
        return p;
    }

    // ── Body ─────────────────────────────────────────────────────────────────
    private JPanel bodyPanel() {
        long total     = dao.getAllTasks().size();
        long pending   = dao.countByStatus("PENDING");
        long inProg    = dao.countByStatus("IN_PROGRESS");
        long completed = dao.countByStatus("COMPLETED");
        long overdue   = dao.countOverdue();

        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setOpaque(false);
        body.add(statCards(total, pending, inProg, completed), BorderLayout.NORTH);
        body.add(bottomSection(overdue),                       BorderLayout.CENTER);
        return body;
    }

    // ── Stat cards ────────────────────────────────────────────────────────────
    private JPanel statCards(long total, long pending, long inProg, long completed) {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.add(statCard("Total Tasks",  String.valueOf(total),     new Color(99, 102, 241),  new Color(238, 242, 255)));
        row.add(statCard("In Progress",  String.valueOf(inProg),    new Color(245, 158, 11),  new Color(255, 251, 235)));
        row.add(statCard("Pending",      String.valueOf(pending),   new Color(239, 68, 68),   new Color(254, 242, 242)));
        row.add(statCard("Completed",    String.valueOf(completed), new Color(34, 197, 94),   new Color(240, 253, 244)));
        return row;
    }

    private JPanel statCard(String label, String value, Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // top color bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 5, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        // Circle icon
        JPanel circle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, 44, 44);
                g2.dispose();
            }
        };
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(44, 44));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 38));
        valLbl.setForeground(accent);

        JLabel nameLbl = new JLabel(label);
        nameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLbl.setForeground(new Color(100, 116, 139));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setOpaque(false);
        text.add(valLbl);
        text.add(nameLbl);

        card.add(text,   BorderLayout.CENTER);
        card.add(circle, BorderLayout.EAST);

        // Drop shadow effect via border
        card.setBorder(new CompoundBorder(
            new ShadowBorder(),
            new EmptyBorder(20, 22, 20, 22)
        ));
        return card;
    }

    // ── Bottom: alert + recent table ─────────────────────────────────────────
    private JPanel bottomSection(long overdue) {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setOpaque(false);
        p.add(alertBanner(overdue),   BorderLayout.NORTH);
        p.add(recentTasksPanel(),     BorderLayout.CENTER);
        return p;
    }

    private JPanel alertBanner(long count) {
        boolean bad = count > 0;
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bad ? new Color(254, 242, 242) : new Color(240, 253, 244));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(bad ? new Color(239, 68, 68) : new Color(34, 197, 94));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 18, 12, 18));

        String msg = bad
            ? "  " + count + " task(s) are overdue — please review them immediately."
            : "  All tasks are on track. Great work!";
        JLabel lbl = new JLabel((bad ? "[!]" : "[OK]") + msg);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(bad ? new Color(185, 28, 28) : new Color(21, 128, 61));
        panel.add(lbl);
        return panel;
    }

    private JPanel recentTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        JLabel title = new JLabel("Recent Tasks");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(new Color(15, 23, 42));

        List<Task> tasks = dao.getAllTasks();
        String[] cols = {"Task Title", "Category", "Priority", "Status", "Due Date"};
        Object[][] data = new Object[Math.min(tasks.size(), 6)][5];
        for (int i = 0; i < data.length; i++) {
            Task t = tasks.get(i);
            data[i][0] = t.getTitle();
            data[i][1] = t.getCategory() != null ? t.getCategory() : "-";
            data[i][2] = t.getPriority();
            data[i][3] = t.getStatus();
            data[i][4] = t.getDueDate() != null ? t.getDueDate().toString() : "-";
        }

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        applyTableStyle(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new ShadowBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void applyTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(30, 41, 59));
        table.setSelectionBackground(new Color(238, 242, 255));

        // Header
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                lbl.setBackground(new Color(15, 23, 42));
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setBorder(new EmptyBorder(0, 14, 0, 8));
                lbl.setOpaque(true);
                return lbl;
            }
        });

        // Cell renderer with badge for priority/status
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lbl.setForeground(new Color(30, 41, 59));
                lbl.setBorder(new EmptyBorder(0, 14, 0, 8));
                lbl.setOpaque(true);
                lbl.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 255));
                if (sel) lbl.setBackground(new Color(238, 242, 255));

                // Priority badge
                if (col == 2) {
                    String p = String.valueOf(v);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    lbl.setHorizontalAlignment(CENTER);
                    switch (p) {
                        case "HIGH"   -> { lbl.setBackground(new Color(254, 226, 226)); lbl.setForeground(new Color(185, 28, 28)); }
                        case "MEDIUM" -> { lbl.setBackground(new Color(255, 237, 213)); lbl.setForeground(new Color(194, 65, 12)); }
                        case "LOW"    -> { lbl.setBackground(new Color(220, 252, 231)); lbl.setForeground(new Color(21, 128, 61)); }
                    }
                }
                // Status badge
                if (col == 3) {
                    String s = String.valueOf(v);
                    lbl.setText(s.replace("_", " "));
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    lbl.setHorizontalAlignment(CENTER);
                    switch (s) {
                        case "COMPLETED"   -> { lbl.setBackground(new Color(220, 252, 231)); lbl.setForeground(new Color(21, 128, 61)); }
                        case "IN_PROGRESS" -> { lbl.setBackground(new Color(254, 243, 199)); lbl.setForeground(new Color(146, 64, 14)); }
                        case "PENDING"     -> { lbl.setBackground(new Color(238, 242, 255)); lbl.setForeground(new Color(79, 70, 229)); }
                    }
                }
                return lbl;
            }
        });
    }

    // ── Simple shadow border ──────────────────────────────────────────────────
    static class ShadowBorder extends AbstractBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(x + 2, y + 2, w - 2, h - 2, 14, 14);
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(x, y, w - 3, h - 3, 12, 12);
            g2.dispose();
        }
        public Insets getBorderInsets(Component c) { return new Insets(3, 3, 5, 5); }
    }
}
