package com.taskflow.ui;

import com.taskflow.dao.TaskDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {

    private final TaskDAO dao;
    private JPanel contentArea;
    private DashboardPanel dashboardPanel;
    private TaskListPanel taskListPanel;
    private JButton activeNav = null;

    private static final Color BG_DARK   = new Color(15, 23, 42);
    private static final Color BG_SIDE   = new Color(20, 30, 58);
    private static final Color ACCENT    = new Color(99, 102, 241);
    private static final Color ACCENT2   = new Color(139, 92, 246);

    public MainWindow(TaskDAO dao) {
        this.dao = dao;
        setTitle("TaskFlow  —  Task Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 720);
        setMinimumSize(new Dimension(960, 600));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(248, 249, 255));
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);

        contentArea    = new JPanel(new CardLayout());
        dashboardPanel = new DashboardPanel(dao);
        taskListPanel  = new TaskListPanel(dao, this);
        contentArea.add(dashboardPanel, "DASHBOARD");
        contentArea.add(taskListPanel,  "TASKS");
        contentArea.setBackground(new Color(248, 249, 255));

        root.add(contentArea, BorderLayout.CENTER);
        setContentPane(root);
    }

    // ── Gradient Header ──────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG_DARK, getWidth(), 0, new Color(30, 27, 75));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(new EmptyBorder(0, 24, 0, 24));
        header.setOpaque(false);

        // Logo area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);

        JLabel dot = new JLabel("  ");
        dot.setOpaque(true);
        dot.setBackground(ACCENT);
        dot.setPreferredSize(new Dimension(6, 32));

        JLabel logoText = new JLabel("  TaskFlow");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoText.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("  |  Task Management System");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tagline.setForeground(new Color(148, 163, 184));

        logoPanel.add(dot);
        logoPanel.add(logoText);
        logoPanel.add(tagline);

        // Right info
        JLabel info = new JLabel("Java Swing  •  Software Developer Intern Project  ");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(new Color(100, 116, 139));

        header.add(logoPanel, BorderLayout.WEST);
        header.add(info,      BorderLayout.EAST);
        return header;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BG_SIDE);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(24, 0, 24, 0));
        sidebar.setOpaque(false);

        JLabel menuLabel = new JLabel("  NAVIGATION");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        menuLabel.setForeground(new Color(71, 85, 105));
        menuLabel.setBorder(new EmptyBorder(0, 20, 12, 0));
        menuLabel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(menuLabel);

        JButton dashBtn = navItem("Dashboard",  "DASHBOARD", true);
        JButton taskBtn = navItem("My Tasks",    "TASKS",     false);
        activeNav = dashBtn;

        sidebar.add(dashBtn);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(taskBtn);
        sidebar.add(Box.createVerticalGlue());

        // Bottom badge
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        badge.setOpaque(false);
        badge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel ver = new JLabel("v1.0.0  —  Intern Build");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ver.setForeground(new Color(51, 65, 85));
        badge.add(ver);
        sidebar.add(badge);
        return sidebar;
    }

    private JButton navItem(String text, String card, boolean active) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getBackground().equals(ACCENT)) {
                    GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), 0, ACCENT2);
                    g2.setPaint(gp);
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 10, 10);
                }
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(active ? Color.WHITE : new Color(148, 163, 184));
        btn.setBackground(active ? ACCENT : BG_SIDE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            ((CardLayout) contentArea.getLayout()).show(contentArea, card);
            if ("TASKS".equals(card)) taskListPanel.refresh();
            if ("DASHBOARD".equals(card)) {
                contentArea.remove(dashboardPanel);
                dashboardPanel = new DashboardPanel(dao);
                contentArea.add(dashboardPanel, "DASHBOARD");
                ((CardLayout) contentArea.getLayout()).show(contentArea, "DASHBOARD");
            }
            if (activeNav != null) {
                activeNav.setBackground(BG_SIDE);
                activeNav.setForeground(new Color(148, 163, 184));
                activeNav.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
            btn.setBackground(ACCENT);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            activeNav = btn;
            btn.repaint();
        });

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeNav) btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeNav) btn.setForeground(new Color(148, 163, 184));
            }
        });
        return btn;
    }
}
