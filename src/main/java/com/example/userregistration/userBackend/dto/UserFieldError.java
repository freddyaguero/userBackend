package com.example.userregistration.userBackend.dto;

import java.time.LocalDateTime;

public class UserFieldError {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    
}
    