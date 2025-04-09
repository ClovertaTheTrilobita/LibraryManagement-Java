package org.librarymanagment.database;

import java.sql.Timestamp;

public class BorrowInfo {
    private int borrowId;
    private int userId;
    private int bookId;
    private String bookName;
    private Timestamp borrowTime;
    private Timestamp returnTime;

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Timestamp getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Timestamp borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }
}