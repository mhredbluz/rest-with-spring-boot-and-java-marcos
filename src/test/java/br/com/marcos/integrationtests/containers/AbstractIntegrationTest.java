package br.com.marcos.integrationtests.containers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;
import java.util.Map;


@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        //Cria um container em runtime usando a versão 15 do postgresql
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

        //Faz o stream sobre o container criado acima e para obter as configurações a serem utilizadas a seguir
        private static void startContainers() {
            Startables.deepStart(Stream.of(postgres)).join();
        }

        @DynamicPropertySource
        private static Map<String, String> createConnectionConfiguration() {
            return Map.of("spring.datasource.url", postgres.getJdbcUrl(), "spring.datasource.username", postgres.getUsername(), "spring.datasource.password", postgres.getPassword());
        }


        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment(); // Obtem o contexto do Spring que está inicializando | Seta uma propriedade nova chamada testcontainers
            MapPropertySource testcontainers =
                    new MapPropertySource(
                            "testcontainers",
                            (Map) createConnectionConfiguration()); // Mapeia e seta as configurações escritas dinamicamente
            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
