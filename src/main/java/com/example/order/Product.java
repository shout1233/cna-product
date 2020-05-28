package com.example.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue
    Long id;
    String name;
    int stock;

    @PostPersist
    public void eventPublish(){
        ProductChanged productChanged = new ProductChanged();
        productChanged.setProductId(this.getId());
        productChanged.setProductName(this.getName());
        productChanged.setProductStock(this.getStock());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(productChanged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor messageProcessor = ProductApplication.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = messageProcessor.output();

        boolean messageSendSuccess = false;
        messageSendSuccess = outputChannel.send(
            MessageBuilder.withPayload(json)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build()
        );

        if (messageSendSuccess){
            System.out.println("Message Sended!!");
        } else {
            System.out.println("Message Failed!!");
        }
    }


    @PostUpdate
    public void updateEventPublish(){
        ProductChanged productChanged = new ProductChanged();
        productChanged.setProductId(this.getId());
        productChanged.setProductName(this.getName());
        productChanged.setProductStock(this.getStock());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(productChanged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor messageProcessor = ProductApplication.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = messageProcessor.output();

        boolean messageSendSuccess = false;
        messageSendSuccess = outputChannel.send(
                MessageBuilder.withPayload(json)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build()
        );

        if (messageSendSuccess){
            System.out.println("Update Message Sended!!");
        } else {
            System.out.println("Update Message Failed!!");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}