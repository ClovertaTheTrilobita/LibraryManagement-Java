package org.librarymanagment.database;

import java.sql.Timestamp;

public class Book {
    private int id;
    private String name;
    private String author;
    private String location;
    private int isBorrowed;
    private Timestamp storageTime;
    private Timestamp borrowedTime;
    private Timestamp returnTime;

    public Timestamp getBorrowedTime() {
        return borrowedTime;
    }

    public void setBorrowedTime(Timestamp borrowedTime) {
        this.borrowedTime = borrowedTime;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public int getIsBorrowed() {
        return isBorrowed;
    }

    public void setIsBorrowed(int isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public Timestamp getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Timestamp storageTime) {
        this.storageTime = storageTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Book(int id, String name, String author, String location, int isBorrowed, Timestamp storageTime, Timestamp borrowedTime, Timestamp returnTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.location = location;
        this.isBorrowed = isBorrowed;
        this.storageTime = storageTime;
        this.borrowedTime = borrowedTime;
        this.returnTime = returnTime;
    }
}
