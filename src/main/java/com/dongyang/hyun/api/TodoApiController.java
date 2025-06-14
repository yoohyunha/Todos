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
        Todo target = todoService.findById(id);
        if (target == null) return ResponseEntity.badRequest().build();
        // completed만 갱신
        target.setCompleted(dto.isCompleted());
        Todo updated = todoService.save(target);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/api/todos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return todoService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
