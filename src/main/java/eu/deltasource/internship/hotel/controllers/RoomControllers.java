package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rooms")

public class RoomControllers {

    private final RoomService roomService;

    public RoomControllers(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    List<Room> getAllRooms() {
        return roomService.findRooms();
    }

    @PostMapping("/rooms")
    Room addNewRoom(@RequestBody Room newRoom) {
        return roomService.saveRoom(newRoom);
    }

    @PostMapping("/rooms")
    void addNewRoom(@RequestBody Room... rooms) {
        roomService.saveRooms(rooms);
    }

    @GetMapping("/rooms/{id}")
    Room getRoomById(@PathVariable int id){
        return roomService.getRoomById(id);
    }
}
