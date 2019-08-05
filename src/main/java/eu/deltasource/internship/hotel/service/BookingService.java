package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.exception.InvalidDateException;
import eu.deltasource.internship.hotel.exception.InvalidBookingException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.to.BookingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Taner Ilyazov - Delta Source Bulgaria on 2019-07-28.
 */
@Service
public class BookingService {

	private final BookingRepository bookingRepository;

	private final RoomService roomService;

	private final GuestService guestService;

	@Autowired
	public BookingService(BookingRepository bookingRepository, RoomService roomService, GuestService guestService) {
		this.bookingRepository = bookingRepository;
		this.roomService = roomService;
		this.guestService = guestService;
	}

	/**
	 * Adds a new entry to the repository, throws invalid param exception, if the booking is null or if the
	 * booking has a from value which is greater than the to value
	 *
	 * @param bookingTO id of the guest
	 */
	public void createBooking(BookingTO bookingTO) {

		validateBooking(bookingTO);

		validateRoom(bookingTO);

		Room room = roomService.getRoomById(bookingTO.getRoomId());

		bookingRepository.save(covertBookingTOtoBookingModel(bookingTO));
	}

	/**
	 * Returns all of the bookings
	 */
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	/**
	 * Returns a booking by it's id, throws if the booking does not exist
	 * @param id id of the booking
	 * @return the booking which has matching id
	 */
	public Booking getBookingById(int id) {
		validateBookingID(id);
		return bookingRepository.findById(id);
	}

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
				throw new InvalidDateException("Booking overlaps with another one");
			}
		}

		book.setBookingDates(from, to);
	}

	/*public void updateBooking(BookingTO bookingTO) {
		validateBooking(bookingTO);

		Booking book = bookingRepository.findById(bookingTO.getBookingId());

		for (Booking booking : bookingRepository.findAll()) {
			if (booking.getRoomId() == book.getRoomId() && isOverlapping(bookingTO.getFrom(), bookingTO.getTo(),
				booking) && bookingTO.getBookingId()
				!= booking.getBookingId()) {
				throw new InvalidDateException("Booking overlaps with another one");
			}
		}
	}*/

	private void overlapChecker(BookingTO booking) {
		for (Booking current : bookingRepository.findAll()) {
			if (current.getRoomId() == booking.getRoomId() && isOverlapping(booking.getFrom(),
				booking.getTo(), current) && booking.getBookingId() == current.getBookingId()) {
				throw new InvalidDateException("Booking overlaps with another one");
			}
		}
	}

	private boolean isOverlapping(LocalDate from, LocalDate to, Booking book) {
		return !(book.getFrom().isAfter(to) || book.getTo().isBefore(from) || book.getTo().equals(from));
	}

	/**
	 * Completely validates a booking. Checks if the BookingTO refference is pointing towards a null object, throws
	 * if it is. Checks if every refference type field points towards a null object, throws if it is. Checks if the
	 * dates are valid, throws, if they are not. Checks if a booking with the same ID exists and throws, if it does.
	 * Checks, if
	 * @param booking
	 */
	private void validateBooking(BookingTO booking) {
		if (booking == null) {
			throw new InvalidBookingException("Booking cannot be null");
		} else if (booking.getFrom() == null) {
			throw new InvalidDateException("From date cannot be null");
		} else if (booking.getTo() == null) {
			throw new InvalidDateException("To date cannot be null");
		}

		if (booking.getTo().isBefore(booking.getFrom())) {
			throw new InvalidDateException("To date cannot be after from date.");
		}

		if (!guestService.existsById(booking.getGuestId())) {
			throw new ItemNotFoundException("Guest with this ID does not exist.");
		}

		if (!roomService.existsById(booking.getRoomId())) {
			throw new ItemNotFoundException("Room with this ID does not exist.");
		}

	}

	/**
	 * Checks if the space in the room is enough for the amount of people.
	 */
	private boolean isSpaceEnough(int necessarySpace, Room room) {
		return room.getRoomCapacity() >= necessarySpace;
	}

	/**
	 * Validates the room requested by the booking. Checks if the room exists, checks if the space in the room is
	 * enough and throws, if either of the conditions aren't met.
	 * @param booking id of the booking
	 */
	private void validateRoom(BookingTO booking) {
		if (!roomService.existsById(booking.getRoomId())) {
			throw new ItemNotFoundException("Room does not exist.");
		}

		if (!isSpaceEnough(booking.getNumberOfPeople(), roomService.getRoomById(booking.getRoomId()))) {
			throw new InvalidBookingException("Not enough space in room");
		}

		overlapChecker(booking);
	}


	/**
	 * Checks if a booking exists by it's ID and throws an exception, if it does not.
	 */
	private void validateBookingID(int bookingID) {
		if (!bookingRepository.existsById(bookingID)) {
			throw new ItemNotFoundException("Booking does not exist.");
		}
	}

	/**
	 * Converts the bookingTO to a Booking
	 */
	private Booking covertBookingTOtoBookingModel(BookingTO bookingTO) {
		Booking book = new Booking(bookingTO.getBookingId(), bookingTO.getGuestId(), bookingTO.getRoomId(),
			bookingTO.getNumberOfPeople(),
			bookingTO.getFrom(), bookingTO.getTo());
		return book;
	}


}
