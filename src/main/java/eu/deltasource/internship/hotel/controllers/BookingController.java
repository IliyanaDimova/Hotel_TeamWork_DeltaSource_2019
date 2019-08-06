package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.service.BookingService;
import eu.deltasource.internship.hotel.to.BookingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateBooking(@RequestBody BookingTO bookingTO) {
		bookingService.updateBooking(bookingTO.getRoomId(), bookingTO.getFrom(), bookingTO.getTo());
	}
}
