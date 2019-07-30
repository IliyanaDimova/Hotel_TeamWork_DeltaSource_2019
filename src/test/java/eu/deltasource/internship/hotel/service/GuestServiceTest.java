package eu.deltasource.internship.hotel.service;


import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GuestServiceTest {
    GuestService gs = new GuestService(new GuestRepository());

    @BeforeEach
    public void beforeEach() {
        gs.createGuest("Pesho", "Peshov", Gender.MALE);
        gs.createGuest("Penka", "Peshova", Gender.FEMALE);
        gs.createGuest("Ganka", "Petrova", Gender.FEMALE);
    }

    @Test
    public void testExistsById() {
        //given
        //when
        //then
        assertEquals(true, gs.existsById(2));
        assertFalse(gs.existsById(4));
    }

    private void assertTrue(int size) {
    }

    @Test
    public void testCreateGuest() {
        //given
        //when
        //then
        assertEquals(3, gs.returnAllGuests().size());
    }

    @Test
    public void testReturnAllGuests() {
        //given
        Guest guest2 = new Guest(2, "Penka", "Peshova", Gender.FEMALE);
        //when
        //then
        assertEquals(guest2, gs.returnAllGuests().get(1));
    }

    @Test
    public void testReturnGuestById() {
        //given
        Guest guest2 = new Guest(2, "Penka", "Peshova", Gender.FEMALE);
        //when
        //then
        assertEquals(guest2, gs.returnGuestById(2));
    }

    @Test
    public void testReturnGuestByIdException() {
        //given
        //when
        //then
        assertThrows(ItemNotFoundException.class, () -> {
            gs.returnGuestById(4);
        });
    }

    @Test
    public void testUpdateGuestById() {
        //given
        String firstName = "Katrin";
        String lastName = "Apdelgafar";
        Gender gender = Gender.MALE;
        //when
        gs.updateGuestById(2, firstName, lastName, gender);
        //then
        assertEquals(firstName, gs.returnGuestById(2).getFirstName());
        assertEquals(lastName, gs.returnGuestById(2).getLastName());
        assertEquals(gender, gs.returnGuestById(2).getGender());
    }

    @Test
    public void testRemoveGuestById() {
        //given
        //when
        gs.removeGuestById(1);
        //then
        assertEquals(2, gs.returnAllGuests().size());
    }
}
