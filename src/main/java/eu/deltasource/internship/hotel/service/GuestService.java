package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.to.GuestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.util.List;

/**
 * Service class for Guest
 * Has one GuestRepository field
 */
@Service
public class GuestService {

	private final GuestRepository guestRepository;

	@Autowired
	/**
	 * Constructor - gives a repository value
	 */
	public GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	/**
	 * Checks if guest by given ID exists
	 *
	 * @return true if the guest exists
	 */
	public boolean existsById(int id) {
		return guestRepository.existsById(id);
	}

	/**
	 * Adds a new guest to GuestRepository
	 * Throws if gender is not female/male
	 *
	 * @param guest transfer object for guest
	 */
	public void createGuest(GuestTO guest) {
		if(guest.getGender()!= Gender.FEMALE && guest.getGender()!= Gender.MALE){
			throw new FailedInitializationException("Invalid Gender!");
		}
		Guest newGuest = new Guest(0, guest.getLastName(), guest.getFirstName(), guest.getGender());
		guestRepository.save(newGuest);
	}

	/**
	 * @return a list of all guests in a GuestRepository
	 */
	public List<Guest> returnAllGuests() {
		return guestRepository.findAll();
	}

	/**
	 * Returns a guest by given guest ID
	 * Throws Exception if Guest with this ID doesn't exist!
	 */
	public Guest returnGuestById(int guestId) {
		return guestRepository.findById(guestId);
	}

	/**
	 * Updates a guest's names and gender by given guest ID
	 * Throws Exception if Guest with this ID doesn't exist!
	 *
	 * @param guestId      the ID of the guest we want to update
	 * @param newGuestData transfer object for Guest without ID
	 */
	public void updateGuestById(int guestId, GuestTO newGuestData) {
		Guest updatedGuest = new Guest(guestId, newGuestData.getFirstName(), newGuestData.getLastName(),
			newGuestData.getGender());
		guestRepository.updateGuest(updatedGuest);
	}

	/**
	 * Removes a guest by given ID
	 * Throws Exception if Guest with this ID doesn't exist!
	 */
	public void removeGuestById(int guestId) {
		guestRepository.delete(returnGuestById(guestId));
	}
}
