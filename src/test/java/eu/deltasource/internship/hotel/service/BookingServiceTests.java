package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.domain.Hotel;
import eu.deltasource.internship.hotel.domain.commodity.BedType;
import eu.deltasource.internship.hotel.exception.InvalidBookingException;
import eu.deltasource.internship.hotel.exception.InvalidDateException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.to.BookingTO;
import eu.deltasource.internship.hotel.to.RoomTO;
import eu.deltasource.internship.hotel.to.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.to.commodityTOs.BedTO;
import eu.deltasource.internship.hotel.to.commodityTOs.ShowerTO;
import eu.deltasource.internship.hotel.to.commodityTOs.ToiletTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		Guest guest = new Guest(0, "gosho", "minka", Gender.FEMALE);
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

		Set<AbstractCommodityTO> doubleSet = new HashSet<AbstractCommodityTO>(Arrays.asList(new BedTO(BedType.DOUBLE)
			, new ToiletTO(), new ShowerTO()));
		// commodities for a single room
		Set<AbstractCommodityTO> singleSet = new HashSet<>(Arrays.asList(new BedTO(SINGLE), new ToiletTO(),
			new ShowerTO()));

		// commodities for a double room with king size bed
		Set<AbstractCommodityTO> kingSizeSet = new HashSet<>(Arrays.asList(new BedTO(BedType.KING_SIZE),
			new ToiletTO(),
			new ToiletTO()));

		// commodities for a 3 person room with a king size and a single
		Set<AbstractCommodityTO> threePeopleKingSizeSet = new HashSet<>(Arrays.asList(new BedTO(BedType.KING_SIZE),
			new BedTO(SINGLE), new ToiletTO(), new ToiletTO()));

		// commodities for a 4 person room with 2 doubles
		Set<AbstractCommodityTO> fourPersonSet = new HashSet<>(Arrays.asList(new BedTO(BedType.DOUBLE),
			new BedTO(BedType.DOUBLE), new ToiletTO(), new ShowerTO()));

		// commodities for a 4 person room with 2 doubles
		Set<AbstractCommodityTO> fivePersonSet = new HashSet<>(Arrays.asList(new BedTO(BedType.KING_SIZE),
			new BedTO(BedType.DOUBLE), new BedTO(SINGLE), new ToiletTO(), new ToiletTO(), new ShowerTO()));

		// create some rooms
		RoomTO doubleRoom = new RoomTO(doubleSet);
		RoomTO singleRoom = new RoomTO(singleSet);
		RoomTO kingSizeRoom = new RoomTO(kingSizeSet);
		RoomTO threePeopleKingSizeRoom = new RoomTO(threePeopleKingSizeSet);
		RoomTO fourPersonRoom = new RoomTO(fourPersonSet);
		RoomTO fivePersonRoom = new RoomTO(fivePersonSet);

		// adds the rooms to the repository, which then can be accesses from the RoomService
		roomService.createRooms(doubleRoom, singleRoom, kingSizeRoom, threePeopleKingSizeRoom, fourPersonRoom,
			fivePersonRoom);
	}

	@Test
	public void createBooking_throwsInvalidParameter_whenPassedNull() {
		//given
		// when
		//then
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDate yesterday = LocalDate.now().minusDays(1);

		Assertions.assertThrows(InvalidBookingException.class, () -> {
			bookingService.createBookingById(null);
		});
		Assertions.assertThrows(InvalidDateException.class, () -> {
			bookingService.createBookingById(new BookingTO(1, 1, 1, 1, null, null));
		});
		Assertions.assertThrows(InvalidDateException.class, () -> {
			bookingService.createBookingById(
				new BookingTO(1, 1, 1, 1, today, null));
		});
		Assertions.assertThrows(InvalidDateException.class, () -> {
			bookingService.createBookingById(
				new BookingTO(2, 1, 1, 1, today, yesterday));
		});
		Assertions.assertThrows(ItemNotFoundException.class, () -> {
			bookingService.createBookingById(
				new BookingTO(3, 1, -1, 1, today, tomorrow));
		});
		Assertions.assertThrows(ItemNotFoundException.class, () -> {
			bookingService.createBookingById(
				new BookingTO(4, -1, 1, 1, today, tomorrow));
		});
	}

	@Test
	public void updateBooking_continuallyUpdates_evenWhenOverlappingWithSelf() {
		//given
		LocalDate first = LocalDate.now();
		LocalDate fifth = LocalDate.now().plusDays(4);
		LocalDate eleventh = LocalDate.now().plusDays(10);
		LocalDate fifteenth = LocalDate.now().plusDays(14);
		LocalDate twentyFirst = LocalDate.now().plusDays(20);
		LocalDate twentyFifth = LocalDate.now().plusDays(25);
		LocalDate twentyThird = LocalDate.now().plusDays(22);

		bookingService.createBookingById(new BookingTO(1, 1, 1, 1, eleventh, fifteenth));
		//when
		//then
		bookingService.updateBooking(1, first, fifth);
		Assertions.assertEquals(first, bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(fifth, bookingService.getBookingById(1).getTo());
		bookingService.updateBooking(1, twentyFirst, twentyFifth);
		Assertions.assertEquals(twentyFirst, bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(twentyFifth, bookingService.getBookingById(1).getTo());
		bookingService.updateBooking(1, first, twentyThird);
		Assertions.assertEquals(first, bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(twentyThird, bookingService.getBookingById(1).getTo());
	}

	@Test
	public void getAllBookings_returnsBooking() {
		//given
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		//when
		BookingTO first = new BookingTO(1, 1, 1, 1, today,
			tomorrow);
		BookingTO second = new BookingTO(2, 1, 3, 2, today,
			tomorrow);
		bookingService.createBookingById(first);
		bookingService.createBookingById(second);
		//then
		Assertions.assertEquals(2, bookingService.getAllBookings().size());
		Assertions.assertTrue(isBookingTOequalToBooking(first, bookingService.getBookingById(1)));
		Assertions.assertTrue(isBookingTOequalToBooking(second, bookingService.getBookingById(2)));
	}

	@Test
	public void createBooking_throws_whenDatesOverlap() {
		//given
		LocalDate dayOne = LocalDate.now();
		LocalDate dayTwo = LocalDate.now().plusDays(1);
		LocalDate dayThree = LocalDate.now().plusDays(2);
		LocalDate dateFour = LocalDate.now().plusDays(3);
		//when
		bookingService.createBookingById(new BookingTO(1, 1, 1, 1, dayOne,
			dayThree));
		//then
		Assertions.assertThrows(InvalidDateException.class, () -> {
			bookingService.createBookingById(new BookingTO(1, 1, 1, 1, dayTwo,
				dateFour));
		});

	}


	@Test
	public void updateBooking_updatesBooking_singleBooking() {
		//given
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDate todayPlusThree = LocalDate.now().plusDays(3);
		bookingService.createBookingById(new BookingTO(1, 1, 1, 1, today,
			tomorrow));
		//when
		bookingService.updateBooking(1, today, todayPlusThree);
		//then
		Assertions.assertEquals(today, bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(todayPlusThree, bookingService.getBookingById(1).getTo());
	}

	@Test
	public void updateBooking_throws_whenOverlapping() {
		//given
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDate todayPlusFive = LocalDate.now().plusDays(5);
		LocalDate todayPlusSix = LocalDate.now().plusDays(6);
		LocalDate todayPlusSeven = LocalDate.now().plusDays(7);
		bookingService.createBookingById(new BookingTO(1, 1, 1, 1, today, tomorrow));
		bookingService.createBookingById(new BookingTO(2, 1, 1, 1, todayPlusFive, todayPlusSeven));
		//when
		//then
		Assertions.assertThrows(InvalidDateException.class, () -> {
			bookingService.updateBooking(1, tomorrow, todayPlusSix);
		});
	}

	@Test
	public void updateBooking_continuallyUpdates_objectOverload() {
		//given
		LocalDate first = LocalDate.now();
		LocalDate fifth = LocalDate.now().plusDays(4);
		LocalDate twentyFirst = LocalDate.now().plusDays(20);
		LocalDate twentyFifth = LocalDate.now().plusDays(25);
		LocalDate twentySecond = LocalDate.now().plusDays(21);
		bookingService.createBookingById(new BookingTO(1, 1, 1, 1, first, fifth));

		BookingTO updateBooking1 = new BookingTO(1, 1, 1, 1, twentyFirst, twentyFifth);
		BookingTO updateBooking2 = new BookingTO(1, 1, 1, 1, fifth, twentySecond);

		//when
		//then
		bookingService.updateBooking(updateBooking1);
		Assertions.assertEquals(twentyFirst, bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(twentyFifth, bookingService.getBookingById(1).getTo());
		bookingService.updateBooking(updateBooking2);
		Assertions.assertEquals(fifth, bookingService.getBookingById(1).getFrom());
		Assertions.assertEquals(twentySecond, bookingService.getBookingById(1).getTo());
	}

	/**
	 * Checks if Booking is equal to BookingTO
	 *
	 * @return true if equal
	 */
	private boolean isBookingTOequalToBooking(BookingTO bookingTO, Booking booking) {
		return bookingTO.getGuestId() == booking.getGuestId() &&
			bookingTO.getRoomId() == booking.getRoomId() &&
			bookingTO.getTo() == booking.getTo() &&
			bookingTO.getFrom() == booking.getFrom() &&
			bookingTO.getNumberOfPeople() == booking.getNumberOfPeople();
	}
}
