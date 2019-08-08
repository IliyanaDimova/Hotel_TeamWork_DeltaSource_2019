package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.to.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.to.commodityTOs.BedTO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Room Transfer Object - without ID
 */
public class RoomTO {
	private static final int EMPTY_ROOM = 0;

	private int roomCapacity;
	private Set<AbstractCommodityTO> commodities;

	public RoomTO() {
		commodities = new HashSet<>();
	}

	/**
	 * Constructor - initializes all fields
	 */
	public RoomTO(Set<AbstractCommodityTO> commodities) {
		this.commodities = new HashSet<>();
		this.commodities.addAll(commodities);
		roomCapacitySetter();
	}

	public void setCommodities(AbstractCommodityTO... commodities) {
		this.commodities = new HashSet<AbstractCommodityTO>(Arrays.asList(commodities));
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}

	public Set<AbstractCommodityTO> getCommodities() {
		return commodities;
	}

	/**
	 * Sets the capacity by counting all beds' capacity
	 */
	private void roomCapacitySetter() {
		roomCapacity = 0;
		for (AbstractCommodityTO commodity : commodities) {
			if (commodity instanceof BedTO) {
				roomCapacity += ((BedTO) commodity).getSize();
			}
		}
		if (roomCapacity == EMPTY_ROOM) {
			throw new FailedInitializationException("Room can not be empty");
		}
	}
}
