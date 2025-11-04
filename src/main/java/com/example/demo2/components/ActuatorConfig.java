package com.example.demo2.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class ActuatorConfig implements HealthIndicator {
    @Override
    public Health health() {
        try {
            String computerName = InetAddress.getLocalHost().getHostName();
            return Health.up().withDetail("Computer name", computerName).build();

        } catch (Exception e) {
            return Health.down().withDetail("Error", e.getMessage()).build();
        }

    }
}
