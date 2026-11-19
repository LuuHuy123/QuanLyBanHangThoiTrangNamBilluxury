package GUI;

import Human.HoaDon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class AppDialog {

    private static final Color BG = new Color(18, 17, 15);
    private static final Color CARD = new Color(33, 31, 29);
    private static final Color CARD_2 = new Color(42, 39, 35);
    private static final Color BORDER = new Color(112, 84, 52);
    private static final Color GOLD = new Color(192, 149, 94);
    private static final Color GOLD_LIGHT = new Color(223, 196, 162);
    private static final Color TEXT = new Color(245, 239, 230);
    private static final Color TEXT_MUTED = new Color(182, 172, 158);
    private static final Color SUCCESS = new Color(84, 160, 110);
    private static final Color WARNING = new Color(205, 157, 76);
    private static final Color ERROR = new Color(190, 78, 66);
    private static final int DETAIL_DIALOG_WIDTH = 860;
    private static final int DETAIL_CONTENT_WIDTH = 800;

    private AppDialog() {
    }

    public static void showSuccess(Component parent, String title, String message) {
        showMessageDialog(parent, title, message, StatusType.SUCCESS, "OK", null, false);
    }

    public static void showInfo(Component parent, String title, String message) {
        showMessageDialog(parent, title, message, StatusType.INFO, "OK", null, false);
    }

    public static void showWarning(Component parent, String title, String message) {
        showMessageDialog(parent, title, message, StatusType.WARNING, "Đã hiểu", null, false);
    }

    public static void showError(Component parent, String title, String message) {
        showMessageDialog(parent, title, message, StatusType.ERROR, "Đóng", null, false);
    }

    public static boolean showConfirm(Component parent, String title, String message) {
        final boolean[] accepted = {false};
        showMessageDialog(parent, title, message, StatusType.CONFIRM, "Đồng ý", () -> accepted[0] = true, true);
        return accepted[0];
    }

    private static void showMessageDialog(Component parent,
                                          String title,
                                          String message,
                                          StatusType type,
                                          String okText,
                                          Runnable confirmAction,
                                          boolean hasCancel) {

        JDialog dialog = createBaseDialog(parent, title, 620, hasCancel ? 300 : 280);

        JPanel root = (JPanel) dialog.getContentPane();
        root.setLayout(new BorderLayout());
        root.setBackground(BG);

        root.add(createHeader(dialog, title), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(22, 0));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(28, 28, 20, 28));

        JLabel icon = new JLabel(new StatusCircleIcon(type, 70));
        icon.setVerticalAlignment(SwingConstants.TOP);
        body.add(icon, BorderLayout.WEST);

        JLabel msg = new JLabel(toHtml(message, 19, 1.6, 420));
        msg.setForeground(TEXT);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        msg.setVerticalAlignment(SwingConstants.CENTER);
        body.add(msg, BorderLayout.CENTER);

        root.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 20, 24, 20));

        if (hasCancel) {
            JButton cancel = createSecondaryButton("Hủy");
            cancel.addActionListener(e -> dialog.dispose());
            footer.add(cancel);
        }

        JButton ok = createPrimaryButton(okText);
        ok.addActionListener(e -> {
            if (confirmAction != null) {
                confirmAction.run();
            }
            dialog.dispose();
        });
        footer.add(ok);

        root.add(footer, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void showHoaDonDetails(Component parent, HoaDon hd) {
        JDialog dialog = createBaseDialog(parent, "Chi tiết hóa đơn", DETAIL_DIALOG_WIDTH, 720);
        JPanel root = (JPanel) dialog.getContentPane();
        root.setLayout(new BorderLayout());
        root.setBackground(BG);

        root.add(createHeader(dialog, "Chi tiết hóa đơn · " + hd.getMaHoaDon()), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(18, 24, 12, 24));
        content.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 0));
        content.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, Integer.MAX_VALUE));

        JLabel caption = new JLabel("Thông tin chi tiết các sản phẩm trong hóa đơn");
        caption.setForeground(TEXT_MUTED);
        caption.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        caption.setAlignmentX(Component.LEFT_ALIGNMENT);
        caption.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, 24));
        content.add(caption);
        content.add(Box.createVerticalStrut(12));

        JPanel summary = new JPanel(new GridLayout(2, 2, 14, 14));
        summary.setOpaque(false);
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);
        summary.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 140));
        summary.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, 140));
        summary.add(createInfoCard("Mã khách hàng", safe(hd.getMaKhachHang())));
        summary.add(createInfoCard("Mã nhân viên", safe(hd.getMaNhanVien())));
        summary.add(createInfoCard("Ngày lập", hd.getNgayLap() != null
                ? hd.getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : ""));
        summary.add(createInfoCard("Trạng thái", safe(hd.getTrangThai())));
        content.add(summary);
        content.add(Box.createVerticalStrut(16));

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setOpaque(false);
        tableWrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(0, 0, 0, 0)
        ));
        tableWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableWrap.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 260));
        tableWrap.setMinimumSize(new Dimension(DETAIL_CONTENT_WIDTH, 260));
        tableWrap.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, 260));

        JPanel tableHeader = new JPanel(new GridLayout(1, 4));
        tableHeader.setBackground(new Color(53, 45, 36));
        tableHeader.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 42));
        tableHeader.add(createHeaderCell("Mã SP"));
        tableHeader.add(createHeaderCell("Số lượng"));
        tableHeader.add(createHeaderCell("Đơn giá"));
        tableHeader.add(createHeaderCell("Thành tiền"));
        tableWrap.add(tableHeader, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setOpaque(true);
        rows.setBackground(CARD);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        if (hd.getChiTiet().isEmpty()) {
            JPanel empty = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 50));
            empty.setOpaque(false);

            JLabel lbl = new JLabel("Hóa đơn chưa có chi tiết sản phẩm.");
            lbl.setForeground(TEXT_MUTED);
            lbl.setFont(new Font("Segoe UI", Font.ITALIC, 16));

            empty.add(lbl);
            rows.add(empty);
        } else {
            int index = 0;

            for (Map.Entry<String, double[]> entry : hd.getChiTiet().entrySet()) {
                String maSP = entry.getKey();
                double soLuong = entry.getValue()[0];
                double donGia = entry.getValue()[1];
                double thanhTien = soLuong * donGia;

                JPanel row = new JPanel(new GridLayout(1, 4));
                row.setBackground(index % 2 == 0 ? CARD : CARD_2);
                row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
                row.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 48));
                row.setMinimumSize(new Dimension(DETAIL_CONTENT_WIDTH, 48));
                row.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, 48));

                row.add(createBodyCell(maSP, false));
                row.add(createBodyCell(String.valueOf((int) soLuong), true));
                row.add(createBodyCell(formatMoney(donGia) + " VNĐ", true));
                row.add(createBodyCell(formatMoney(thanhTien) + " VNĐ", true));

                rows.add(row);
                index++;
            }
        }

        JScrollPane tableScroll = new JScrollPane(rows);
        tableScroll.setBorder(null);
        tableScroll.getViewport().setBackground(CARD);
        tableScroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        tableScroll.getHorizontalScrollBar().setUI(new DarkScrollBarUI());
        tableScroll.getVerticalScrollBar().setUnitIncrement(18);
        tableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tableScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        tableWrap.add(tableScroll, BorderLayout.CENTER);
        content.add(tableWrap);
        content.add(Box.createVerticalStrut(16));

        JPanel totalPanel = new JPanel(new GridLayout(1, 3, 14, 0));
        totalPanel.setOpaque(false);
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 88));
        totalPanel.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, 88));
        totalPanel.add(createTotalCard("Tổng tiền", formatMoney(hd.getTongTien()) + " VNĐ"));
        totalPanel.add(createTotalCard("Giảm giá", formatMoney(hd.getGiamGia()) + " VNĐ"));
        totalPanel.add(createTotalCard("Thanh toán", formatMoney(hd.getThanhToan()) + " VNĐ"));
        content.add(totalPanel);

        if (hd.getGhiChu() != null && !hd.getGhiChu().trim().isEmpty()) {
            content.add(Box.createVerticalStrut(16));

            JPanel note = new JPanel(new BorderLayout(0, 8));
            note.setOpaque(true);
            note.setBackground(CARD);
            note.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    new EmptyBorder(12, 14, 12, 14)
            ));

            JLabel title = new JLabel("Ghi chú");
            title.setForeground(GOLD_LIGHT);
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel text = new JLabel(toHtml(hd.getGhiChu(), 14, 1.4, 760));
            text.setForeground(TEXT_MUTED);
            text.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            note.add(title, BorderLayout.NORTH);
            note.add(text, BorderLayout.CENTER);
            note.setAlignmentX(Component.LEFT_ALIGNMENT);
            note.setPreferredSize(new Dimension(DETAIL_CONTENT_WIDTH, 95));
            note.setMaximumSize(new Dimension(DETAIL_CONTENT_WIDTH, 95));

            content.add(note);
        }

        JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.setBorder(null);
        contentScroll.getViewport().setBackground(BG);
        contentScroll.setOpaque(false);
        contentScroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        contentScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScroll.getVerticalScrollBar().setUnitIncrement(18);

        root.add(contentScroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(10, 20, 16, 20));

        JButton close = createPrimaryButton("Đóng");
        close.setPreferredSize(new Dimension(150, 42));
        close.setMinimumSize(new Dimension(150, 42));
        close.setMaximumSize(new Dimension(150, 42));
        close.setBackground(new Color(214, 174, 117));
        close.setForeground(new Color(28, 20, 10));
        close.setOpaque(true);
        close.setContentAreaFilled(true);
        close.setBorderPainted(true);
        close.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 201, 151), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        close.addActionListener(e -> dialog.dispose());

        footer.add(close);
        root.add(footer, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static JPanel createInfoCard(String label, String value) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(label);
        lblTitle.setForeground(TEXT_MUTED);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(TEXT);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 15));

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblValue);
        return panel;
    }

    private static JPanel createTotalCard(String label, String value) {
        JPanel panel = createInfoCard(label, value);
        Component comp = panel.getComponent(2);
        if (comp instanceof JLabel lbl) {
            lbl.setForeground(GOLD_LIGHT);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }
        return panel;
    }

    private static JLabel createHeaderCell(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setForeground(GOLD_LIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBorder(new EmptyBorder(10, 6, 10, 6));
        return lbl;
    }

    private static JLabel createBodyCell(String text, boolean centered) {
        JLabel lbl = new JLabel(text, centered ? SwingConstants.CENTER : SwingConstants.LEFT);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setBorder(new EmptyBorder(9, 8, 9, 8));
        return lbl;
    }

    private static JPanel createHeader(JDialog dialog, String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(27, 24, 21));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(14, 18, 14, 12)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(TEXT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        header.add(lblTitle, BorderLayout.WEST);

        JButton close = new JButton(new CloseIcon(TEXT_MUTED, 16, 2.4f));
        close.setUI(new BasicButtonUI());
        close.setPreferredSize(new Dimension(34, 34));
        close.setFocusPainted(false);
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setOpaque(false);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dialog.dispose());
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                close.setIcon(new CloseIcon(GOLD_LIGHT, 16, 2.4f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setIcon(new CloseIcon(TEXT_MUTED, 16, 2.4f));
            }
        });
        header.add(close, BorderLayout.EAST);
        return header;
    }

    private static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new BasicButtonUI());
        btn.setPreferredSize(new Dimension(145, 38));
        btn.setMinimumSize(new Dimension(145, 38));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setForeground(new Color(28, 20, 10));
        btn.setBackground(new Color(214, 174, 117));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 201, 151), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(226, 188, 131));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(214, 174, 117));
            }
        });
        return btn;
    }

    private static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new BasicButtonUI());
        btn.setPreferredSize(new Dimension(148, 46));
        btn.setMinimumSize(new Dimension(148, 46));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setForeground(TEXT);
        btn.setBackground(CARD_2);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 18, 8, 18)
        ));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 55, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(CARD_2);
            }
        });
        return btn;
    }

    private static JDialog createBaseDialog(Component parent, String title, int width, int height) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = owner instanceof Frame
                ? new JDialog((Frame) owner, title, true)
                : new JDialog((Dialog) null, title, true);
        dialog.setUndecorated(true);
        dialog.setSize(width, height);
        dialog.setResizable(false);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(BORDER, 1));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static String formatMoney(double value) {
        return String.format("%,.0f", value);
    }

    private static String toHtml(String text, int fontSize, double lineHeight, int width) {
        String escaped = safe(text)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
        return "<html><div style='width:" + width + "px; font-size:" + fontSize + "px; line-height:" + lineHeight + ";'>" + escaped + "</div></html>";
    }

    private enum StatusType {
        SUCCESS, INFO, WARNING, ERROR, CONFIRM
    }

    private static class StatusCircleIcon implements Icon {
        private final StatusType type;
        private final int size;

        StatusCircleIcon(StatusType type, int size) {
            this.type = type;
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color = switch (type) {
                case SUCCESS -> SUCCESS;
                case INFO -> GOLD;
                case WARNING, CONFIRM -> WARNING;
                case ERROR -> ERROR;
            };

            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 55));
            g2.fillOval(x, y, size, size);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.6f));
            g2.drawOval(x + 1, y + 1, size - 2, size - 2);

            switch (type) {
                case SUCCESS -> drawCheck(g2, x, y, size);
                case ERROR -> drawCross(g2, x, y, size, Color.WHITE, 3.2f);
                case INFO -> drawInfo(g2, x, y, size);
                case WARNING -> drawExclamation(g2, x, y, size);
                case CONFIRM -> drawQuestion(g2, x, y, size);
            }
            g2.dispose();
        }

        private void drawCheck(Graphics2D g2, int x, int y, int size) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D path = new Path2D.Double();
            path.moveTo(x + size * 0.28, y + size * 0.55);
            path.lineTo(x + size * 0.44, y + size * 0.70);
            path.lineTo(x + size * 0.74, y + size * 0.34);
            g2.draw(path);
        }

        private void drawInfo(Graphics2D g2, int x, int y, int size) {
            g2.setColor(Color.WHITE);
            g2.fillOval((int) (x + size * 0.46), (int) (y + size * 0.22), (int) (size * 0.09), (int) (size * 0.09));
            g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine((int) (x + size * 0.5), (int) (y + size * 0.38), (int) (x + size * 0.5), (int) (y + size * 0.68));
        }

        private void drawExclamation(Graphics2D g2, int x, int y, int size) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine((int) (x + size * 0.5), (int) (y + size * 0.24), (int) (x + size * 0.5), (int) (y + size * 0.58));
            g2.fillOval((int) (x + size * 0.46), (int) (y + size * 0.71), (int) (size * 0.08), (int) (size * 0.08));
        }

        private void drawQuestion(Graphics2D g2, int x, int y, int size) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D path = new Path2D.Double();
            path.moveTo(x + size * 0.34, y + size * 0.30);
            path.curveTo(x + size * 0.38, y + size * 0.18, x + size * 0.62, y + size * 0.18, x + size * 0.66, y + size * 0.33);
            path.curveTo(x + size * 0.69, y + size * 0.45, x + size * 0.56, y + size * 0.48, x + size * 0.50, y + size * 0.58);
            g2.draw(path);
            g2.fillOval((int) (x + size * 0.46), (int) (y + size * 0.72), (int) (size * 0.08), (int) (size * 0.08));
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static class CloseIcon implements Icon {
        private final Color color;
        private final int size;
        private final float stroke;

        CloseIcon(Color color, int size, float stroke) {
            this.color = color;
            this.size = size;
            this.stroke = stroke;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 2, y + 2, x + size - 2, y + size - 2);
            g2.drawLine(x + size - 2, y + 2, x + 2, y + size - 2);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static class DarkScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(130, 96, 59);
            this.trackColor = new Color(28, 25, 22);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }

    private static void drawCross(Graphics2D g2, int x, int y, int size, Color color, float stroke) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine((int) (x + size * 0.32), (int) (y + size * 0.32), (int) (x + size * 0.68), (int) (y + size * 0.68));
        g2.drawLine((int) (x + size * 0.68), (int) (y + size * 0.32), (int) (x + size * 0.32), (int) (y + size * 0.68));
    }
}
