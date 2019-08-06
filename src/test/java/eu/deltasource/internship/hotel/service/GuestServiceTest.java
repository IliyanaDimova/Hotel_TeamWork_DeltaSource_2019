package eu.deltasource.internship.hotel.service;


import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.to.GuestTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GuestServiceTest {
	GuestService guestService = new GuestService(new GuestRepository());

	@BeforeEach
	public void beforeEach() {
		guestService.createGuest(new GuestTO("Pesho", "Peshov", Gender.MALE));
		guestService.createGuest(new GuestTO("Penka", "Peshova", Gender.FEMALE));
		guestService.createGuest(new GuestTO("Ganka", "Petrova", Gender.FEMALE));
	}

	@Test
	public void testExistsById() {
		//given
		//when
		//then
		assertEquals(true, guestService.existsById(2));
		assertFalse(guestService.existsById(4));
	}

	private void assertTrue(int size) {
	}

	@Test
	public void testCreateGuest() {
		//given
		//when
		//then
		assertEquals(3, guestService.returnAllGuests().size());
	}
	@Test
	public void testCreateGuestGenderThrows() {
		//given
		GuestTO invalidGender = new GuestTO("Name", "Name", null);
		//when
		//then
		assertThrows(FailedInitializationException.class, () -> {
			guestService.createGuest(invalidGender);
		});
	}

	@Test
	public void testReturnAllGuests() {
		//given
		Guest guest2 = new Guest(2, "Penka", "Peshova", Gender.FEMALE);
		//when
		//then
		assertEquals(guest2, guestService.returnAllGuests().get(1));
	}

	@Test
	public void testReturnGuestById() {
		//given
		Guest guest2 = new Guest(2, "Penka", "Peshova", Gender.FEMALE);
		//when
		//then
		assertEquals(guest2, guestService.returnGuestById(2));
	}

	@Test
	public void testReturnGuestByIdException() {
		//given
		//when
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.returnGuestById(4);
		});
	}

	@Test
	public void testUpdateGuestById() {
		//given
		String firstName = "Katrin";
		String lastName = "Apdelgafar";
		Gender gender = Gender.MALE;
		//when
		guestService.updateGuestById(2, new GuestTO(firstName, lastName, gender));
		//then
		assertEquals(firstName, guestService.returnGuestById(2).getFirstName());
		assertEquals(lastName, guestService.returnGuestById(2).getLastName());
		assertEquals(gender, guestService.returnGuestById(2).getGender());
	}

	@Test
	public void testUpdateGuestByIdThrowsGender() {
		//given
		GuestTO invalidGender = new GuestTO("Name", "Name", null);
		//when
		//then
		assertThrows(FailedInitializationException.class, () -> {
			guestService.updateGuestById(3, invalidGender);
		});
	}

	@Test
	public void testRemoveGuestById() {
		//given
		//when
		guestService.removeGuestById(1);
		//then
		assertEquals(2, guestService.returnAllGuests().size());
	}
}
