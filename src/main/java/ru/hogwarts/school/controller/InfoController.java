package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

@RestController
public class InfoController {

    @Value("${server.port}")
    private int port;

    @Autowired
    private InfoService infoService;

    public InfoController (InfoService infoService){ this.infoService = infoService ;}

    @GetMapping("/port")
    public Integer getServerPort() {
        return port;
    }
    @GetMapping("/getResult")
    public String getResult() {
        return infoService.getResult();
    }

}
