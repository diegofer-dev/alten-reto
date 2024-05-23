package com.w2m.starships.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.starships.configuration.TestRedisConfiguration;
import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.models.entities.Starship;
import com.w2m.starships.repositories.StarshipRepository;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestRedisConfiguration.class)
@ContextConfiguration(initializers = StarshipControllerIntegrationTests.Initializer.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StarshipControllerIntegrationTests {

    private static final String STARSHIP_EXISTS = "STARSHIP_EXISTS";
    private static final String FILTERS_SET = "FILTERS_SET";
    private static final String STARSHIP_NOT_EXISTS = "STARSHIP_NOT_EXISTS";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Starship TEST_STARSHIP = Starship.builder()
            .starshipName("Test name")
            .movieName("Test movie")
            .numberOfPassengers(4)
            .build();

    private static final Starship TEST_STARSHIP_2 = Starship.builder()
            .starshipName("Test name 2")
            .movieName("Test movie 2")
            .numberOfPassengers(8)
            .build();

    private static final StarshipDTO TEST_STARSHIP_DTO = StarshipDTO.builder()
            .starshipName(TEST_STARSHIP.getStarshipName())
            .movieName(TEST_STARSHIP.getMovieName())
            .numberOfPassengers(TEST_STARSHIP.getNumberOfPassengers())
            .build();

    private static final StarshipDTO TEST_STARSHIP_DTO_UPDATE = StarshipDTO.builder()
            .starshipName("Updated name")
            .movieName(TEST_STARSHIP.getMovieName())
            .numberOfPassengers(TEST_STARSHIP.getNumberOfPassengers())
            .build();

    @ClassRule
    public static RabbitMQContainer testContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13.2-alpine"))
            .withExposedPorts(5672, 15672);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StarshipRepository starshipRepository;

    @AfterEach
    public void cleanUp() {
        starshipRepository.deleteAll();
    }

    @BeforeAll
    static void startContainer(){
        testContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        testContainer.stop();
    }

    @ParameterizedTest
    @ValueSource(strings = {STARSHIP_EXISTS, FILTERS_SET})
    void should_getStarships(String cases) throws Exception {
        starshipRepository.saveAll(List.of(TEST_STARSHIP, TEST_STARSHIP_2));
        if (STARSHIP_EXISTS.equals(cases))
            mockMvc.perform(MockMvcRequestBuilders.get("/api/starships")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numberOfElements").value(2))
                    .andReturn();
        else
            mockMvc.perform(MockMvcRequestBuilders.get("/api/starships")
                            .param("starshipName", "2")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numberOfElements").value(1))
                    .andReturn();
    }

    @ParameterizedTest
    @ValueSource(strings = {STARSHIP_EXISTS, STARSHIP_NOT_EXISTS})
    void should_getStarship(String cases) throws Exception {
        Long id = starshipRepository.save(TEST_STARSHIP).getStarshipId();
        if (STARSHIP_NOT_EXISTS.equals(cases))
            id = -1L;

        if (STARSHIP_EXISTS.equals(cases))
            mockMvc.perform(MockMvcRequestBuilders.get("/api/starships/" + id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.starshipName").value(TEST_STARSHIP.getStarshipName()))
                    .andReturn();
        else
            mockMvc.perform(MockMvcRequestBuilders.get("/api/starships/" + id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("Starship with id " + id + " not found"))
                    .andReturn();

    }

    @Test
    void should_createStarship() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/starships")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer test")
                        .content(MAPPER.writeValueAsBytes(TEST_STARSHIP_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.starshipName").value(TEST_STARSHIP_DTO.getStarshipName()))
                .andReturn();
        Assertions.assertEquals(1L, starshipRepository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {STARSHIP_EXISTS, STARSHIP_NOT_EXISTS})
    void should_updateStarship(String cases) throws Exception {
        Long id = starshipRepository.save(TEST_STARSHIP).getStarshipId();
        if (STARSHIP_NOT_EXISTS.equals(cases))
            id = -1L;

        if (STARSHIP_EXISTS.equals(cases)) {
            mockMvc.perform(MockMvcRequestBuilders.put("/api/starships/" + id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(MAPPER.writeValueAsBytes(TEST_STARSHIP_DTO_UPDATE)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.starshipName").value(TEST_STARSHIP_DTO_UPDATE.getStarshipName()))
                    .andReturn();
            starshipRepository.findById(id).ifPresent(starship -> Assertions.assertEquals(TEST_STARSHIP_DTO_UPDATE.getStarshipName(), starship.getStarshipName()));
        } else
            mockMvc.perform(MockMvcRequestBuilders.put("/api/starships/" + id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .content(MAPPER.writeValueAsBytes(TEST_STARSHIP_DTO_UPDATE)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("Starship with id " + id + " not found"))
                    .andReturn();
    }

    @ParameterizedTest
    @ValueSource(strings = {STARSHIP_EXISTS, STARSHIP_NOT_EXISTS})
    void should_deleteStarship(String cases) throws Exception {
        Long id = starshipRepository.save(TEST_STARSHIP).getStarshipId();
        if (STARSHIP_NOT_EXISTS.equals(cases))
            id = -1L;

        if (STARSHIP_EXISTS.equals(cases)) {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/starships/" + id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.starshipName").value(TEST_STARSHIP.getStarshipName()))
                    .andReturn();
            Assertions.assertEquals(0, starshipRepository.count());
        } else {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/starships/" + id)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("Starship with id " + id + " not found"))
                    .andReturn();
            Assertions.assertEquals(1L, starshipRepository.count());
        }
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            var values = TestPropertyValues.of(
                    "spring.rabbitmq.host=" + testContainer.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + testContainer.getMappedPort(5672)
            );
            values.applyTo(applicationContext);
        }
    }


}
