package eu.deltasource.internship.hotel.to;

import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.Shower;
import eu.deltasource.internship.hotel.domain.commodity.Toilet;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;

import java.util.*;

/**
 * Room Transfer Object - without ID
 */
public class RoomTO {
	private static final int EMPTY_ROOM = 0;

	private int roomCapacity;
	private Set<AbstractCommodity> commodities;
	private List<Bed> beds;
	private List<Toilet> toilets;
	private List<Shower> showers;

	public RoomTO(){
		toilets = new ArrayList<>();
		beds = new ArrayList<>();
		showers = new ArrayList<>();
	}

	/**
	 * Constructor - initializes all fields
	 */
	public RoomTO(Set<AbstractCommodity> commodities) {
		this.commodities = new HashSet<>();
		this.commodities.addAll(commodities);
		roomCapacitySetter();
	}

	public void setCommodities(AbstractCommodity... commodities) {
		this.commodities = new HashSet<AbstractCommodity>(Arrays.asList(commodities));
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
