package com.bpdev.roomservice.services;

import com.bpdev.roomservice.entities.messaging.HotelDeletionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class RoomConsumer {

    @Autowired
    private RoomService roomService;

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queues.hotel-deleted}")
    public void hotelDeletedConsumer(HotelDeletionMessage hotelDeletionMessage){
        log.info("HotelDeletedConsumer consumed {} from queue", hotelDeletionMessage);
        roomService.deleteAllByHotelId(hotelDeletionMessage.getHotelId());
    }

}
