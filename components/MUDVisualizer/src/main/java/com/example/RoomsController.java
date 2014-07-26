package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@EnableAutoConfiguration
public class RoomsController {

    private DoorRepository doorRepository = new DoorRepository();

    @RequestMapping("/")
    @ResponseBody
    Collection<Door> home() {
        return doorRepository.getDoors();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RoomsController.class, args);
    }
}