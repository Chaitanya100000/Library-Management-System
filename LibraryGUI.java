
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class LibraryGUI extends JFrame {

    private final Library library;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    private JLabel totalValue;
    private JLabel availableValue;
    private JLabel issuedValue;
    private JLabel genresValue;
    private JLabel resultLabel;

    private JButton dashboardBtn, addBtn, searchBtn, viewBtn, issueBtn, returnBtn, removeBtn;

    private static final Color NAVY = new Color(18, 42, 88);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BG = new Color(246, 249, 253);
    private static final Color TEXT = new Color(25, 42, 78);
    private static final Color MUTED = new Color(91, 111, 145);
    private static final Color BORDER = new Color(222, 230, 240);

    public LibraryGUI() {
        library = new Library();

        setTitle("Library Management System");
        setSize(1500, 900);
        setMinimumSize(new Dimension(1200, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        showAllBooks();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMain(), BorderLayout.CENTER);

        setContentPane(root);
    }

    // ================= SIDEBAR =================

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(290, 0));
        sidebar.setBackground(NAVY);
        sidebar.setBorder(new EmptyBorder(30, 14, 24, 14));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel logoIcon = new JLabel("LIBRARY");
        logoIcon.setForeground(new Color(93, 179, 255));
        logoIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel brand = new JLabel(
                "<html><b>Library</b><br>Management System</html>"
        );
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));

        top.add(logoIcon);
        top.add(Box.createVerticalStrut(5));
        top.add(brand);
        top.add(Box.createVerticalStrut(38));

        dashboardBtn = navButton("Dashboard", true);
        addBtn = navButton("Add Book", false);
        searchBtn = navButton("Search Book", false);
        viewBtn = navButton("View All Books", false);
        issueBtn = navButton("Issue Book", false);
        returnBtn = navButton("Return Book", false);
        removeBtn = navButton("Remove Book", false);

        dashboardBtn.addActionListener(e -> {
            activate(dashboardBtn);
            showAllBooks();
        });

        addBtn.addActionListener(e -> {
            activate(addBtn);
            showAddBookDialog();
        });

        searchBtn.addActionListener(e -> {
            activate(searchBtn);
            showSearchDialog();
        });

        viewBtn.addActionListener(e -> {
            activate(viewBtn);
            showAllBooks();
        });

        issueBtn.addActionListener(e -> {
            activate(issueBtn);
            showIssueDialog();
        });

        returnBtn.addActionListener(e -> {
            activate(returnBtn);
            showReturnDialog();
        });

        removeBtn.addActionListener(e -> {
            activate(removeBtn);
            showRemoveDialog();
        });

        top.add(dashboardBtn);
        top.add(Box.createVerticalStrut(7));
        top.add(addBtn);
        top.add(Box.createVerticalStrut(7));
        top.add(searchBtn);
        top.add(Box.createVerticalStrut(7));
        top.add(viewBtn);
        top.add(Box.createVerticalStrut(7));
        top.add(issueBtn);
        top.add(Box.createVerticalStrut(7));
        top.add(returnBtn);
        top.add(Box.createVerticalStrut(7));
        top.add(removeBtn);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JSeparator line = new JSeparator();
        line.setForeground(new Color(67, 91, 135));

        JButton exit = navButton("Exit", false);
        exit.addActionListener(e -> confirmExit());

        bottom.add(line);
        bottom.add(Box.createVerticalStrut(14));
        bottom.add(exit);

        sidebar.add(top, BorderLayout.NORTH);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton navButton(String text, boolean active) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(active ? BLUE : NAVY);
        b.setBorder(new EmptyBorder(14, 22, 14, 10));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        return b;
    }

    private void activate(JButton active) {
        JButton[] buttons = {
                dashboardBtn, addBtn, searchBtn,
                viewBtn, issueBtn, returnBtn, removeBtn
        };

        for (JButton b : buttons) {
            b.setBackground(b == active ? BLUE : NAVY);
        }
    }

    // ================= MAIN =================

    private JPanel createMain() {
        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(28, 30, 25, 30));

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);

        body.add(createStats(), BorderLayout.NORTH);
        body.add(createBookPanel(), BorderLayout.CENTER);
        body.add(createQuickActions(), BorderLayout.SOUTH);

        main.add(body, BorderLayout.CENTER);
        return main;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setOpaque(false);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome to Library Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Manage books, issue and return books easily");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(MUTED);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(subtitle);

        JPanel right = new JPanel(new GridLayout(1, 2, 12, 0));
        right.setOpaque(false);

        String day = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE"));
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        right.add(infoCard("DATE", day + "  •  " + date));
        right.add(infoCard("LIBRARIAN", "Admin"));

        header.add(text, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel infoCard(String small, String value) {
        RoundedPanel p = new RoundedPanel(Color.WHITE, 18);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12, 18, 12, 18));

        JLabel a = new JLabel(small);
        a.setFont(new Font("Segoe UI", Font.BOLD, 11));
        a.setForeground(MUTED);

        JLabel b = new JLabel(value);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(TEXT);

        p.add(a);
        p.add(Box.createVerticalStrut(4));
        p.add(b);
        return p;
    }

    // ================= STAT CARDS =================

    private JPanel createStats() {
        JPanel p = new JPanel(new GridLayout(1, 4, 16, 0));
        p.setOpaque(false);

        RoundedPanel total = statCard(
                "Total Books", "0", "All books in library",
                new Color(231, 242, 255), BLUE
        );

        RoundedPanel available = statCard(
                "Available Books", "0", "Ready to issue",
                new Color(229, 249, 239), new Color(17, 145, 91)
        );

        RoundedPanel issued = statCard(
                "Issued Books", "0", "Currently issued",
                new Color(255, 240, 224), new Color(234, 113, 18)
        );

        RoundedPanel genres = statCard(
                "Total Genres", "0", "Different categories",
                new Color(243, 235, 255), new Color(124, 58, 237)
        );

        totalValue = findValueLabel(total);
        availableValue = findValueLabel(available);
        issuedValue = findValueLabel(issued);
        genresValue = findValueLabel(genres);

        p.add(total);
        p.add(available);
        p.add(issued);
        p.add(genres);

        return p;
    }

    private RoundedPanel statCard(
            String title, String value, String note,
            Color bg, Color accent) {

        RoundedPanel card = new RoundedPanel(bg, 20);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(TEXT);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 34));
        v.setForeground(TEXT);

        JLabel n = new JLabel(note);
        n.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        n.setForeground(MUTED);

        text.add(t);
        text.add(Box.createVerticalStrut(4));
        text.add(v);
        text.add(Box.createVerticalStrut(2));
        text.add(n);

        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.BOLD, 28));
        dot.setForeground(accent);

        card.add(text, BorderLayout.CENTER);
        card.add(dot, BorderLayout.EAST);

        return card;
    }

    private JLabel findValueLabel(RoundedPanel card) {
        JPanel p = (JPanel) card.getComponent(0);
        return (JLabel) p.getComponent(2);
    }

    // ================= BOOK TABLE =================

    private JPanel createBookPanel() {
        RoundedPanel panel = new RoundedPanel(Color.WHITE, 20);
        panel.setLayout(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(18, 18, 15, 18));

        JPanel top = new JPanel(new BorderLayout(15, 0));
        top.setOpaque(false);

        JLabel heading = new JLabel("Library Books");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(TEXT);

        JPanel search = new JPanel(new BorderLayout(8, 0));
        search.setOpaque(false);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(430, 42));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 13, 8, 13)
        ));
        searchField.setToolTipText("Search by ID, title, author or genre");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refreshTable(searchField.getText().trim());
            }
        });

        JButton refresh = modernButton("Refresh", BLUE);
        refresh.addActionListener(e -> showAllBooks());

        search.add(searchField, BorderLayout.CENTER);
        search.add(refresh, BorderLayout.EAST);

        top.add(heading, BorderLayout.WEST);
        top.add(search, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);

        String[] columns = {
                "Book ID", "Title", "Author", "Genre",
                "Status", "Issued To", "Actions"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(44);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(TEXT);
        table.setGridColor(new Color(235, 239, 246));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(231, 240, 255));
        table.setSelectionForeground(TEXT);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setBackground(new Color(244, 248, 253));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);

        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

        table.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ActionEditor());

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(scroll, BorderLayout.CENTER);

        resultLabel = new JLabel(" ");
        resultLabel.setForeground(MUTED);
        resultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(resultLabel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= QUICK ACTIONS =================

    private JPanel createQuickActions() {
        JPanel p = new JPanel(new GridLayout(1, 5, 12, 0));
        p.setOpaque(false);

        p.add(quick("Add New Book", "Add a book to library",
                BLUE, e -> {
                    activate(addBtn);
                    showAddBookDialog();
                }));

        p.add(quick("Search Book", "Find books quickly",
                new Color(16, 145, 92), e -> {
                    activate(searchBtn);
                    showSearchDialog();
                }));

        p.add(quick("Issue Book", "Issue a book to student",
                new Color(239, 143, 0), e -> {
                    activate(issueBtn);
                    showIssueDialog();
                }));

        p.add(quick("Return Book", "Return an issued book",
                new Color(124, 58, 237), e -> {
                    activate(returnBtn);
                    showReturnDialog();
                }));

        p.add(quick("Remove Book", "Delete a book from library",
                new Color(220, 53, 69), e -> {
                    activate(removeBtn);
                    showRemoveDialog();
                }));

        return p;
    }

    private JButton quick(String title, String note, Color color,
                          ActionListener action) {

        JButton b = new JButton(
                "<html><b>" + title + "</b><br>" +
                "<font size='2'>" + note + "</font></html>"
        );

        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBorder(new EmptyBorder(13, 16, 13, 10));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(action);

        return b;
    }

    private JButton modernButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ================= SEARCH =================

    private void showSearchDialog() {
        JTextField input = new JTextField();

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel label = new JLabel("Enter Book ID, title, author or genre:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));

        p.add(label, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this, p, "Search Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String keyword = input.getText().trim();

            if (keyword.isEmpty()) {
                error("Please enter something to search.");
                return;
            }

            searchField.setText(keyword);
            refreshTable(keyword);
            activate(searchBtn);
        }
    }

    private void showAllBooks() {
        searchField.setText("");
        refreshTable("");
        activate(viewBtn);
    }

    // ================= ADD =================

    private void showAddBookDialog() {
        JTextField id = new JTextField();
        JTextField title = new JTextField();
        JTextField author = new JTextField();
        JTextField genre = new JTextField();

        JPanel p = formPanel();
        p.add(formLabel("Book ID")); p.add(id);
        p.add(formLabel("Title")); p.add(title);
        p.add(formLabel("Author")); p.add(author);
        p.add(formLabel("Genre")); p.add(genre);

        int result = JOptionPane.showConfirmDialog(
                this, p, "Add New Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            int bookId = Integer.parseInt(id.getText().trim());

            if (title.getText().trim().isEmpty()
                    || author.getText().trim().isEmpty()
                    || genre.getText().trim().isEmpty()) {
                error("Please fill all fields.");
                return;
            }

            for (Book b : library.getBooks()) {
                if (b.getBookId() == bookId) {
                    error("Book ID " + bookId + " already exists.");
                    return;
                }
            }

            library.addBook(new Book(
                    bookId,
                    title.getText().trim(),
                    author.getText().trim(),
                    genre.getText().trim()
            ));

            showAllBooks();
            success("Book added successfully.");

        } catch (NumberFormatException ex) {
            error("Book ID must be a valid number.");
        }
    }

    // ================= ISSUE =================

    private void showIssueDialog() {
        JTextField id = new JTextField();
        JTextField student = new JTextField();

        JPanel p = formPanel();
        p.add(formLabel("Book ID")); p.add(id);
        p.add(formLabel("Student Name")); p.add(student);

        int result = JOptionPane.showConfirmDialog(
                this, p, "Issue Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            int bookId = Integer.parseInt(id.getText().trim());
            String studentName = student.getText().trim();

            if (studentName.isEmpty()) {
                error("Please enter student name.");
                return;
            }

            Book book = find(bookId);

            if (book == null) {
                error("Book with ID " + bookId + " was not found.");
                return;
            }

            if (book.isIssued()) {
                error("This book is already issued to " + book.getIssuedTo() + ".");
                return;
            }

            library.issueBook(bookId, studentName);
            showAllBooks();
            success("Book issued successfully to " + studentName + ".");

        } catch (NumberFormatException ex) {
            error("Book ID must be a valid number.");
        } catch (BookNotFoundException ex) {
            error(ex.getMessage());
        }
    }

    // ================= RETURN =================

    private void showReturnDialog() {
        String input = JOptionPane.showInputDialog(
                this, "Enter Book ID:",
                "Return Book", JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            Book book = find(id);

            if (book == null) {
                error("Book with ID " + id + " was not found.");
                return;
            }

            if (!book.isIssued()) {
                error("This book is already available.");
                return;
            }

            library.returnBook(id);
            showAllBooks();
            success("Book returned successfully.");

        } catch (NumberFormatException ex) {
            error("Book ID must be a valid number.");
        } catch (BookNotFoundException ex) {
            error(ex.getMessage());
        }
    }

    // ================= REMOVE =================

    private void showRemoveDialog() {
        String input = JOptionPane.showInputDialog(
                this, "Enter Book ID:",
                "Remove Book", JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            Book book = find(id);

            if (book == null) {
                error("Book with ID " + id + " was not found.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove \"" + book.getTitle() + "\" from the library?",
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            library.removeBook(id);
            showAllBooks();
            success("Book removed successfully.");

        } catch (NumberFormatException ex) {
            error("Book ID must be a valid number.");
        } catch (BookNotFoundException ex) {
            error(ex.getMessage());
        }
    }

    // ================= ROW ACTIONS =================

    private Book find(int id) {
        for (Book b : library.getBooks()) {
            if (b.getBookId() == id) return b;
        }
        return null;
    }

    private void viewSelectedBook(int row) {
        if (row < 0) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Book b = find(id);

        if (b == null) return;

        JOptionPane.showMessageDialog(
                this,
                "<html><b>Book Details</b><br><br>" +
                "Book ID: " + b.getBookId() + "<br>" +
                "Title: " + b.getTitle() + "<br>" +
                "Author: " + b.getAuthor() + "<br>" +
                "Genre: " + b.getGenre() + "<br>" +
                "Status: " + (b.isIssued() ? "Issued" : "Available") + "<br>" +
                "Issued To: " + b.getIssuedTo() +
                "</html>",
                "Book Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void deleteSelectedBook(int row) {
        if (row < 0) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Book b = find(id);

        if (b == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete \"" + b.getTitle() + "\"?",
                "Delete Book",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                library.removeBook(id);
                refreshTable(searchField.getText().trim());
                success("Book removed successfully.");
            } catch (BookNotFoundException ex) {
                error(ex.getMessage());
            }
        }
    }

    // ================= TABLE RENDERERS =================

    class ActionRenderer extends JPanel
            implements javax.swing.table.TableCellRenderer {

        ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {

            removeAll();

            JButton view = smallButton("View", new Color(37, 99, 235));
            JButton del = smallButton("Delete", new Color(220, 53, 69));

            add(view);
            add(del);

            setBackground(selected ? new Color(231, 240, 255) : Color.WHITE);
            return this;
        }
    }

    class ActionEditor extends AbstractCellEditor
            implements javax.swing.table.TableCellEditor {

        private final JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 5, 5)
        );

        ActionEditor() {
            panel.setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean selected,
                int row, int column) {

            panel.removeAll();

            JButton view = smallButton("View", new Color(37, 99, 235));
            JButton del = smallButton("Delete", new Color(220, 53, 69));

            final int modelRow = table.convertRowIndexToModel(row);

            view.addActionListener(e -> {
                fireEditingStopped();
                viewSelectedBook(modelRow);
            });

            del.addActionListener(e -> {
                fireEditingStopped();
                deleteSelectedBook(modelRow);
            });

            panel.add(view);
            panel.add(del);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private JButton smallButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setBorder(new EmptyBorder(5, 9, 5, 9));
        b.setFocusPainted(false);
        return b;
    }

    static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {

            JLabel l = (JLabel) super.getTableCellRendererComponent(
                    table, value, selected, focused, row, column
            );

            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));

            if ("Issued".equals(value)) {
                l.setForeground(new Color(184, 35, 47));
                l.setBackground(new Color(255, 229, 233));
            } else {
                l.setForeground(new Color(10, 126, 79));
                l.setBackground(new Color(219, 248, 234));
            }

            return l;
        }
    }

    // ================= HELPERS =================

    private JPanel formPanel() {
        JPanel p = new JPanel(new GridLayout(0, 2, 12, 12));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        return p;
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT);
        return l;
    }

    private void success(String msg) {
        JOptionPane.showMessageDialog(
                this, msg, "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(
                this, msg, "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void refreshTable(String keyword) {
        model.setRowCount(0);

        String key = keyword == null ? "" : keyword.toLowerCase();

        int total = library.getBooks().size();
        int issued = 0;
        Set<String> genres = new HashSet<>();

        for (Book b : library.getBooks()) {
            if (b.isIssued()) issued++;
            if (b.getGenre() != null && !b.getGenre().isBlank()) {
                genres.add(b.getGenre().trim().toLowerCase());
            }

            boolean matches = key.isBlank()
                    || String.valueOf(b.getBookId()).contains(key)
                    || safe(b.getTitle()).toLowerCase().contains(key)
                    || safe(b.getAuthor()).toLowerCase().contains(key)
                    || safe(b.getGenre()).toLowerCase().contains(key)
                    || safe(b.getIssuedTo()).toLowerCase().contains(key);

            if (matches) {
                model.addRow(new Object[]{
                        b.getBookId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getGenre(),
                        b.isIssued() ? "Issued" : "Available",
                        b.getIssuedTo(),
                        ""
                });
            }
        }

        int available = total - issued;

        totalValue.setText(String.valueOf(total));
        availableValue.setText(String.valueOf(available));
        issuedValue.setText(String.valueOf(issued));
        genresValue.setText(String.valueOf(genres.size()));

        resultLabel.setText(
                key.isBlank()
                        ? "Showing all " + total + " books"
                        : "Showing " + model.getRowCount() +
                          " matching book(s) for \"" + keyword + "\""
        );
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    static class RoundedPanel extends JPanel {
        private final Color bg;
        private final int radius;

        RoundedPanel(Color bg, int radius) {
            this.bg = bg;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(bg);
            g2.fillRoundRect(
                    0, 0, getWidth(), getHeight(),
                    radius, radius
            );
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {}

            new LibraryGUI().setVisible(true);
        });
    }
}
