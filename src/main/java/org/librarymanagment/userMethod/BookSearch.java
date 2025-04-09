package org.librarymanagment.userMethod;

import org.librarymanagment.database.Book;
import org.librarymanagment.database.DataBase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class BookSearch {

    private DataBase dataBase = new DataBase();
    private DataBase.BookDB bookDB = dataBase.new BookDB();

    // 多条件查询图书
    public List<String> searchBooks(String searchType, String keyword,
                                    Timestamp startDate, Timestamp endDate,
                                    int pageNumber) {
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
                .map(BookSearch::formatBookInfo)
                .collect(Collectors.toList());
    }

    private static String formatBookInfo(Book book) {
        return String.format(
                "ID: %d\n书名: %s\n作者: %s\n位置: %s\n状态: %s\n入库时间: %s\n借出时间: %s\n归还时间: %s",
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getLocation(),
                book.getIsBorrowed() == 1? "已借出" : "在馆",
                formatTimestamp(book.getStorageTime()),
                formatTimestamp(book.getBorrowedTime()),
                formatTimestamp(book.getReturnTime())
        );
    }

    private static String formatTimestamp(Timestamp ts) {
        return ts != null? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts) : "无记录";
    }
}