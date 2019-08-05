package eu.deltasource.internship.hotel.exception;

public class InvalidBookingException extends RuntimeException {

	public InvalidBookingException() {
	}

	public InvalidBookingException(String message) {
		super(message);
	}

	public InvalidBookingException(String message, Throwable cause) {
		super(message, cause);
	}
}
