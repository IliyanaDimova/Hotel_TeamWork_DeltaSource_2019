package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Taner Ilyazov - Delta Source Bulgaria on 2019-07-28.
 */
@RequestMapping("/booking")
public class BookingService {

    private final BookingRepository bookingRepository;

    private final RoomService roomService;

    private final GuestService guestService;

    public BookingService(BookingRepository bookingRepository, RoomService roomService, GuestService guestService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    //todo annotate accordingly & map
    /**
     * Adds a new entry to the repository, throws invalid param exception, if the booking is null or if the
     * booking
     * has invalid date values
     *
     * @param bookingId      id of the booking
     * @param guestId        id of the guest
     * @param roomId         id of the room
     * @param numberOfPeople the amount of people
     * @param from           start date of the booking
     * @param to             end date of the booking
     */
    public void createBooking(int bookingId, int guestId, int roomId, int numberOfPeople, LocalDate from,
                              LocalDate to) {

        if (from == null) {
            throw new InvalidParameterException("Date cannot be null.");
        }
        if (to == null) {
            throw new InvalidParameterException("Date cannot be null.");
        }
        if (to.isBefore(from)) {
            throw new InvalidParameterException("Booking has invalid date values.");
        }

        if (bookingRepository.existsById(bookingId)) {
            throw new InvalidParameterException("Booking with this ID already exists.");
        }

        if (!guestService.existsById(guestId)) {
            throw new InvalidParameterException("Guest with this ID does not exist.");
        }

        if (!roomService.existsById(roomId)) {
            throw new InvalidParameterException("Room does not exist.");
        }

        Room room = roomService.getRoomById(roomId);

        if (isSpaceEnough(numberOfPeople, room)) {
            throw new InvalidParameterException("Not enough space in room");
        }

        for (Booking booking : bookingRepository.findAll()) {
            if (checkForOverlappingDates(from, to, booking)) {
                throw new InvalidParameterException("Booking overlaps with another one");
            }
        }
        bookingRepository.save(new Booking(bookingId, guestId, roomId, numberOfPeople, from, to));
    }

    /**
     * Returns all of the bookings
     * @return a read only list of the bookings 
     */

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }


    private boolean checkForOverlappingDates(LocalDate from, LocalDate to, Booking book) {
        return !(book.getFrom().isAfter(to) || book.getTo().isBefore(from) || book.getTo().equals(from));
    }

    private boolean isSpaceEnough(int peopleCount, Room room) {
        return room.getRoomCapacity() >= peopleCount;
    }

}
