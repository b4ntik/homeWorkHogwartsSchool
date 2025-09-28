package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    public InfoService (){
    }
    public String getResult(){
        long sum = 0;
        for (long i = 0; i <= 1000000; i++){
            sum += i;
        }
        return "Результат: " + sum;
    }
}
