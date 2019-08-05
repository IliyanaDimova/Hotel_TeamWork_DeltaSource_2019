package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.domain.Booking;

import java.time.LocalDate;

/**
 * Transfer Object of Booking
 */
public class BookingTO {

	private int bookingId;
	private int guestId;
	private int roomId;

	private int numberOfPeople;

	private LocalDate from;
	private LocalDate to;

	/**
	 * Constructor with all params
	 *
	 * @param guestId
	 * @param roomId
	 * @param numberOfPeople
	 * @param from
	 * @param to
	 */
	public BookingTO(int bookingId, int guestId, int roomId, int numberOfPeople, LocalDate from, LocalDate to) {
		this.bookingId = bookingId;
		this.guestId = guestId;
		this.roomId = roomId;
		this.numberOfPeople = numberOfPeople;
		this.from = from;
		this.to = to;
	}

	/**
	 * @param booking
	 */
	public BookingTO(Booking booking) {
		this.bookingId = booking.getBookingId();
		this.guestId = booking.getGuestId();
		this.roomId = booking.getRoomId();
		this.numberOfPeople = booking.getNumberOfPeople();
		this.from = booking.getFrom();
	}

	/**
	 * Getter for booking ID field
	 *
	 * @return int - ID of the booking
	 */

	public int getBookingId() {
		return bookingId;
	}

	/**
	 * Getter for from date of the booking
	 *
	 * @return from date of the booking
	 */
	public LocalDate getFrom() {
		return from;
	}

	/**
	 * Getter for to date of the booking
	 *
	 * @return to date of the booking
	 */
	public LocalDate getTo() {
		return to;
	}

	/**
	 * Getter for room id of the booking
	 *
	 * @return room id of the booking
	 */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * Returns the amount of people for which the room is
	 *
	 * @return number of people
	 */
	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	/**
	 * Getter for guest id of the booking
	 *
	 * @return room id of the booking
	 */
	public int getGuestId() {
		return guestId;
	}

}
