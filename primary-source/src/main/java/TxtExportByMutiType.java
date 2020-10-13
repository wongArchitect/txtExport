
public class TxtExportByMutiType {

	public static void main(String[] args) {

		//工作日打印
		new TxtExportByDaySort().export();
		//目录打印
		new TxtExportByCatalogueSort().export();
	}

}  