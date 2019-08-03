package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.to.BookingTO;

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
	 * @param bookingTO id of the guest
	 */
	public void createBooking(BookingTO bookingTO) {

		validateBooking(bookingTO);

		if (bookingRepository.existsById(bookingTO.getBookingId())) {
			throw new InvalidParameterException("Booking with this ID already exists.");
		}

		validateRoom(bookingTO);

		Room room = roomService.getRoomById(bookingTO.getRoomId());

		//todo include in validate dates

		bookingRepository.save(covertBookingTOtoBookingModel(bookingTO));
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
		validateBookingID(id);
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
		validateBookingID(id);
		return bookingRepository.deleteById(id);
	}

	//todo not working correctly, fix

	/**
	 * Updates a booking by using it's ID and throws, if the booking is overlapping with another one
	 *
	 * @param id   id of the booking to be updated
	 * @param from new from date
	 * @param to   new to date
	 */
	public void updateBooking(int id, LocalDate from, LocalDate to) {
		validateBookingID(id);

		Booking book = bookingRepository.findById(id);

		for (Booking booking : bookingRepository.findAll()) {
			if (booking.getRoomId() == book.getRoomId() && isOverlapping(from, to, booking) && id
				!= booking.getBookingId()) {
				throw new InvalidParameterException("Booking overlaps with another one");
			}
		}

		book.setBookingDates(from, to);
	}

	private void overlapChecker(BookingTO booking) {
		for (Booking current : bookingRepository.findAll()) {
			if (current.getRoomId() == booking.getRoomId() && isOverlapping(booking.getFrom(),
				booking.getTo(), current) && booking.getBookingId() == current.getBookingId()) {
				throw new InvalidParameterException("Booking overlaps with another one");
			}
		}
	}

	private boolean isOverlapping(LocalDate from, LocalDate to, Booking book) {
		return !(book.getFrom().isAfter(to) || book.getTo().isBefore(from) || book.getTo().equals(from));
	}

	private void validateBooking(BookingTO booking) {
		if (booking == null) {
			throw new InvalidParameterException("Booking cannot be null");
		} else if (booking.getFrom() == null) {
			throw new InvalidParameterException("From date cannot be null");
		} else if (booking.getTo() == null) {
			throw new InvalidParameterException("To date cannot be null");
		}

		if (booking.getTo().isBefore(booking.getFrom())) {
			throw new InvalidParameterException("To date cannot be after from date.");
		}

		if (!guestService.existsById(booking.getGuestId())) {
			throw new InvalidParameterException("Guest with this ID does not exist.");
		}

		if (!roomService.existsById(booking.getRoomId())) {
			throw new InvalidParameterException("Room does not exist.");
		}

	}


	private boolean isSpaceEnough(int peopleCount, Room room) {
		return room.getRoomCapacity() >= peopleCount;
	}

	/**
	 * Validates the room requested by the booking. Checks if the room exists, checks if the space in the room is
	 * enough
	 *
	 * @param booking
	 */
	private void validateRoom(BookingTO booking) {
		if (!roomService.existsById(booking.getRoomId())) {
			throw new InvalidParameterException("Room does not exist.");
		}

		if (!isSpaceEnough(booking.getNumberOfPeople(), roomService.getRoomById(booking.getRoomId()))) {
			throw new InvalidParameterException("Not enough space in room");
		}


	}

	private void validateBookingID(int bookingID) {
		if (!bookingRepository.existsById(bookingID)) {
			throw new InvalidParameterException("Booking does not exist.");
		}
	}

	private Booking covertBookingTOtoBookingModel(BookingTO bookingTO) {
		Booking book = new Booking(bookingTO.getBookingId(), bookingTO.getGuestId(), bookingTO.getRoomId(),
			bookingTO.getNumberOfPeople(),
			bookingTO.getFrom(), bookingTO.getTo());
		return book;
	}


}
