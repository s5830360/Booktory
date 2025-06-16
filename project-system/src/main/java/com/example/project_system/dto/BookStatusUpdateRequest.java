package com.example.project_system.dto;

import com.example.project_system.entity.BookStatus;
import lombok.Getter;

@Getter
public class BookStatusUpdateRequest {
    private BookStatus status;

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}