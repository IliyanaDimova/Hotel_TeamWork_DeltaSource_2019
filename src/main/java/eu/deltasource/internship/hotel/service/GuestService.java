package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.repository.GuestRepository;

import java.util.List;

/**
 * Created by Taner Ilyazov - Delta Source Bulgaria on 2019-07-28.
 */
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public void createGuest(Guest guest){
        if(!guestRepository.existsById(guest.getGuestId())){
            guestRepository.save(guest);
        }
        else{
            throw new FailedInitializationException("Guest already exists!");
        }
    }

    public void createGuest(String firstName, String lastName, Gender gender){
        Guest newGuest = new Guest(0, firstName, lastName, gender);
        guestRepository.save(newGuest);
    }

    public List<Guest> returnAllGuests(){
        return guestRepository.findAll();
    }
}
