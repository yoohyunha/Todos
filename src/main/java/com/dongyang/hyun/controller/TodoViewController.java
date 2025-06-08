package com.dongyang.hyun.controller;

import com.dongyang.hyun.dto.TodoDto;
import com.dongyang.hyun.entity.Todo;
import com.dongyang.hyun.entity.User;
import com.dongyang.hyun.service.TodoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
public class TodoViewController {
    private final TodoService todoService;

    public TodoViewController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todos/create")
    public String createTodo(TodoDto dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Todo todo = dto.toEntity();
        todo.setUser(user);
        todoService.create(todo); // TodoService에 create(Todo todo) 메서드 필요
        return "redirect:/todos";
    }

    @GetMapping("/todos")
    public String todosPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        // 사용자별 투두만 보여주려면 findByUserId 사용
        List<Todo> todos = todoService.findByUserId(user.getId());
        model.addAttribute("todos", todos);
        model.addAttribute("doneCount", todos.stream().filter(Todo::isCompleted).count());
        model.addAttribute("totalCount", todos.size());
        return "todos/todos";
    }
}
