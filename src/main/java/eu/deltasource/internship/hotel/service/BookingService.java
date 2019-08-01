package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.to.BookingTO;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Taner Ilyazov - Delta Source Bulgaria on 2019-07-28.
 */
public class BookingService {

	private final BookingRepository bookingRepository;

	private final RoomService roomService;

	private final GuestService guestService;

	public BookingService(BookingRepository bookingRepository, RoomService roomService, GuestService guestService) {
		this.bookingRepository = bookingRepository;
		this.roomService = roomService;
		this.guestService = guestService;
	}

	//todo annotate accordingly & map, service

	/**
	 * Adds a new entry to the repository, throws invalid param exception, if the booking is null or if the
	 * booking has a from value which is greater than the to value
	 *
	 * @param bookingId 	id of the booking
	 * @param bookingTO 	id of the guest
	 */
	public void createBooking(int bookingId, BookingTO bookingTO) {

		validateBooking(bookingTO);

		if (bookingRepository.existsById(bookingId)) {
			throw new InvalidParameterException("Booking with this ID already exists.");
		}

		Room room = roomService.getRoomById(bookingTO.getRoomId());

		//todo include in validate dates

		bookingRepository.save(BookingTOtoModdel(bookingId, bookingTO));
	}

	/**
	 * Returns all of the bookings
	 *
	 * @return a read only list of the bookings
	 */
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	/**
	 * Returns a booking by it's id, throws if the booking does not exist
	 *
	 * @param id id of the booking
	 * @return the booking which has matching id
	 */
	public Booking getBookingById(int id) {
		return bookingRepository.findById(id);
	}

	//todo possible param check

	/**
	 * Attempts to delete the item with matching id, returns a boolean
	 *
	 * @param id id of the item to be deleted
	 * @return boolean depending if the item was successfully deleted or not
	 */
	public boolean removeBookingById(int id) {
		if (!bookingRepository.existsById(id)) {
			throw new InvalidParameterException("Booking with id does not exist");
		}
		return bookingRepository.deleteById(id);
	}

	//todo not working correctly, fix

	/**
	 * Updates a booking
	 *
	 * @param id   id of the booking to be updated
	 * @param from new from date
	 * @param to   new to date
	 */
	public void updateBooking(int id, LocalDate from, LocalDate to) {
		Booking book = bookingRepository.findById(id);

		for (Booking booking : bookingRepository.findAll()) {
			if (checkForOverlappingDates(from, to, booking) && booking.getRoomId() == book.getRoomId()) {
				throw new InvalidParameterException("Booking overlaps with another one");
			}
		}

		book.setBookingDates(from, to);
	}


	private boolean checkForOverlappingDates(LocalDate from, LocalDate to, Booking book) {
		return !(book.getFrom().isAfter(to) || book.getTo().isBefore(from) || book.getTo().equals(from));
	}

	private void validateBooking(BookingTO booking) {
		validateDates(booking.getFrom(), booking.getTo());


		if (!guestService.existsById(booking.getGuestId())) {
			throw new InvalidParameterException("Guest with this ID does not exist.");
		}

		if (!roomService.existsById(booking.getGuestId())) {
			throw new InvalidParameterException("Room does not exist.");
		}

	}


	private boolean isSpaceEnough(int peopleCount, Room room) {
		return room.getRoomCapacity() >= peopleCount;
	}

	private void validateDates(LocalDate from, LocalDate to) {
		if(from == null) {
			throw new InvalidParameterException("From date cannot be null");
		}
		else if(to == null) {
			throw new InvalidParameterException("To date cannot be null");
		}
		if(to.isBefore(from)){
			throw new InvalidParameterException("To date cannot be after from date.");
		}

	}

	private void validateRoom(BookingTO booking){
		if(!roomService.existsById(booking.getRoomId())){
			throw new InvalidParameterException("Room does not exist.");
		}

		if (!isSpaceEnough(booking.getNumberOfPeople(), roomService.getRoomById(booking.getRoomId()))) {
			throw new InvalidParameterException("Not enough space in room");
		}

		for (Booking current : bookingRepository.findAll()) {
			if (checkForOverlappingDates(booking.getFrom(), booking.getTo(), current) && current.getRoomId() != booking.getRoomId()) {
				throw new InvalidParameterException("Booking overlaps with another one");
			}
		}
	}

	private Booking BookingTOtoModdel(int id, BookingTO booking){
		Booking book = new Booking(id, booking.getGuestId(), booking.getGuestId(), booking.getNumberOfPeople(),
			booking.getFrom(), booking.getTo());
		return book;
	}


}
