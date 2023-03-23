package com.example.LibraryProjectWebApp.persistance.entity;


import com.example.LibraryProjectWebApp.config.Constant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Book")
@Slf4j
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_book")
    private String codeBook;

    @NotEmpty(message = "Book title must not be empty")
    @Size(min = 2, max = 100, message = "Book title must be between 2 and 100 characters long")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author must not be empty")
    @Size(min = 2, max = 100, message = "Book title must be between 2 and 100 characters long")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Year must be greater than 1500")
    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date takenAt;

    @Column(name = "booked")
    private boolean booked;

    @Transient
    private String returnBefore;

    @Transient
    private String takenAtString;

    @Transient
    private boolean expired;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getReturnBefore()  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(getTakenAt().toString()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        c.add(Calendar.DATE, 10);
        return new SimpleDateFormat("dd MMM yyyy").format(c.getTime());
    }

    public String getTakenAtString() {
        return new SimpleDateFormat("dd MMM yyyy").format(getTakenAt().getTime());
    }

    public boolean isExpired() {
        long diffInMillies = 0l;

        try{diffInMillies = Math.abs(getTakenAt().getTime() - new Date().getTime());}
        catch (NullPointerException e){
            log.error(e.getMessage());
        }
        if (diffInMillies > Constant.BOOK_RETURN_DEADLINE){
            return true;
    }
        return false;
    }

    public boolean isBooked() {
        return booked;
    }
}
