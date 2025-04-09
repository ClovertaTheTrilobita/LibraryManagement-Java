package org.librarymanagment.userMethod;

import org.librarymanagment.database.DataBase;
import org.librarymanagment.database.BorrowInfo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BookBorrow {
    private DataBase dataBase;

    public BookBorrow() {
        dataBase = new DataBase();
    }

    public boolean returnBook(String userName, int bookId) {
        DataBase.UserListDB userListDB = dataBase.new UserListDB();
        DataBase.BorrowedBookManagement borrowedBookManagement = dataBase.new BorrowedBookManagement();
        int userId = userListDB.getUserByUserName(userName).getUserId();
        return borrowedBookManagement.returnBook(bookId);
    }

    public List<Vector> getBorrowedBooks(String userName) {
        DataBase.UserListDB userListDB = dataBase.new UserListDB();
        DataBase.BorrowedBookManagement borrowedBookManagement = dataBase.new BorrowedBookManagement();
        int userId = userListDB.getUserByUserName(userName).getUserId();

        List<BorrowInfo> borrowInfos = borrowedBookManagement.getBorrowHistoryByUserIdWithBookName(userId);
        List<Vector> tableData = new ArrayList<>();
        for (BorrowInfo borrowInfo : borrowInfos) {
            Vector rowData = new Vector<>();

            rowData.add(borrowInfo.getBookId());
            rowData.add(borrowInfo.getBookName());

            Timestamp borrowTime = borrowInfo.getBorrowTime();
            Timestamp returnTime = borrowInfo.getReturnTime();

            rowData.add(borrowTime != null ? borrowTime.toString() : "无记录");
            rowData.add(returnTime != null ? returnTime.toString() : "无记录");

            String status = returnTime != null ? "已还书" : "借阅中";
            rowData.add(status);

            tableData.add(rowData);
        }
        return tableData;
    }
}