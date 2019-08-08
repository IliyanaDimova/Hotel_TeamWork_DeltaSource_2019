package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.Shower;
import eu.deltasource.internship.hotel.domain.commodity.Toilet;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.to.RoomTO;
import eu.deltasource.internship.hotel.to.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.to.commodityTOs.BedTO;
import eu.deltasource.internship.hotel.to.commodityTOs.ShowerTO;
import eu.deltasource.internship.hotel.to.commodityTOs.ToiletTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		roomRepository.save(convertRoomTOtoRoomObject(0, room));
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
		List<Room> potentialRooms = new ArrayList<Room>();
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null) {
				throw new FailedInitializationException("Room can not be null ");
			}
			potentialRooms.add(convertRoomTOtoRoomObject(0, rooms[i]));
		}
		roomRepository.saveAll(potentialRooms);
	}

	/**
	 * Deletes a room from the repository by given room id
	 *
	 * @return true if successfully deleted
	 */
	public boolean deleteRoomById(int id) {
		if (!existsById(id)) {
			throw new ItemNotFoundException("Room not found");
		}
		return roomRepository.deleteById(id);
	}

	//todo not working implement correctly

	/**
	 * Updates room's commodities and capacity
	 * Throws Exception if room has no commodities or if there is no room with the given id
	 *
	 * @param room to be updated
	 */
	public void updateRoom(int roomId, RoomTO room) {
		if (!existsById(roomId)) {
			throw new ItemNotFoundException("Room not found");
		}
		Room newRoom = convertRoomTOtoRoomObject(roomId, room);
		roomRepository.updateRoom(newRoom);
	}

	//todo refacture

	public Room convertRoomTOtoRoomObject(int id, RoomTO roomTO) {
		return new Room(id, convert_roomTOcommoditiesTO_To_AbstractCommodities(roomTO));
	}

	public Set<AbstractCommodity> convert_roomTOcommoditiesTO_To_AbstractCommodities(RoomTO roomTO) {
		List<AbstractCommodity> commodityList = new ArrayList<AbstractCommodity>();
		for (AbstractCommodityTO abstractCommodityTO : roomTO.getCommodities()) {
			commodityList.add(convertCommodityTO_to_Commodity(abstractCommodityTO));
		}
		return new HashSet<>(commodityList);
	}

	private AbstractCommodity convertCommodityTO_to_Commodity(AbstractCommodityTO abstractCommodityTO) {
		if (abstractCommodityTO instanceof BedTO) {
			return new Bed(((BedTO) abstractCommodityTO).getBedType());
		} else if (abstractCommodityTO instanceof ToiletTO) {
			return new Toilet();
		} else if (abstractCommodityTO instanceof ShowerTO) {
			return new Shower();
		} else {
			throw new InvalidParameterException("Invalid commodity passed.");
		}
	}
}
