package com.dongyang.hyun.service;

import com.dongyang.hyun.dto.TodoDto;
import com.dongyang.hyun.entity.Todo;
import com.dongyang.hyun.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public List<Todo> findByUserId(Long userId) {
        return todoRepository.findByUserId(userId);
    }

    // REST API용 (DTO 기반)
    public Todo create(TodoDto dto) {
        Todo todo = dto.toEntity();
        return create(todo); // 아래 엔티티 기반 메서드 재사용
    }

    public Todo create(Todo todo) {
        if (todo.getId() != null) return null;
        return todoRepository.save(todo);
    }
    public Todo update(Long id, TodoDto dto) {
        Todo target = todoRepository.findById(id).orElse(null);
        if (target == null) return null;
        target.setContent(dto.getContent());
        target.setCompleted(dto.isCompleted());
        return todoRepository.save(target);
    }

    public boolean delete(Long id) {
        if (!todoRepository.existsById(id)) return false;
        todoRepository.deleteById(id);
        return true;
    }

    public List<Todo> findByUserIdAndDate(Long userId, LocalDate date) {
        return todoRepository.findByUserIdAndDate(userId, date);
    }

}
