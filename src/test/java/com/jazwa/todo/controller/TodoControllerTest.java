package com.jazwa.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazwa.todo.model.Todo;
import com.jazwa.todo.repository.TodoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TodoRepository todoRepository;

    @Test
    public void getAllReturnOk() throws Exception {

        List<Todo> todos = new ArrayList<>();
        Todo todo1 = new Todo("title1","content1");
        Todo todo2 = new Todo("title2","content2");
        todos.add(todo1);
        todos.add(todo2);

        when(todoRepository.findAll()).thenReturn(todos);

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    public void getAllReturnNoContent() throws Exception {
        when(todoRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/todos"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getOneReturnOk() throws Exception {
        Todo todo1 = new Todo("title1","content1");
        todo1.setId(1L);

        String todoJson = objectMapper.writeValueAsString(todo1);

        when(todoRepository.findById(1L)).thenReturn(Optional.ofNullable(todo1));
        mockMvc.perform(get("/todos/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(todoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void getOneReturnNotFound() throws Exception {

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addOneReturnCreated() throws Exception {
        Todo todo1 = new Todo("title1","content1");
        todo1.setId(1L);
        String todoJson = objectMapper.writeValueAsString(todo1);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo1);
        mockMvc.perform(post("/todos")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(todoJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(todoJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void addOneReturnBadRequest() throws Exception {
        Todo todo1 = new Todo("title1","content1");
        todo1.setId(1L);
        String todoJson = objectMapper.writeValueAsString(todo1);
        when(todoRepository.save(any(Todo.class))).thenReturn(null);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(todoJson))

                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteOneReturnOk() throws Exception {
        Todo todo1 = new Todo("title1","content1");
        todo1.setId(1L);

        String todoJson = objectMapper.writeValueAsString(todo1);

        when(todoRepository.findById(1L)).thenReturn(Optional.ofNullable(todo1));
        mockMvc.perform(delete("/todos/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(todoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteOneReturnNotFound() throws Exception {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/todos/1"))

                .andExpect(status().isNotFound());
    }

    @Test
    public void updateOneReturnOk() throws Exception {
        Todo todo1 = new Todo("title1","content1");
        todo1.setId(1L);
        String todoJson = objectMapper.writeValueAsString(todo1);

        when(todoRepository.findById(1L)).thenReturn(Optional.ofNullable(todo1));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo1);
        mockMvc.perform(put("/todos/1")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(todoJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(todoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void updateOneReturnNotFound() throws Exception{
        Todo todo1 = new Todo("title1","content1");
        todo1.setId(1L);
        String todoJson = objectMapper.writeValueAsString(todo1);

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/todos/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(todoJson))
                .andExpect(status().isNotFound());
    }
}