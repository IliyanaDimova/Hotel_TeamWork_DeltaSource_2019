package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rooms")

public class RoomControllers {

    private final RoomRepository roomRepository;

    public RoomControllers(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
}
