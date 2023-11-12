package com.bpdev.hotelservice.services;

import com.bpdev.hotelservice.entities.messaging.HotelDeletionMessage;
import com.bpdev.rabbitmqservice.RabbitMQMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessagingService {

    @Autowired
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    public void publishHotelDeletedEvent(UUID hotelId){
        rabbitMQMessageProducer.publish(
                new HotelDeletionMessage(hotelId),
                "internal.exchange",
                "hotel.deleted.routing-key"
        );
    }
}
