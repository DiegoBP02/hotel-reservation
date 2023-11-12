package com.bpdev.roomservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration  {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.hotel-deleted}")
    private String hotelDeletedQueue;

    @Value("${rabbitmq.routing-keys.hotel-deleted}")
    private String hotelDeletedRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue hotelDeletedQueue() {
        return new Queue(this.hotelDeletedQueue);
    }

    @Bean
    public Binding hotelDeletedBinding() {
        return BindingBuilder
                .bind(hotelDeletedQueue())
                .to(internalTopicExchange())
                .with(this.hotelDeletedRoutingKey);
    }

}