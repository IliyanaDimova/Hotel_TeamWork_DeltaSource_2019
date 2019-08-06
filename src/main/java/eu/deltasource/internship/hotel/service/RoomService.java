package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.to.RoomTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Room
 * Has one RoomRepository field
 */
@Service
public class RoomService {

	private final RoomRepository roomRepository;

	@Autowired
	/**
	 * Constructor - gives a roomRepository value
	 */
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	/**
	 * Throws Exception if room with the given id does not exist
	 *
	 * @return (unmodifiable) room with the same id as the given
	 */
	public Room getRoomById(int id) {
		return roomRepository.findById(id);
	}

	/**
	 * @return all rooms in repository (unmodifiable)
	 */
	public List<Room> findRooms() {
		return roomRepository.findAll();
	}

	/**
	 * Adds a room in the repository
	 * Throws Exception if room was not added
	 */
	public void createRoom(RoomTO room) {
		if (room == null) {
			throw new FailedInitializationException("Room can not be null ");
		}
		roomRepository.save(room);
	}

	/**
	 * @return true if a room with the given number exists
	 */
	public boolean existsById(int id) {
		return roomRepository.existsById(id);
	}

	/**
	 * Adds many rooms to the repository
	 */
	public void createRooms(RoomTO... rooms) {
		for (RoomTO item : rooms) {
			if (item == null)
				throw new FailedInitializationException("Room can not be null ");
		}
		roomRepository.saveAll(rooms);
	}

	/**
	 * Deletes a room from the repository by given room id
	 *
	 * @return true if successfully deleted
	 */
	public boolean deleteRoomById(int id) {
		if(!existsById(id)){
			throw new ItemNotFoundException("Room not found");
		}
		return roomRepository.deleteById(id);
	}

	/**
	 * Updates room's commodities and capacity
	 * Throws Exception if room has no commodities or if there is no room with the given id
	 *
	 * @param room to be updated
	 */
	public void updateRoom(int roomId, RoomTO room) {
		if(!existsById(roomId)){
			throw new ItemNotFoundException("Room not found");
		}
		Room newRoom = new Room(roomId, room.getCommodities());
		roomRepository.updateRoom(newRoom);
	}
}
