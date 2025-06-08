package com.dongyang.hyun.dto;

import com.dongyang.hyun.entity.Todo;
import com.dongyang.hyun.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoDto {
    private Long id;
    private String content;
    private boolean completed;

    public Todo toEntity() {
        return new Todo(id, content, completed);
    }
}
