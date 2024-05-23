package com.w2m.starships.amqp.producers;

import com.w2m.starships.configuration.RabbitMQConfiguration;
import com.w2m.starships.models.dto.StarshipDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void notifyNewStarship(StarshipDTO starshipDTO) {
        rabbitTemplate.send(RabbitMQConfiguration.EXCHANGE_NAME, RabbitMQConfiguration.NOTIFY_STARSHIP_ROUTING_KEY, MessageBuilder.withBody(("New starship created: " + starshipDTO).getBytes()).build());
    }
}
