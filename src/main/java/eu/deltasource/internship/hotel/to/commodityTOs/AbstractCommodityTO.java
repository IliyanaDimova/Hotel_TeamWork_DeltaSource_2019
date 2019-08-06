package eu.deltasource.internship.hotel.to.commodityTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.Shower;
import eu.deltasource.internship.hotel.domain.commodity.Toilet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Bed.class, name = "Bed"),

	@JsonSubTypes.Type(value = Toilet.class, name = "Toilet"),
	@JsonSubTypes.Type(value = Shower.class, name = "Shower")})

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
