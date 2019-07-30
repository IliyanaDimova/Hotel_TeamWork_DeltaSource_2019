package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.domain.Hotel;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.*;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eu.deltasource.internship.hotel.domain.commodity.BedType.SINGLE;

public class BookingServiceTests {

	private static BookingRepository bookingRepository;
	private static GuestRepository guestRepository;
	private static RoomRepository roomRepository;
	private static GuestService guestService;
	private static RoomService roomService;
	private static BookingService bookingService;

	@BeforeEach
	void setUp() {
		Guest guest = new Guest(5, "gosho", "minka", Gender.FEMALE);
		bookingRepository = new BookingRepository();
		guestRepository = new GuestRepository();
		guestRepository.save(guest);
		roomRepository = new RoomRepository();
		guestService = new GuestService(guestRepository);
		roomService = new RoomService(roomRepository);
		bookingService = new BookingService(bookingRepository, roomService, guestService);

		//hotel API

		Hotel hotel = new Hotel(bookingService, guestService, roomService);

		// Filling up hotel with ready rooms to use

		// Comodities for a double room
		AbstractCommodity doubleBed = new Bed(BedType.DOUBLE);
		AbstractCommodity toilet = new Toilet();
		AbstractCommodity shower = new Shower();

		Set<AbstractCommodity> doubleSet = new HashSet<>(Arrays.asList(doubleBed, toilet, shower));

		// commodities for a single room
		Set<AbstractCommodity> singleSet = new HashSet<>(Arrays.asList(new Bed(SINGLE), new Toilet(), new Shower()));

		// commodities for a double room with king size bed
		Set<AbstractCommodity> kingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Toilet(),
			new Shower()));

		// commodities for a 3 person room with a king size and a single
		Set<AbstractCommodity> threePeopleKingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE),
			new Bed(SINGLE), new Toilet(), new Shower()));

		// commodities for a 4 person room with 2 doubles
		Set<AbstractCommodity> fourPersonSet = new HashSet<>(Arrays.asList(new Bed(BedType.DOUBLE),
			new Bed(BedType.DOUBLE), new Toilet(), new Shower()));

		// commodities for a 4 person room with 2 doubles
		Set<AbstractCommodity> fivePersonSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE),
			new Bed(BedType.DOUBLE), new Bed(SINGLE), new Toilet(), new Toilet(), new Shower()));

		// create some rooms
		Room doubleRoom = new Room(1, doubleSet);
		Room singleRoom = new Room(1, singleSet);
		Room kingSizeRoom = new Room(1, kingSizeSet);
		Room threePeopleKingSizeRoom = new Room(1, threePeopleKingSizeSet);
		Room fourPersonRoom = new Room(1, fourPersonSet);
		Room fivePersonRoom = new Room(1, fivePersonSet);

		// adds the rooms to the repository, which then can be accesses from the RoomService
		roomService.saveRooms(doubleRoom, singleRoom, kingSizeRoom, threePeopleKingSizeRoom, fourPersonRoom,
			fivePersonRoom);
	}

	@Test
	public void createBooking_throwsInvalidParameter_whenPassedNull() {
		//given
		setUp();
		// when
		//then
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bookingService.createBooking(1,
				1, 1, 1, null, null);
		});
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bookingService.createBooking(1,
				1, 1, 1, LocalDate.now(), null);
		});
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bookingService.createBooking(1,
				1, 1, 1, LocalDate.now(), LocalDate.now().minusDays(1));
		});
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bookingService.createBooking(1,
				1, -1, 1, LocalDate.now(), LocalDate.now().plusDays(1));
		});
	}

	@Test
	public void createBooking_createsBooking_whenCorrectParameters() {
		//given
		setUp();
		//when
		bookingService.createBooking(1, 1, 1, 1, LocalDate.now(),
			LocalDate.now().plusDays(1));
		//then
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bookingService.createBooking(1, 1, 1, 1, LocalDate.now(),
				LocalDate.now().plusDays(1));
		});
	}

	@Test
	public void getAllBookings_returnsBOoking() {
		//given
		setUp();
		//when
		bookingService.createBooking(1, 1, 1, 1, LocalDate.now(),
			LocalDate.now().plusDays(1));
		//then
		Assertions.assertEquals(bookingService.getAllBookings().size(), 1);
	}

	@Test
	public void updateBooking_updatesBooking_singleBooking() {
		//given
		setUp();
		bookingService.createBooking(1, 1, 1, 1, LocalDate.now(),
			LocalDate.now().plusDays(1));
		//when
		bookingService.updateBooking(1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));
		//then
		Assertions.assertEquals(LocalDate.now().plusDays(2), bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(LocalDate.now().plusDays(3), bookingService.getBookingById(1).getTo());
	}

	@Test
	public void updateBooking_throws_whenOverlapping() {
		//given
		setUp();
		bookingService.createBooking(1, 1, 1, 1, LocalDate.now(),
			LocalDate.now().plusDays(1));
		bookingService.createBooking(2, 1, 1, 1, LocalDate.now().plusDays(5),
			LocalDate.now().plusDays(7));
		//when
		//then
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			bookingService.updateBooking(1, LocalDate.now()
				.plusDays(2), LocalDate.now().plusDays(8));
		});

	}


}
