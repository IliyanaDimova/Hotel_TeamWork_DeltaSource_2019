package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;

import java.util.HashSet;
import java.util.Set;

/**
 * Room Transfer Object - without ID
 */
public class RoomTO {
	private static final int EMPTY_ROOM = 0;

	private int roomCapacity;
	private Set<AbstractCommodity> commodities;

	/**
	 * Constructor - initializes all fields
	 */
	public RoomTO(Set<AbstractCommodity> commodities) {
		this.commodities = new HashSet<>();
		this.commodities.addAll(commodities);
		roomCapacitySetter();
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}

	public Set<AbstractCommodity> getCommodities() {
		return commodities;
	}

	/**
	 * Sets the capacity by counting all beds' capacity
	 */
	private void roomCapacitySetter() {
		roomCapacity = 0;
		for (AbstractCommodity commodity : commodities) {
			if (commodity instanceof Bed) {
				roomCapacity += ((Bed) commodity).getSize();
			}
		}
		if (roomCapacity == EMPTY_ROOM) {
			throw new FailedInitializationException("Room can not be empty");
		}
	}
}
