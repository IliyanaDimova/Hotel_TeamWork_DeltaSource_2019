package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.service.RoomService;
import eu.deltasource.internship.hotel.to.RoomTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@GetMapping
	List<Room> getAllRooms() {
		return roomService.findRooms();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	void addNewRooms(@RequestBody RoomTO... rooms) {
		roomService.createRooms(rooms);
	}

	@GetMapping("/{id}")
	Room getRoomById(@PathVariable int id) {
		return roomService.getRoomById(id);
	}

	@DeleteMapping("/{id}")
	void deleteRoomById(@PathVariable int id){
		roomService.deleteRoomById(id);
	}

	@PutMapping("/{id}")
	void deleteRoomById(@PathVariable int id, @RequestBody RoomTO roomTO){
		roomService.updateRoom(id,roomTO);
	}
}
