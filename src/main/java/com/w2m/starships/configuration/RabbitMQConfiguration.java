package com.w2m.starships.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String CREATE_STARSHIP_QUEUE_NAME = "w2m-create-starship";
    public static final String CREATE_STARSHIP_ROUTING_KEY = "w2m.create.starships";

    public static final String NOTIFY_STARSHIP_QUEUE_NAME = "w2m-notify-starship";
    public static final String NOTIFY_STARSHIP_ROUTING_KEY = "w2m.notify.starships";

    public static final String EXCHANGE_NAME = "w2m-exchange";

    @Bean
    public Queue createStarshipQueue() {
        return new Queue(CREATE_STARSHIP_QUEUE_NAME, false);
    }

    @Bean
    public Queue notifyStarshipQueue() {
        return new Queue(NOTIFY_STARSHIP_QUEUE_NAME, false);
    }


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding declareCreateStarshipBinding(Queue createStarshipQueue, TopicExchange exchange) {
        return BindingBuilder.bind(createStarshipQueue).to(exchange).with(CREATE_STARSHIP_ROUTING_KEY);
    }

    @Bean
    public Binding declareNotifyStarshipBinding(Queue notifyStarshipQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notifyStarshipQueue).to(exchange).with(NOTIFY_STARSHIP_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }



}
