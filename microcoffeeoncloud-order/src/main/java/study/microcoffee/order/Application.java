package study.microcoffee.order;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Spring Boot application of the microservice.
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static boolean runFromIDE = false;

    public static void main(String[] args) {
        logHostInfo();

        SpringApplication app = new SpringApplication(Application.class);

        if (runFromIDE) {
            setProfileAndProperties(app);
        }

        app.run(args);
    }

    private static void setProfileAndProperties(SpringApplication app) {
        app.setAdditionalProfiles("devlocal");

        System.setProperty("javax.net.ssl.trustStore", "target/keystore/microcoffee-keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "12345678");
    }

    private static void logHostInfo() {
        try {
            logger.info("Hostname:   {}", InetAddress.getLocalHost().getHostName());
            logger.info("IP address: {}", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            logger.warn("Failed to read host info.", e);
        }
    }
}
