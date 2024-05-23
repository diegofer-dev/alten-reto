package com.w2m.starships.amqp.consumers;

import com.w2m.starships.configuration.RabbitMQConfiguration;
import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.services.StarshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final StarshipService starshipService;

    @RabbitListener(queues = RabbitMQConfiguration.CREATE_STARSHIP_QUEUE_NAME)
    public void consumeMessage (@Payload StarshipDTO starshipDTO) {
        starshipService.save(starshipDTO);
    }
}
