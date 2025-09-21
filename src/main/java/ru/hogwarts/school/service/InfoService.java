package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

@Service
public class InfoService {
    @Autowired
    private ServerProperties serverProperties;

   // @Value("${server.port}")
    private Integer serverPort;
    public InfoService(){
        this.serverProperties = serverProperties;
    }
    public Integer getServerPort(){
        serverPort = serverProperties.getPort();
    return serverPort;
    }
}
