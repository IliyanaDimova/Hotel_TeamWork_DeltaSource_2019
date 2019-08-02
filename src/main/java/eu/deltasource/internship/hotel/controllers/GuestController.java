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
		guestService.createGuest(new GuestTO(guest.getFirstName(), guest.getLastName(), guest.getGender()));
	}

	@GetMapping(value = "/{id}")
	public void returnGuestById(@PathVariable("id") int id) {
		guestService.returnGuestById(id);
	}

	@DeleteMapping(value = "/{id}")
	public void removeGuestById(@PathVariable("id") int id) {
		guestService.removeGuestById(id);
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateGuestById(@RequestBody Guest guest) {
		guestService.updateGuestById(guest.getGuestId(), new GuestTO(guest.getFirstName(), guest.getLastName(), guest.getGender()));
	}
}
