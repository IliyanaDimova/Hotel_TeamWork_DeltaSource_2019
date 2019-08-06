package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.service.GuestService;
import eu.deltasource.internship.hotel.to.GuestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guests")
public class GuestController {

	@Autowired
	public GuestService guestService;

	@GetMapping
	public List<Guest> returnAllGuests() {
		return guestService.returnAllGuests();
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuest(@RequestBody GuestTO guest) {
		guestService.createGuest(guest);
	}

	@GetMapping(value = "/{id}")
	public Guest returnGuestById(@PathVariable("id") int id) {
		return guestService.returnGuestById(id);
	}

	@DeleteMapping(value = "/{id}")
	public void removeGuestById(@PathVariable("id") int id) {
		guestService.removeGuestById(id);
	}

	@PutMapping(value = "/{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateGuestById(@PathVariable int id, @RequestBody GuestTO guest) {
		guestService.updateGuestById(id, guest);
	}
}
