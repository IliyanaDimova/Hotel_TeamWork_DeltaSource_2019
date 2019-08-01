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
		setBookingDates(from, to);
	}

	/**
	 * Constructor that constructs a TransferBooking by another TransferBooking
	 * @param booking
	 */
	public BookingTO(BookingTO booking) {
		this.guestId = booking.guestId;
		this.roomId = booking.roomId;
		this.numberOfPeople = booking.numberOfPeople;
		setBookingDates(booking.from, booking.to);
	}

	/**
	 * Sets the booking dates
	 * @param from
	 * @param to
	 */
	public void setBookingDates(LocalDate from, LocalDate to) {
		try {
			if (from.isAfter(to) || to.equals(from) || from.isBefore(LocalDate.now())) {
				throw new FailedInitializationException("Invalid dates given!");
			}
			this.from = from;
			this.to = to;
		} catch (NullPointerException npe) {
			throw new FailedInitializationException("Date parameters are null!");
		}
	}
}
