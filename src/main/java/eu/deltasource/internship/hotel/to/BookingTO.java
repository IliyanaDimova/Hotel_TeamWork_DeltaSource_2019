package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;

import java.time.LocalDate;

/**
 * Transfer Object of Booking - has no ID
 */
public class BookingTO {

	private int guestId;
	private int roomId;

	private int numberOfPeople;

	private LocalDate from;
	private LocalDate to;

	/**
	 * Constructor with all params
	 * @param guestId
	 * @param roomId
	 * @param numberOfPeople
	 * @param from
	 * @param to
	 */
	public BookingTO(int guestId, int roomId, int numberOfPeople, LocalDate from, LocalDate to) {
		this.guestId = guestId;
		this.roomId = roomId;
		this.numberOfPeople = numberOfPeople;
		this.from = from;
		this.to = to;
	}

	/**
	 * Constructor that constructs a TransferBooking by another TransferBooking
	 * @param booking
	 */
	public BookingTO(Booking booking) {
		this.guestId = booking.getGuestId();
		this.roomId = booking.getRoomId();
		this.numberOfPeople = booking.getNumberOfPeople();
		this.from = booking.getFrom();
	}

	/**
	 * Getter for from date of the booking
	 * @return from date of the booking
	 */
	public LocalDate getFrom(){
		return from;
	}

	/**
	 * Getter for to date of the booking
	 * @return to date of the booking
	 */
	public LocalDate getTo(){
		return to;
	}

	/**
	 * Getter for room id of the booking
	 * @return room id of the booking
	 */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * Returns the amount of people for which the room is
	 * @return number of people
	 */
	public int getNumberOfPeople(){
		return numberOfPeople;
	}

	/**
	 * Getter for guest id of the booking
	 * @return room id of the booking
	 */
	public int getGuestId() {
		return guestId;
	}

}
