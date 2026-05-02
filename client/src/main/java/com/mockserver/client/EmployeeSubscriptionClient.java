package com.mockserver.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.stereotype.Component;

@Component
public class EmployeeSubscriptionClient implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EmployeeSubscriptionClient.class);

    private final WebSocketGraphQlClient graphQlClient;

    public EmployeeSubscriptionClient(WebSocketGraphQlClient graphQlClient) {
        this.graphQlClient = graphQlClient;
    }

    @Override
    public void run(String... args) {
        String createdSubscription = """
                subscription {
                    employeeCreated {
                        id
                        name
                        email
                        department
                    }
                }
                """;

        String updatedSubscription = """
                subscription {
                    employeeUpdated {
                        id
                        name
                        email
                        department
                    }
                }
                """;

        String deletedSubscription = """
                subscription {
                    employeeDeleted {
                        id
                        name
                        email
                        department
                    }
                }
                """;

        graphQlClient.document(createdSubscription)
                .retrieveSubscription("employeeCreated")
                .toEntity(Employee.class)
                .subscribe(
                        employee -> log.info("CREATED => {}", employee),
                        error -> log.error("employeeCreated subscription error", error),
                        () -> log.info("employeeCreated subscription completed")
                );

        graphQlClient.document(updatedSubscription)
                .retrieveSubscription("employeeUpdated")
                .toEntity(Employee.class)
                .subscribe(
                        employee -> log.info("UPDATED => {}", employee),
                        error -> log.error("employeeUpdated subscription error", error),
                        () -> log.info("employeeUpdated subscription completed")
                );

        graphQlClient.document(deletedSubscription)
                .retrieveSubscription("employeeDeleted")
                .toEntity(Employee.class)
                .subscribe(
                        employee -> log.info("DELETED => {}", employee),
                        error -> log.error("employeeDeleted subscription error", error),
                        () -> log.info("employeeDeleted subscription completed")
                );

        log.info("Subscription client started on port 8082 and listening for employee events");
    }
}