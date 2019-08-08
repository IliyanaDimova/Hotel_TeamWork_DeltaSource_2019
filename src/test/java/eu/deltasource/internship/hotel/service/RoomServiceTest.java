package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.to.RoomTO;
import eu.deltasource.internship.hotel.to.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.to.commodityTOs.BedTO;
import eu.deltasource.internship.hotel.to.commodityTOs.ShowerTO;
import eu.deltasource.internship.hotel.to.commodityTOs.ToiletTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static eu.deltasource.internship.hotel.domain.commodity.BedType.DOUBLE;
import static eu.deltasource.internship.hotel.domain.commodity.BedType.SINGLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomServiceTest {

	private RoomService roomService = new RoomService(new RoomRepository());
	private RoomTO singleRoom;
	private RoomTO doubleRoom;
	private Set<AbstractCommodityTO> singleSet;

	@BeforeEach
	public void setUp() {
		singleSet = new HashSet<>(Arrays.asList(new BedTO(SINGLE), new ToiletTO(), new ShowerTO()));
		singleRoom = new RoomTO(singleSet);

		Set<AbstractCommodityTO> doubleSet = new HashSet<>(Arrays.asList(new BedTO(DOUBLE), new ToiletTO(),
			new ShowerTO()));
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

		//when
		Room getRoom = roomService.getRoomById(1);

		//then
		assertEquals(getRoom, getRoom);
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
		assertThrows(ItemNotFoundException.class, () -> roomService.getRoomById(invalidRoomId));
	}

	@Test
	public void testCreateRoomException() {
		//given

		//when

		//then
		assertThrows(FailedInitializationException.class, () -> roomService.createRoom(null));
		assertThrows(FailedInitializationException.class, () -> roomService.createRooms(doubleRoom, null));
	}

	@Test
	public void testDeleteRoomByIdException() {
		//given
		int invalidRoomId = 9999;

		//when

		//then
		assertThrows(ItemNotFoundException.class, () -> roomService.deleteRoomById(invalidRoomId));
	}

	@Test
	public void testUpdateRoomByIdException() {
		//given
		int invalidRoomId = 9999;

		//when

		//then
		assertThrows(ItemNotFoundException.class, () -> roomService.updateRoom(invalidRoomId, doubleRoom));
	}


}
