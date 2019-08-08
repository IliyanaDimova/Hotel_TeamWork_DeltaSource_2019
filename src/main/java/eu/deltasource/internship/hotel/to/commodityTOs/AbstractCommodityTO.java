package eu.deltasource.internship.hotel.to.commodityTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
	@JsonSubTypes.Type(value = BedTO.class, name = "Bed"),

	@JsonSubTypes.Type(value = ToiletTO.class, name = "Toilet"),
	@JsonSubTypes.Type(value = ShowerTO.class, name = "Shower")})

public class AbstractCommodityTO {

	public int id;

	public AbstractCommodityTO() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
