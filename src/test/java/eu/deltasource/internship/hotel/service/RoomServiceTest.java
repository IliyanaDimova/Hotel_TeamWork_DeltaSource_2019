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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static eu.deltasource.internship.hotel.domain.commodity.BedType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomServiceTest {

	private RoomService roomService = new RoomService(new RoomRepository());
	private RoomTO singleRoom;
	private RoomTO doubleRoom;
	private Set<AbstractCommodity> singleSet;

	@BeforeEach
	public void setUp() {
		singleSet = new HashSet<>(Arrays.asList(new Bed(SINGLE), new Toilet(), new Shower()));
		singleRoom = new RoomTO(singleSet);

		Set<AbstractCommodity> doubleSet = new HashSet<>(Arrays.asList(new Bed(DOUBLE), new Toilet(), new Shower()));
		doubleRoom = new RoomTO(doubleSet);
	}

	@Test
	public void testCreateRoom() {
		//given

		//when
		roomService.createRooms(singleRoom, doubleRoom);

		//then
		assertEquals(2, roomService.findRooms().size());
	}

	@Test
	public void testGetRoomById() {
		//given
		roomService.createRoom(singleRoom);
		Room room = new Room (1, singleSet);

		//when
		Room getRoom = roomService.getRoomById(1);

		//then
		assertEquals(room, getRoom);
	}

	@Test
	public void testFindRooms() {
		//given
		roomService.createRoom(singleRoom);
		roomService.createRoom(doubleRoom);

		//when
		List<Room> roomList = roomService.findRooms();

		//then
		assertEquals(2, roomList.size());
	}

	@Test
	public void testUpdateRoom() {
		//given
		roomService.createRoom(singleRoom);

		//when
		roomService.updateRoom(1, doubleRoom);
		int roomCapacity = roomService.getRoomById(1).getRoomCapacity();

		//then
		assertEquals(2, roomCapacity);
	}

	@Test
	public void testDeleteRoomById() {
		//given
		roomService.createRoom(singleRoom);

		//when
		roomService.deleteRoomById(1);
		List<Room> roomList = roomService.findRooms();

		//then
		assertEquals(0, roomList.size());
	}

	@Test
	public void testGetRoomByIdException() {
		//given
		int invalidRoomId = 9999;

		//when

		//then
		assertThrows(ItemNotFoundException.class, ()->roomService.getRoomById(invalidRoomId));
	}

	@Test
	public void testCreateRoomException() {
		//given

		//when

		//then
		assertThrows(FailedInitializationException.class, ()->roomService.createRoom(null));
		assertThrows(FailedInitializationException.class, ()->roomService.createRooms(doubleRoom, null));
	}

	@Test
	public void testDeleteRoomByIdException() {
		//given
		int invalidRoomId = 9999;

		//when

		//then
		assertThrows(ItemNotFoundException.class, ()->roomService.deleteRoomById(invalidRoomId));
	}

	@Test
	public void testUpdateRoomByIdException() {
		//given
		int invalidRoomId = 9999;

		//when

		//then
		assertThrows(ItemNotFoundException.class, ()->roomService.updateRoom(invalidRoomId, doubleRoom));
	}









}
