package com.example.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageServiceTest {

    @Test
    void getMessageReturnsExpectedText() {
        // Arrange
        MessageService service = new MessageService();

        // Act
        String actual = service.getMessage();

        // Assert
        assertEquals("Hello COMP2005!", actual);
    }
}