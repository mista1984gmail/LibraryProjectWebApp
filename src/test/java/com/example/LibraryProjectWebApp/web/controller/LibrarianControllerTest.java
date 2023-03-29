package com.example.LibraryProjectWebApp.web.controller;


import com.example.LibraryProjectWebApp.service.UserService;
import com.example.LibraryProjectWebApp.service.convertor.BookMapper;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import com.example.LibraryProjectWebApp.service.impl.BooksServiceImpl;
import com.example.LibraryProjectWebApp.util.BookFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibrarianController.class)
public class LibrarianControllerTest {

    public static final String URL = "/librarian";
    public static final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private BooksServiceImpl bookService;


    @Test
    @WithMockUser(username = "aliona", password = "12345", roles = "LIBRARIAN")
    public void findAll() throws Exception {
        //given
        BookDto bookFirst = BookFixture.createFirstBook();
        BookDto bookSecond = BookFixture.createSecondBook();

        var books = List.of(bookFirst, bookSecond);

        //when
        when(bookService.findAll())
                .thenReturn(books);

        //then
        MvcResult mvcResult = mockMvc.perform(get(URL + "/books"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String httpPage = mvcResult.getResponse().getContentAsString();

        Document doc = Jsoup.parse(httpPage);
        Elements elements = doc.select("td");
        ArrayList<String> text = new ArrayList<>();
        for(Element element : elements) {
            if(element.tagName().equals("td"))
                text.add(element.text());
        }

        Assertions.assertTrue(text.contains("Head First Java"));
        Assertions.assertTrue(text.contains("Kathy Sierra & Bert Bates"));
        Assertions.assertTrue(text.contains("2012"));
        Assertions.assertTrue(text.contains("Java: A Beginners Guide"));
        Assertions.assertTrue(text.contains("Herbert Schildt"));
        Assertions.assertTrue(text.contains("2015"));

        verify(bookService, times(1)).findAll();

    }

    @Test
    @WithMockUser(username = "aliona", password = "12345", roles = "LIBRARIAN")
    void save() throws Exception {
        //given
        BookDto bookDto = BookFixture.createFirstBook();

        //when
        bookService.save(bookDto);

        //then
        mockMvc.perform(
                        post(URL + "/book/new"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(bookService).save(bookDto);
    }

    @Test
    void deleteById() throws Exception {
        //given

        //when
        doNothing().when(bookService).delete(ID);

        //then
        mockMvc.perform(
                delete(URL + "/book/show/librarian/book/delete/" + ID)).andExpect(status().isOk());

        verify(bookService, times(0)).delete(ID);

    }
}
