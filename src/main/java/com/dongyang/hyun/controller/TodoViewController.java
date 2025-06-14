package com.dongyang.hyun.controller;

import com.dongyang.hyun.dto.TodoDto;
import com.dongyang.hyun.entity.FriendRequest;
import com.dongyang.hyun.entity.Todo;
import com.dongyang.hyun.entity.User;
import com.dongyang.hyun.service.FriendService;
import com.dongyang.hyun.service.TodoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TodoViewController {
    private final TodoService todoService;
    private final FriendService friendService;

    public TodoViewController(TodoService todoService, FriendService friendService) {
        this.todoService = todoService;
        this.friendService = friendService;
    }

    @PostMapping("/todos/create")
    public String createTodo(TodoDto dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Todo todo = dto.toEntity();
        todo.setUser(user);
        todoService.create(todo);
        return "redirect:/todos?date=" + todo.getDate(); // 추가 후 해당 날짜로 이동
    }


    @GetMapping("/todos")
    public String todosPage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (date == null) date = LocalDate.now();
        List<Todo> todos = todoService.findByUserIdAndDate(user.getId(), date);

        // 친구 목록, 친구 요청 추가
        List<User> friends = friendService.getFriends(user.getId());
        List<FriendRequest> pendingRequests = friendService.getPendingRequests(user.getId());

        model.addAttribute("todos", todos);
        model.addAttribute("doneCount", todos.stream().filter(Todo::isCompleted).count());
        model.addAttribute("totalCount", todos.size());
        model.addAttribute("selectedDate", date);

        model.addAttribute("currentYear", date.getYear());
        model.addAttribute("currentMonth", date.getMonthValue());
        model.addAttribute("prevMonth", date.minusMonths(1).withDayOfMonth(1));
        model.addAttribute("nextMonth", date.plusMonths(1).withDayOfMonth(1));
        model.addAttribute("calendarRows", generateCalendarRows(date, date));

        // 추가!
        model.addAttribute("friends", friends);
        model.addAttribute("pendingRequests", pendingRequests);

        return "todos/todos";
    }


    private List<List<Map<String, Object>>> generateCalendarRows(LocalDate baseDate, LocalDate selectedDate) {
        YearMonth ym = YearMonth.from(baseDate);
        LocalDate firstDay = ym.atDay(1);
        LocalDate lastDay = ym.atEndOfMonth();
        List<List<Map<String, Object>>> calendarRows = new ArrayList<>();
        List<Map<String, Object>> week = new ArrayList<>();

        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Sunday=0
        for (int i = 0; i < dayOfWeek; i++) {
            week.add(Map.of("day", "", "date", ""));
        }
        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            week.add(Map.of(
                    "day", date.getDayOfMonth(),
                    "date", date.toString(),
                    "today", date.equals(LocalDate.now()),
                    "isSelected", date.equals(selectedDate)
            ));
            if (week.size() == 7) {
                calendarRows.add(week);
                week = new ArrayList<>();
            }
        }
        while (week.size() < 7) {
            week.add(Map.of("day", "", "date", ""));
        }
        if (!week.isEmpty()) calendarRows.add(week);
        return calendarRows;
    }

}
