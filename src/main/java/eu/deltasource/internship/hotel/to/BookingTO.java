package eu.deltasource.internship.hotel.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate from;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
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
	 * @param bookingModel
	 */
	public BookingTO(Booking bookingModel) {
		this.bookingId = bookingModel.getBookingId();
		this.guestId = bookingModel.getGuestId();
		this.roomId = bookingModel.getRoomId();
		this.numberOfPeople = bookingModel.getNumberOfPeople();
		this.from = bookingModel.getFrom();
		this.to = bookingModel.getTo();
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

	/**
	 * Empty constructor -needed for Json
	 */
	public BookingTO() {
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}
}
