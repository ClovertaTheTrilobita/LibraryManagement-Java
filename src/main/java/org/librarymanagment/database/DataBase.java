package org.librarymanagment.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataBase {
    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/library_managemtent?serverTimezone=UTC";
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
        String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time "
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
                            rs.getTimestamp("storage_time")
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
     * @param keyword: 搜索内容
    	 * @param pageNumber: 页数
      * @return List<Book>
     * @author cloverta
     * @description 搜索书名，支持少字搜索，不支持模糊搜索。
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
        String sql = "SELECT book_id, book_name, author, location, is_borrowed, storage_time "
                + "FROM book "
                + "WHERE book_name LIKE ? "
                + "LIMIT 10 OFFSET ?";

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
                            rs.getTimestamp("storage_time")
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