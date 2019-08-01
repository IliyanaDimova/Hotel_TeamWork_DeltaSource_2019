package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rooms")
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping
	List<Room> getAllRooms() {
		return roomService.findRooms();
	}

	@PostMapping
	Room addNewRoom(@RequestBody Room room) {
		return roomService.saveRoom(room);
	}

	@PostMapping
	void addNewRooms(@RequestBody Room... rooms) {
		roomService.saveRooms(rooms);
	}

	@GetMapping("{id}")
	Room getRoomById(@PathVariable int id) {
		return roomService.getRoomById(id);
	}
}
