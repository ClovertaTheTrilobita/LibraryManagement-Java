package org.librarymanagment.management;

import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.Book;

import java.sql.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class BookManagement {
    // 添加图书
    public static boolean addBook(String bookName, String author, String location) {
        // 创建 Book 对象并填充数据
        Book book = new Book(
                0,                  // book_id 由数据库自增
                bookName,
                author,
                location,
                0,// is_borrowed 默认未借出
                new Timestamp(System.currentTimeMillis()),  // 当前时间戳
                null, // borrowedTime（未被借出）
                null  // returnTime（未被借出）
        );

        // 调用数据库操作类
        DataBase db = new DataBase();
        DataBase.BookDB bookDB = db.new BookDB();
        boolean success = bookDB.addBook(book);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "添加失败，请检查输入");
        }
        return success;
    }

    // 删除图书（需检查借阅状态）
    public static boolean deleteBook(int bookId) {
        // 检查是否被借出
        DataBase db = new DataBase();
        DataBase.BookDB bookDB = db.new BookDB();
        boolean success = bookDB.deleteBook(bookId);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "删除失败，请检查图书ID是否存在");
        } else {
            JOptionPane.showMessageDialog(null, "删除成功");
        }
        return success;
    }

    // 多条件查询图书

        public static List<String> searchBooks(String searchType, String keyword,
                                               Timestamp startDate, Timestamp endDate,
                                               int pageNumber) {
            DataBase db = new DataBase();
            DataBase.BookDB bookDB = db.new BookDB();
            List<Book> books = new ArrayList<>();

            try {
                switch (searchType.toLowerCase()) {
                    case "id":
                        validatePageForIdSearch(pageNumber);
                        int bookId = parseBookId(keyword);
                        books = bookDB.searchBooksById(bookId, pageNumber);
                        break;

                    case "name":
                        validateKeyword(keyword);
                        books = bookDB.searchBooksByName(keyword, pageNumber);
                        break;

                    case "author":
                        validateKeyword(keyword);
                        books = bookDB.searchBooksByAuthor(keyword, pageNumber);
                        break;

                    case "location":
                        validateKeyword(keyword);
                        books = bookDB.searchBooksByLocation(keyword, pageNumber);
                        break;

                    case "time":
                        validateDateRange(startDate, endDate);
                        books = bookDB.searchBooksByStorageDate(startDate, endDate, pageNumber);
                        break;

                    default:
                        throw new IllegalArgumentException("不支持的查询类型: " + searchType);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID必须为数字");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "查询失败: " + e.getMessage());
            }

            return convertBooksToDisplay(books);
        }

        // 辅助方法：参数校验
        private static void validatePageForIdSearch(int pageNumber) {
            if (pageNumber != 1) {
                throw new IllegalArgumentException("ID查询不支持分页");
            }
        }

        private static int parseBookId(String keyword) {
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new IllegalArgumentException("图书ID不能为空");
            }
            return Integer.parseInt(keyword);
        }

        private static void validateKeyword(String keyword) {
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new IllegalArgumentException("搜索内容不能为空");
            }
        }

        private static void validateDateRange(Timestamp start, Timestamp end) {
            if (start == null || end == null) {
                throw new IllegalArgumentException("必须指定时间范围");
            }
            if (start.after(end)) {
                throw new IllegalArgumentException("开始时间不能晚于结束时间");
            }
        }

        // 辅助方法：转换显示格式
        private static List<String> convertBooksToDisplay(List<Book> books) {
            return books.stream()
                    .map(BookManagement::formatBookInfo)
                    .collect(Collectors.toList());
        }

        private static String formatBookInfo(Book book) {
            return String.format(
                    "ID: %d\n书名: %s\n作者: %s\n位置: %s\n状态: %s\n入库时间: %s\n借出时间: %s\n归还时间: %s",
                    book.getId(),
                    book.getName(),
                    book.getAuthor(),
                    book.getLocation(),
                    book.getIsBorrowed() == 1 ? "已借出" : "在馆",
                    formatTimestamp(book.getStorageTime()),
                    formatTimestamp(book.getBorrowedTime()),
                    formatTimestamp(book.getReturnTime())
            );
        }

        private static String formatTimestamp(Timestamp ts) {
            return ts != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts) : "无记录";
        }


    // 更新图书信息
    public static boolean updateBookInfo(int bookId, String name, String author,
                                         String location, int isBorrowed,
                                         Timestamp storageTime) {
        // 创建 Book 对象（仅填充需要更新的字段）
        Book book = new Book(
                bookId,       // 虽然 SQL 中不更新 book_id，但需要传递以避免构造函数缺失字段
                name,
                author,
                location,
                isBorrowed,
                storageTime,
                null,         // borrowedTime
                null          // returnTime
        );

        // 调用数据库操作类
        DataBase db = new DataBase();
        DataBase.BookDB bookDB = db.new BookDB();
        boolean success = bookDB.updateBook(bookId, book);

        // 错误处理
        if (!success) {
            JOptionPane.showMessageDialog(null, "更新失败，请检查书籍ID是否存在");
        } else {
            JOptionPane.showMessageDialog(null, "图书信息更新成功");
        }
        return success;
    }
}
