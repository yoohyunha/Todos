package com.dongyang.hyun.api;

import com.dongyang.hyun.dto.TodoDto;
import com.dongyang.hyun.entity.Todo;
import com.dongyang.hyun.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TodoApiController {
    @Autowired
    private TodoService todoService;

    @GetMapping("/api/todos")
    public List<Todo> getTodos() {
        return todoService.findAll();
    }

    @PostMapping("/api/todos")
    public ResponseEntity<Todo> create(@RequestBody TodoDto dto) {
        Todo created = todoService.create(dto);
        return (created != null) ? ResponseEntity.ok(created) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/api/todos/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody TodoDto dto) {
        Todo updated = todoService.update(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/api/todos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return todoService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
