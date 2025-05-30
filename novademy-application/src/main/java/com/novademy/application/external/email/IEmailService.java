package com.novademy.application.external.email;

public interface IEmailService {
    void sendEmail(String to, String subject, String body, boolean isHtml) throws Exception;
} 