package com.bpdev.reservationservice.services;

import com.bpdev.reservationservice.dtos.messaging.RoomDeletionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ReservationConsumer {

    @Autowired
    private ReservationService reservationService;

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queues.room-deleted}")
    public void hotelDeletedConsumer(RoomDeletionMessage roomDeletionMessage){
        log.info("HotelDeletedConsumer consumed {} from queue", roomDeletionMessage);
        reservationService.deleteAllByRoomId(roomDeletionMessage.getRoomId());
    }

}
