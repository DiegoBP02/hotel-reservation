package com.bpdev.reservationservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.room-deleted}")
    private String roomDeletedQueue;

    @Value("${rabbitmq.routing-keys.room-deleted}")
    private String roomDeletedRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue roomDeletedQueue() {
        return new Queue(this.roomDeletedQueue);
    }

    @Bean
    public Binding roomDeletedBinding() {
        return BindingBuilder
                .bind(roomDeletedQueue())
                .to(internalTopicExchange())
                .with(this.roomDeletedRoutingKey);
    }

}