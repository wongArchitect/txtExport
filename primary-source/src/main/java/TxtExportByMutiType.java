import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

public class TxtExportByMutiType {

	public static void main(String[] args) {

		//工作日打印
		new TxtExportByDaySort().export();
		//目录打印
		new TxtExportByCatalogueSort().export();
	}

}  