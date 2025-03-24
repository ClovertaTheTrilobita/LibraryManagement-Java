package org.librarymanagment.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataBase {
    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/library_management?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final Set<String> ALLOWED_TABLES = Set.of("book", "user_list", "borrow_list");

    static {
        try {
            // 自动加载驱动（JDBC 4.0+可以省略，但显式声明更可靠）
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC驱动未找到", e);
        }
    }

    // 获取数据库连接
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    /**
      * @author cloverta
     * @description Database下的BookDB对象，用于对图书数据库的操作。
     * @date 2025/3/23 21:19
     */
    public class BookDB {


        // 查询分页图书信息
        /**
         * @param pageNumber: (int)第几页
         * @return List<Book>
         * @author cloverta
         * @description 获取图书列表信息，传入值为第几页，返回值为一个由Book对象组成的List，一页十本书。
         * @date 2025/3/21 17:45
         */
        public List<Book> fetchBooks(int pageNumber) {
            List<Book> books = new ArrayList<>();
            // 参数校验
            if(pageNumber < 1) {
                throw new IllegalArgumentException("页码不能小于1");
            }

            // 计算偏移量（从0开始）
            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time, borrow_time, return_time "
                    + "FROM book LIMIT 10 OFFSET ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // 设置分页参数
                pstmt.setInt(1, offset);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Book book = new Book(
                                rs.getInt("book_id"),
                                rs.getString("book_name"),
                                rs.getString("author"),
                                rs.getString("location"),
                                rs.getInt("is_borrowed"),
                                rs.getTimestamp("storage_time"),
                                rs.getTimestamp("borrow_time"),
                                rs.getTimestamp("return_time")
                        );
                        books.add(book);
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return books;
        }


        public List<Book> fetchBorrowedBooks(int pageNumber) {
            List<Book> books = new ArrayList<>();
            if(pageNumber < 1) {
                throw new IllegalArgumentException("页码不能小于1");
            }

            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time, borrow_time, return_time FROM book WHERE is_borrowed = 1 LIMIT 10 OFFSET ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // 设置分页参数
                pstmt.setInt(1, offset);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Book book = new Book(
                                rs.getInt("book_id"),
                                rs.getString("book_name"),
                                rs.getString("author"),
                                rs.getString("location"),
                                rs.getInt("is_borrowed"),
                                rs.getTimestamp("storage_time"),
                                rs.getTimestamp("borrow_time"),
                                rs.getTimestamp("return_time")
                        );
                        books.add(book);
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return books;
        }


        /**
         * @param keyword: (String)搜索内容
         * @param pageNumber: (int)页数
         * @return List<Book>
         * @author cloverta
         * @description 搜索书名，支持少字搜索，返回值为一个由Book对象组成的List，一页十本书。
         * @date 2025/3/21 22:11
         */
        public List<Book> searchBooksByName(String keyword, int pageNumber) {
            List<Book> books = new ArrayList<>();
            // 参数校验
            if(pageNumber < 1) {
                throw new IllegalArgumentException("页码不能小于1");
            }

            // 计算偏移量（从0开始）
            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time, borrow_time, return_time "
                    + "FROM book "
                    + "WHERE book_name LIKE ? "
                    + "LIMIT 10 OFFSET ?";

            return getBooks(keyword, books, offset, sql);
        }


        /**
         * @param keyword: (String)搜索关键词
        	 * @param pageNumber: (int)第几页
          * @return List<Book>
         * @author cloverta
         * @description 根据作者名搜索图书，返回值为一个由Book对象组成的List，一页十本书。
         * @date 2025/3/23 21:28
         */
        public List<Book> searchBooksByAuthor(String keyword, int pageNumber) {
            List<Book> books = new ArrayList<>();
            if(pageNumber < 1) {
                throw new IllegalArgumentException("页码不能小于1");
            }

            // 计算偏移量（从0开始）
            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time, borrow_time, return_time "
                    + "FROM book "
                    + "WHERE author LIKE ? "
                    + "LIMIT 10 OFFSET ?";

            return getBooks(keyword, books, offset, sql);
        }


        /**
         * @param keyword: (String)搜索关键词
        	 * @param pageNumber: (int)页数
          * @return List<Book>
         * @author cloverta
         * @description 根据图书位置搜索图书，返回值为一个由Book对象组成的List，一页十本书。
         * @date 2025/3/23 21:47
         */
        public List<Book> searchBooksByLocation(String keyword, int pageNumber) {
            List<Book> books = new ArrayList<>();
            if(pageNumber < 1) {
                throw new IllegalArgumentException("页码不能小于1");
            }

            // 计算偏移量（从0开始）
            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time, borrow_time, return_time "
                    + "FROM book "
                    + "WHERE location LIKE ? "
                    + "LIMIT 10 OFFSET ?";

            return getBooks(keyword, books, offset, sql);
        }


        /**
         * @param book: (Class)一个图书对象
          * @return boolean
         * @author cloverta
         * @description 添加成功则返回True
         * @date 2025/3/23 21:30
         */
        public boolean addBook(Book book) {
            String sql = "INSERT INTO book (book_name, author, location, is_borrowed, storage_time, borrow_time, return_time) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, book.getName());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getLocation());
                pstmt.setInt(4, book.getIsBorrowed());
                pstmt.setTimestamp(5, book.getStorageTime());
                pstmt.setTimestamp(6, book.getBorrowedTime());
                pstmt.setTimestamp(7, book.getReturnTime());

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;  // 返回是否成功删除

            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        }


        /**
         * @param bookId: (int)书本id
          * @return boolean
         * @author cloverta
         * @description 根据bookId删除一本书
         * @date 2025/3/23 21:38
         */
        public boolean deleteBook(int bookId) {
            String sql = "DELETE FROM book WHERE book_id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, bookId);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;  // 返回是否成功删除

            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        }


        /**
         * @param bookId: (int)图书id
        	 * @param book: (Class)Book对象
          * @return boolean
         * @author cloverta
         * @description 根据bookId修改图书信息，不允许修改bookId
         * @date 2025/3/23 21:44
         */
        public boolean updateBook(int bookId, Book book) {
            String sql = "UPDATE book SET book_name = ?, author = ?, location = ?, is_borrowed = ?, storage_time = ? WHERE book_id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, book.getName());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getLocation());
                pstmt.setInt(4, book.getIsBorrowed());
                pstmt.setTimestamp(5, book.getStorageTime());
                pstmt.setInt(6, bookId);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;  // 返回是否成功修改

            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        }


        /**
         * @param keyword:
        	 * @param books:
        	 * @param offset:
        	 * @param sql:
          * @return List<Book>
         * @author cloverta
         * @description 私有方法，用于简化代码
         * @date 2025/3/23 21:29
         */
        private List<Book> getBooks(String keyword, List<Book> books, int offset, String sql) {
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // 设置分页参数
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setInt(2, offset);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Book book = new Book(
                                rs.getInt("book_id"),
                                rs.getString("book_name"),
                                rs.getString("author"),
                                rs.getString("location"),
                                rs.getInt("is_borrowed"),
                                rs.getTimestamp("storage_time"),
                                rs.getTimestamp("borrow_time"),
                                rs.getTimestamp("return_time")
                        );
                        books.add(book);
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return books;
        }
    }


    /**
     * @author cloverta
     * @description DataBase下的UserListDB对象，用于对用户表进行操作。
     * @date 2025/3/23 21:34
     */
    public class UserListDB {
        /**
         * @param pageNumber: (int)页数
         * @return List<User>
         * @author cloverta
         * @description 获取全部用户信息，供管理员使用，一页十个。
         * @date 2025/3/21 19:54
         */
        public List<User> fetchUsers(int pageNumber) {
            List<User> users = new ArrayList<>();
            if(pageNumber < 1) {
                throw new IllegalArgumentException("页码不能小于1");
            }

            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT user_id, user_name, password, email, phone, gender, is_admin "
                    + "FROM user_list LIMIT 10 OFFSET ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, offset);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        User user = new User(
                                rs.getInt("user_id"),
                                rs.getString("user_name"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getInt("gender"),
                                rs.getInt("is_admin")
                        );
                        users.add(user);
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return users;
        }

        /**
         * @param userId: 要删除的用户ID
         * @return boolean
         * @author cloverta
         * @description 删除数据库中相应userID的用户
         * @date 2025/3/21 21:22
         */
        public boolean deleteUser(int userId) {
            String sql = "DELETE FROM user_list WHERE user_id=?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, userId);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;  // 返回是否成功删除

            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        }


        /**
         * @param keyword: 搜索关键字
         * @param pageNumber: 页数
         * @return List<User>
         * @author cloverta
         * @description 通过用户名关键字搜索用户
         * @date 2025/3/21 22:28
         */
        public List<User> searchUsersByName(String keyword, int pageNumber) {
            List<User> users = new ArrayList<>();
            if(keyword == null) {
                throw new IllegalArgumentException("用户名不能为空");
            }

            int offset = (pageNumber - 1) * 10;
            String sql = "SELECT user_id, user_name, password, email, phone, gender, is_admin FROM user_list WHERE user_name LIKE ? LIMIT 10 OFFSET ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setInt(2, offset);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        User user = new User(
                                rs.getInt("user_id"),
                                rs.getString("user_name"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getInt("gender"),
                                rs.getInt("is_admin")
                        );
                        users.add(user);
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            return users;
        }


        /**
         * @param user: (Class)传入的参数为一个用户对象
         * @return boolean
         * @author cloverta
         * @description 传入用户对象，返回值为boolean，及返回值为True代表添加成功。
         * @date 2025/3/23 21:15
         */
        public boolean addUser(User user) {
            String sql = "INSERT INTO user_list (user_name, password, email, phone, gender, is_admin) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, user.getUserName());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getPhone());
                pstmt.setInt(5, user.getGender());
                pstmt.setInt(6, user.getIsAdmin());

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;  // 返回是否成功删除

            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        }


        /**
         * @param userId: (int)用户ID
        	 * @param user: (Class)User对象
          * @return boolean
         * @author cloverta
         * @description 传入用户ID和一个User对象，根据userId修改用户信息
         * @date 2025/3/23 21:53
         */
        public boolean updateUser(int userId, User user) {
            String sql = "UPDATE user_list SET user_name = ?, password = ?, email = ?, phone = ?, gender = ?, is_admin = ? WHERE user_id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, user.getUserName());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getPhone());
                pstmt.setInt(5, user.getGender());
                pstmt.setInt(6, user.getIsAdmin());
                pstmt.setInt(7, userId);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;  // 返回是否成功修改

            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        }
    }



//    public class BorrowHistoryDB {
//        public List<Book> fetchBorrowedBooks(int pageNumber) {
//            List<Book> books = new ArrayList<>();
//            if(pageNumber < 1) {
//                throw new IllegalArgumentException("页码不能小于1");
//            }
//
//            int offset = (pageNumber - 1) * 10;
//            String sql = "SELECT borrow_id, book_id, borrow_time, return_time FROM borrow_list LIMIT 10 OFFSET ?";
//            String sql1 = "SELECT book_id, book_name, author, location, is_borrowed, storage_time, borrow_time, return_time FROM book WHERE book_id = " +
//                    "(SELECT book_id FROM borrow_list) LIMIT 10 OFFSET ?";
//
//            try (Connection conn = getConnection();
//                 PreparedStatement pstmt = conn.prepareStatement(sql);
//                 PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
//
//                pstmt.setInt(1, offset);
//                pstmt1.setInt(1, offset);
//
//                try (ResultSet rs = pstmt.executeQuery();
//                ResultSet rs1 = pstmt1.executeQuery();) {
//                    while (rs.next()) {
//                        Book book = new Book(
//                                rs.getInt("book_id"),
//                                rs.getString("book_name"),
//                                rs.getString("author"),
//                                rs.getString("location"),
//                                rs.getInt("is_borrowed"),
//                                rs.getTimestamp("storage_time")
//                        );
//                        books.add(book);
//                    }
//                }
//            } catch (SQLException e) {
//                handleSQLException(e);
//            }
//            return books;
//        }
//    }

    /**
     * @param formName: (String) 数据库中的表名
     * @return int
     * @author cloverta
     * @description 获取该列表的总页数，例如获取图书列表总页数应为 getTotalPages("book")， 获取用户列表页数则使用 getTotalPages("user_list")
     * @date 2025/3/21 19:41
     */
    public int getTotalPages(String formName) {
        if (!ALLOWED_TABLES.contains(formName)) {
            throw new IllegalArgumentException("非法表名");
        }
        String sql = "SELECT COUNT(*) FROM " + formName;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if(rs.next()) {
                int total = rs.getInt(1);
                return (int) Math.ceil(total / 10.0);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return 0;
    }



    // 异常处理
    private void handleSQLException(SQLException e) {
        System.err.println("数据库操作异常：");
        System.err.println("错误代码: " + e.getErrorCode());
        System.err.println("SQL状态: " + e.getSQLState());
        System.err.println("错误信息: " + e.getMessage());
        e.printStackTrace();
    }

    // 使用示例
    public static void main(String[] args) throws SQLException {
        DataBase db = new DataBase();
    }
}