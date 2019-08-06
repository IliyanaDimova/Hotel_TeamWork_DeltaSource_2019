package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.exception.InvalidDateException;
import eu.deltasource.internship.hotel.service.BookingService;
import eu.deltasource.internship.hotel.to.BookingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createBooking(@RequestBody BookingTO newBooking) {
		bookingService.createBooking(newBooking);
	}

	@GetMapping
	public List<Booking> getAllBookings() {
		return bookingService.getAllBookings();
	}

	@GetMapping(value = "/{id}")
	public Booking findById(@PathVariable(value = "id") int id) {
		return bookingService.getBookingById(id);
	}

	@DeleteMapping(value = "/{id}")
	public boolean deleteByID(@PathVariable(value = "id") int id) {
		return bookingService.removeBookingById(id);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateBooking(@RequestBody BookingTO bookingTO, @RequestParam int id) {
		bookingService.updateBooking(bookingTO);
	}

	/*@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateBooking(@RequestBody List<LocalDate> fromTo, @RequestParam int id) {
		if(fromTo.size()==2) {
			bookingService.updateBooking(id, fromTo.get(0), fromTo.get(1));
		}
		else{
			throw new InvalidDateException("You can't update with more than 2 dates - from and to!");
		}
	}*/
}
