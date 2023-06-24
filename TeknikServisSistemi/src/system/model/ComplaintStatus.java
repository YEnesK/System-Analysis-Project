package system.model;

public enum ComplaintStatus {
	WAITING_OPERATION("İŞLEME ALINACAK"),
	WAITING_COMPONENT("PARÇA BEKLİYOR"),
	IN_OPERATION("İŞLEM GÖRÜYÖR"),
	WAITING_PAYMENT("ÖDEME BEKLİYOR"),
	WAITING_REFUND("İADE BEKLİYOR"),
	COMPLETED("TAMAMLANDI");

	private final String name;

	ComplaintStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
