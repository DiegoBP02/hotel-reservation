package com.bpdev.roomservice.services;

import com.bpdev.rabbitmqservice.RabbitMQMessageProducer;
import com.bpdev.roomservice.dtos.messaging.RoomDeletionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessagingService {

    @Autowired
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    public void publishRoomDeletedEvent(UUID roomId){
        rabbitMQMessageProducer.publish(
                new RoomDeletionMessage(roomId),
                "internal.exchange",
                "room.deleted.routing-key"
        );
    }
}
