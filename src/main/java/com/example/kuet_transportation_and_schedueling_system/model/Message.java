package com.example.kuet_transportation_and_schedueling_system.model;
import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private int userId;
    private String message;
    private LocalDateTime messageDate;
    private String senderName;
    private boolean isRead;

    // JavaFX Properties
    private IntegerProperty messageIdProperty;
    private StringProperty senderNameProperty;
    private StringProperty messageProperty;
    private ObjectProperty<LocalDateTime> messageDateProperty;

    public Message() {}

    public Message(int messageId, int userId, String message, LocalDateTime messageDate) {
        this.messageId = messageId;
        this.userId = userId;
        this.message = message;
        this.messageDate = messageDate;
        this.isRead = false;
        initProperties();
    }

    private void initProperties() {
        this.messageIdProperty = new SimpleIntegerProperty(messageId);
        this.senderNameProperty = new SimpleStringProperty(senderName);
        this.messageProperty = new SimpleStringProperty(message);
        this.messageDateProperty = new SimpleObjectProperty<>(messageDate);
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
        if (messageIdProperty != null) {
            messageIdProperty.set(messageId);
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if (messageProperty != null) {
            messageProperty.set(message);
        }
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
        if (messageDateProperty != null) {
            messageDateProperty.set(messageDate);
        }
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
        if (senderNameProperty != null) {
            senderNameProperty.set(senderName);
        }
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    // JavaFX Properties
    public IntegerProperty messageIdProperty() {
        if (messageIdProperty == null) {
            messageIdProperty = new SimpleIntegerProperty(messageId);
        }
        return messageIdProperty;
    }

    public StringProperty senderNameProperty() {
        if (senderNameProperty == null) {
            senderNameProperty = new SimpleStringProperty(senderName);
        }
        return senderNameProperty;
    }

    public StringProperty messageProperty() {
        if (messageProperty == null) {
            messageProperty = new SimpleStringProperty(message);
        }
        return messageProperty;
    }

    public ObjectProperty<LocalDateTime> messageDateProperty() {
        if (messageDateProperty == null) {
            messageDateProperty = new SimpleObjectProperty<>(messageDate);
        }
        return messageDateProperty;
    }

    @Override public String toString() {
        return "Message{" +
                "from='" + senderName + '\'' +
                ", date=" + messageDate +
                ", message='" + message + '\'' +
                '}';
    }
}

