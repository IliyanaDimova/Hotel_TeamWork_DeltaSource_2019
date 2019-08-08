package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.exception.InvalidBookingException;
import eu.deltasource.internship.hotel.exception.InvalidDateException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.to.BookingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for Booking
 * Has BookingRepository field, RoomService field and GuestService field
 */
@Service
public class BookingService {

	private final BookingRepository bookingRepository;

	private final RoomService roomService;

	private final GuestService guestService;


	@Autowired
	/**
	 * Constructor - gives value to all fields
	 */
	public BookingService(BookingRepository bookingRepository, RoomService roomService, GuestService guestService) {
		this.bookingRepository = bookingRepository;
		this.roomService = roomService;
		this.guestService = guestService;
	}

	/**
	 * Creates the booking for the first available room in the date range.
	 * @param bookingTO
	 */
	public void bookFirstAvailable(BookingTO bookingTO) {
		boolean roomBooked = false;
		validateBooking(bookingTO);
		for (Room room : roomService.findRooms()) {
			try {
				bookingTO.setRoomId(room.getRoomId());
				createBookingById(bookingTO);
				return;
			} catch (InvalidBookingException e) {

			}
		}
		if (!roomBooked) {
			throw new InvalidBookingException("No available rooms");
		}
	}

	/**
	 * Adds a new entry to the repository
	 * Throws invalid param exception if the booking is null or if the
	 * booking has a from value which is greater than the to value
	 *
	 * @param bookingTO transfer object for the guest to be added
	 */
	public void createBookingById(BookingTO bookingTO) {

		validateBooking(bookingTO);

		validateRoom(bookingTO);

		creationOverlapChecker(bookingTO);

		bookingRepository.save(covertBookingTOtoBookingModel(bookingTO));
	}

	/**
	 * Returns all of the bookings
	 */
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	/**
	 * Returns a booking by it's id
	 * Throws if the booking does not exist
	 *
	 * @param id - id of the booking
	 * @return the booking which has matching id
	 */
	public Booking getBookingById(int id) {
		validateBookingID(id);
		return bookingRepository.findById(id);
	}

	/**
	 * Attempts to delete the item with matching id, returns a boolean
	 *
	 * @param id - id of the item to be deleted
	 * @return boolean depending if the item was successfully deleted or not
	 */
	public boolean removeBookingById(int id) {
		validateBookingID(id);
		return bookingRepository.deleteById(id);
	}

	/**
	 * Updates a booking by using it's ID
	 * Throws if the booking is overlapping with another one
	 *
	 * @param id   id of the booking to be updated
	 * @param from new from date
	 * @param to   new to date
	 */
	public void updateBooking(int id, LocalDate from, LocalDate to) {
		validateBookingID(id);

		Booking currentBooking = getBookingById(id);

		Booking potentialBooking = new Booking(currentBooking);

		potentialBooking.setBookingDates(from, to);

		BookingTO potentialBookingTO = new BookingTO(potentialBooking);
		updateOverlapChecker(potentialBookingTO);

		currentBooking.setBookingDates(from, to);
	}

	/**
	 * Updates Booking by given Booking transfer object
	 * Throws if Booking has null fields or from date is after to date
	 * Throws if Booking with the given id does not exist
	 *
	 * @param bookingTO transfer object for Booking
	 */
	public void updateBooking(BookingTO bookingTO) {
		validateBooking(bookingTO);
		validateRoom(bookingTO);

		updateOverlapChecker(bookingTO);

		removeBookingById(bookingTO.getBookingId());

		createBookingById(bookingTO);
	}

	/**
	 * Checks, if the booking dates are overlapping with any of the existing bookings of the room and throws, if they
	 * are. To be used in the create booking method.
	 */
	//todo add method to controller & test
	private void creationOverlapChecker(BookingTO booking) {

		for (Booking current : bookingRepository.findAll()) {
			if (current.getRoomId() == booking.getRoomId() && isOverlapping(booking.getFrom(),
				booking.getTo(), current)) {
				throw new InvalidDateException("Booking overlaps with another one");
			}
		}
	}

	/**
	 * Checks, if the booking dates are overlapping with any of the existing bookings of the room and throws, if they
	 * are. To be used in the update booking method.
	 */
	private void updateOverlapChecker(BookingTO updatedBooking) {
		for (Booking currentBooking : bookingRepository.findAll()) {
			if (currentBooking.getRoomId() == updatedBooking.getRoomId() && isOverlapping(updatedBooking.getFrom(),
				updatedBooking.getTo(), currentBooking) && currentBooking.getBookingId() != updatedBooking.getBookingId()) {
				throw new InvalidDateException("Updated booking dates do overlap with another booking");
			}
		}
	}

	/**
	 * Checks if the dates of a booking are overlapping with the passed dates
	 */
	private boolean isOverlapping(LocalDate from, LocalDate to, Booking book) {
		boolean isNewFromAfterBookedTo = book.getFrom().isAfter(to);
		boolean isBookedToBeforeOrEqualFrom = book.getTo().isBefore(from) || book.getTo().equals(from);
		return !(isNewFromAfterBookedTo || isBookedToBeforeOrEqualFrom);
	}

	/**
	 * Completely validates a booking. Checks if the BookingTO reference is pointing towards a null object, throws
	 * if it is. Checks if every reference type field points towards a null object, throws if it is. Checks if the
	 * dates are valid, throws, if they are not. Checks if a booking with the same ID exists and throws, if it does.
	 *
	 * @param booking transfer Booking object to be checked
	 */
	private void validateBooking(BookingTO booking) {

		bookingNullCheck(booking);

		if (booking.getTo().isBefore(booking.getFrom())) {
			throw new InvalidDateException("To date cannot be after from date.");
		}

		if (booking.getFrom().isBefore(LocalDate.now())) {
			throw new InvalidDateException("Cannot book from a previous date.");
		}

		if (!guestService.existsById(booking.getGuestId())) {
			throw new ItemNotFoundException("Guest with this ID does not exist.");
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
	 *
	 * @param booking id of the booking
	 */
	private void validateRoom(BookingTO booking) {

		if (!isSpaceEnough(booking.getNumberOfPeople(), roomService.getRoomById(booking.getRoomId()))) {
			throw new InvalidBookingException("Not enough space in room");
		}
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
	 * Converts the BookingTO (Booking transfer object) to a Booking
	 */
	private Booking covertBookingTOtoBookingModel(BookingTO bookingTO) {
		Booking book = new Booking(bookingTO.getBookingId(), bookingTO.getGuestId(), bookingTO.getRoomId(),
			bookingTO.getNumberOfPeople(),
			bookingTO.getFrom(), bookingTO.getTo());
		return book;
	}

	/**
	 * Checks BookingTO (Transfer Booking object) and it's fields if they are null
	 */
	private void bookingNullCheck(BookingTO booking) {
		if (booking == null) {
			throw new InvalidBookingException("Booking cannot be null");
		} else if (booking.getFrom() == null) {
			throw new InvalidDateException("From date cannot be null");
		} else if (booking.getTo() == null) {
			throw new InvalidDateException("To date cannot be null");
		}
	}
}
