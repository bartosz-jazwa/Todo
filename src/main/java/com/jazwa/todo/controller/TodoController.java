package com.jazwa.todo.controller;

import com.jazwa.todo.model.Todo;
import com.jazwa.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/todos")
public class TodoController {

    @Autowired
    TodoRepository todoRepository;

    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        List<Todo> todos = todoRepository.findAll();
        if (todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getOne(@PathVariable Long id) {
        return ResponseEntity.of(todoRepository.findById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> addOne(@RequestBody Todo todo) {

        Optional<Todo> todoSave = Optional.ofNullable(todoRepository.save(todo));

        if (todoSave.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(todoSave.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteOne(@PathVariable Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        optionalTodo.ifPresent(t -> todoRepository.delete(t));

        return ResponseEntity.of(optionalTodo);
    }

    @DeleteMapping
    public ResponseEntity<Todo> deleteAll() {
        todoRepository.deleteAll();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateOne(@PathVariable Long id,
                                          @RequestBody Todo todo) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        optionalTodo.ifPresent(t -> {
            t.setTitle(todo.getTitle());
            t.setContent(todo.getContent());
            todoRepository.save(t);
        });
        return ResponseEntity.of(optionalTodo);
    }

}
