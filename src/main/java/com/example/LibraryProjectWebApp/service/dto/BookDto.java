package com.example.LibraryProjectWebApp.service.dto;

import com.example.LibraryProjectWebApp.config.Constant;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    private String codeBook;

    @NotEmpty(message = "Book title must not be empty")
    @Size(min = 2, max = 100, message = "Book title must be between 2 and 100 characters long")
    private String title;

    @NotEmpty(message = "Author must not be empty")
    @Size(min = 2, max = 100, message = "Book title must be between 2 and 100 characters long")
    private String author;

    @Min(value = 1500, message = "Year must be greater than 1500")
    private int year;

    private User owner;

    private Date takenAt;

    private boolean booked;

    private String returnBefore;

    private String takenAtString;

    private boolean expired;

    public BookDto(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getReturnBefore()  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(getTakenAt().toString()));
        } catch (ParseException | NullPointerException e) {
            log.error(e.getMessage());
        }
        c.add(Calendar.DATE, Constant.BOOK_RETURN_DEADLINE_DAYS);
        return new SimpleDateFormat("dd MMM yyyy").format(c.getTime());
    }

    public String getTakenAtString() {
        if(getTakenAt()==null){
            return "";
        }
        else return new SimpleDateFormat("dd MMM yyyy").format(getTakenAt().getTime());
    }

    public boolean isExpired() {
        long diffInMillies = 0l;

        try{diffInMillies = Math.abs(getTakenAt().getTime() - new Date().getTime());}
        catch (NullPointerException e){
            log.error(e.getMessage());
        }
        if (diffInMillies > Constant.BOOK_RETURN_DEADLINE_MS){
            return true;
        }
        return false;
    }

    public boolean isBooked() {
        return booked;
    }
    public Date getTakenAt() {
     return takenAt;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", codeBook='" + codeBook + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
