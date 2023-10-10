package com.thehecklers.neoai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestNeoaiApplication {
    public static void main(String[] args) {
        SpringApplication.from(NeoaiApplication::main).with(TestNeoaiApplication.class).run(args);
    }
}