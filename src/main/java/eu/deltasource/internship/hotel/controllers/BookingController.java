package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.service.BookingService;
import eu.deltasource.internship.hotel.to.BookingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	//todo change all of the request bodies to strings which are goint to be json
	//todo map the jason strings to BookingTOs using jackson

	@PostMapping
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

	@PutMapping(value = "/{id}")
	public void updateBooking(@PathVariable(value = "id") int bookingID, @RequestBody LocalDate from, LocalDate to) {
		bookingService.updateBooking(bookingID, from, to);
	}
}
