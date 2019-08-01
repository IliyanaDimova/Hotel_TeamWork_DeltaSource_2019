package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("guests")

public class GuestControllers {
	GuestRepository guestRepository = new GuestRepository();
	GuestService guestService = new GuestService(guestRepository);

	@GetMapping
	@ResponseBody
	public List<Guest> returnAllGuests() {
		return guestService.returnAllGuests();
	}

	@PostMapping
	@ResponseBody
	public void returnCreateGuest(@PathVariable String firstName, @PathVariable String lastName, @PathVariable Gender gender) {
		guestService.createGuest(firstName, lastName, gender);
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	public void returnGuestById(@PathVariable int id) {
		guestService.returnGuestById(id);
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	@ResponseBody
	public void returnRemoveGuestById(@PathVariable int id) {
		guestService.removeGuestById(id);
	}

	@RequestMapping(value = "/{id}", method = POST)
	@ResponseBody
	public void returnUpdateGuestById(@PathVariable int id, @PathVariable String firstName, @PathVariable String lastName, @PathVariable Gender gender) {
		guestService.updateGuestById(id, firstName, lastName, gender);
	}
}
