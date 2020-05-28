package com.example.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaListener {
    @Autowired
    ProductRepository productRepository;

    @StreamListener(Processor.INPUT)
    public void onEventByString(@Payload OrderPlaced orderPlaced){
        if( orderPlaced.getEventType().equals("OrderPlaced")){
           Optional<Product> optP = productRepository.findById(orderPlaced.getProductId());
           if (optP.isPresent()){
               Product p = optP.get();
               p.setStock(p.getStock() - orderPlaced.getQty());
               productRepository.save(p);
           } else {
               Product p = new Product();
               p.setName(orderPlaced.getProductName());
               p.setStock(orderPlaced.getQty());
               productRepository.save(p);
           }
        }
    }
}
