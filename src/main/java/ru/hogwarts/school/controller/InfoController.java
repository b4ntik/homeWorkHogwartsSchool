package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${server.port}")
    private int port;


    @GetMapping("/port")
    public Integer getServerPort() {
        return port;
    }
    @GetMapping("/getResult")
    public String getResult() {
        long sum = 0;
        for (long i = 0; i <= 1000000; i++){
            sum = sum +i;
        }
        return "Результат: " + sum;
    }
}
