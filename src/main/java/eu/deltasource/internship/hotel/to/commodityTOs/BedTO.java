package eu.deltasource.internship.hotel.to.commodityTOs;

import eu.deltasource.internship.hotel.domain.commodity.BedType;

public class BedTO extends AbstractCommodityTO {
	private BedType bedType;

	public BedTO(BedType bedType) {
		super();
		this.bedType = bedType;

	}

	public BedTO() {
		super();
	}

	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}

	public BedType getBedType() {
		return bedType;
	}

	public int getSize() {
		return bedType.getSize();
	}
}
