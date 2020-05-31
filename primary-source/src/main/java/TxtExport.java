import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

public class TxtExport {

		
	//导出文件夹下所有文本文件的内容
	public static void exportAllFileFromDir(String exportPath, String dirPath, final String order) throws Exception {
		List<String> fileContents = new ArrayList<String>();
		List<FileObject> fileObjects = new ArrayList<FileObject>();
		
		fileObjects = getAllFile(dirPath,fileObjects);

		//今日统计
		String todayStatistics = todayStatistics(fileObjects);
		//排序
		fileObjects.sort(new Comparator<FileObject>() {
			@Override
			public int compare(FileObject o1, FileObject o2) {
				if(order != null && order.equals(" 正序 - 所属文件夹内排序")) {
					return o1.getDate().compareTo(o2.getDate());
				}else {
					return o2.getDate().compareTo(o1.getDate());
				}
			}
		});


		List<FileObject> dirObjects = new ArrayList<>();
		List<FileObject> fileObjectsTemp = new ArrayList<>();
		//原样打印
		for(FileObject f : fileObjects){
			if(f.getType().equals("dir")){
				dirObjects.add(f);
			}
			if(f.getType().equals("file")){
				fileObjectsTemp.add(f);
			}
		}
		//不重复打印
//		for(FileObject f : fileObjects) {
//			if(f.getType().equals("dir")){
//				dirObjects.add(f);
//			}
//		}
//		for(FileObject f : singleFileMap.values()){
//				fileObjectsTemp.add(f);
//		}

		//把文件放对应文件夹中
		List<List<FileObject>> rusultFileObjects = new ArrayList<>();
		for(FileObject dir : dirObjects){
			List<FileObject> rusultFileObjectsTemp = new ArrayList<>();
			rusultFileObjectsTemp.add(dir);
			for(FileObject f : fileObjectsTemp){
				if(dir.getParentDir().equals(f.getParentDir())){
					rusultFileObjectsTemp.add(f);
				}
			}
			rusultFileObjects.add(rusultFileObjectsTemp);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") ; //使用了默认的格式创建了一个日期格式化对象。
		String fileContent = "    - - - - - - - - -    " + "****" + "  今日写作  ****" + "    - - - - - - - - -   \n";
		fileContent += "日期: " + dateFormat.format(new Date()) + "\n" + "\n";
		fileContents.add(fileContent);
		//今日新增
		todayAdd(dirObjects, fileObjectsTemp, fileContents);
		//添加今日统计内容
		fileContents.add(todayStatistics);
		//原样打印
		fileContents.addAll(fileContentSplitJoint(rusultFileObjects, order));
		exportByDocFormat(exportPath, fileContents);
		//不重复打印
//		exportByDocFormat(exportPath, fileContentSplitJointAbandonSame(rusultFileObjects, order));
	}

	private static void todayAdd(List<FileObject> dirObjects, List<FileObject> fileObjectsTemp, List<String> fileContents) {
		if (dirObjects.size() > 0 && fileObjectsTemp.size() > 0) {

//			List<FileObject> todayNewDirs = new ArrayList<>();
			List<FileObject> todayNewfiles = new ArrayList<>();

			//检索今日新增
//			for(FileObject dir : dirObjects){
//				if(UtilDate.isThisToday(dir.getDate())){
//					todayNewDirs.add(dir);
//				}
//			}
			for(FileObject f : fileObjectsTemp){
				if(UtilDate.isThisToday(f.getDate())){
					todayNewfiles.add(f);
				}
			}
			Map<String, String> todayNewDirMap = new HashMap<>();
			for(FileObject f : todayNewfiles){
				if(!todayNewDirMap.containsKey(f.getParentDir())){
					String[] dirPathSplit =  f.getParentDir().split("\\\\");
					String dirName = dirPathSplit[dirPathSplit.length - 1];
					todayNewDirMap.put(f.getParentDir(), dirName);
				}
			}

			String fileContent = "                        " + "****" + "  今日新增  ****" + "                        \n";
//			fileContent += "文件夹新增总数:  " + todayNewDirs.size() + "\n";
			fileContent += "文件新增总数:  " + todayNewfiles.size() + "\n";

			if(todayNewDirMap.size() > 0){
				String dirNams = "";
				for(String name : todayNewDirMap.values()){
					dirNams += "、" + name;
				}
				fileContent += "新增文件所在文件夹列表:  " + dirNams.substring(1) + "\n";
			}else {
				fileContent += "新增文件所在文件夹列表:  没有新增 \n";
			}

			if(todayNewfiles.size() > 0){
				fileContent += "文件新增列表:  \n";
				String fileNams = "";
				todayNewDirMap.keySet();
				for(String parentDir : todayNewDirMap.keySet()){
					fileContent += "  - - " + parentDir + " ： \n";
					for(FileObject f : todayNewfiles){
						if(parentDir.equals(f.getParentDir())){
							fileContent += "      - -  " + f.getTitle() + "\n";
							fileContent += "      - -  大小： " + f.getSize() + "\n";
						}
					}
				}
				fileContent += fileNams + "\n";
			}else {
				fileContent += "文件新增列表:  没有新增 \n";
			}

			fileContent += "\n";
			fileContents.add(fileContent);
		}else {
			System.out.println("文件夹或文件 数量为 0 ");
		}
	}

	public static String todayStatistics(List<FileObject> fileObjects){
		Map<String, FileObject> sameFileMap = new HashMap<>();
		Map<String, FileObject> singleFileMap = new HashMap<>();
		Long sumFilesNum = Long.valueOf(0);
		BigDecimal sumFileSize = BigDecimal.valueOf(0);
		BigDecimal sumSingleFileSize = BigDecimal.valueOf(0);
		BigDecimal sumSameFileSize = BigDecimal.valueOf(0);
		BigDecimal sumCharNum = BigDecimal.valueOf(0);
		BigDecimal sumSingleCharNum = BigDecimal.valueOf(0);
		BigDecimal sumSameCharNum = BigDecimal.valueOf(0);
		for(FileObject f : fileObjects){
			if(f.getType().equals("file")){
				if(!singleFileMap.containsKey(f.getTitle())){
					singleFileMap.put(f.getTitle(), f);
					sumSingleFileSize = sumSingleFileSize.add(BigDecimal.valueOf(f.getSize())) ;
					sumSingleCharNum = sumSingleCharNum.add(BigDecimal.valueOf(f.getContent().length()));
				}else {
					sameFileMap.put(f.getTitle(), f);
					sumSameFileSize = sumSameFileSize.add(BigDecimal.valueOf(f.getSize())) ;
					sumSameCharNum = sumSameCharNum.add(BigDecimal.valueOf(f.getContent().length()));
				}
				sumFilesNum += 1;
				sumFileSize = sumFileSize.add(BigDecimal.valueOf(f.getSize())) ;
				sumCharNum = sumCharNum.add(BigDecimal.valueOf(f.getContent().length()));
			}
		}

		//换行符
		String wrapChar = "\n";
		String todayStatistics = "                        " + "****" + "  今日统计  ****" + "                        ";;
		todayStatistics += wrapChar + "文件总个数：" + sumFilesNum;
		todayStatistics += wrapChar + "重复文件个数：" + sameFileMap.size();
		todayStatistics += wrapChar + "不重复文件个数：" + singleFileMap.size();
		double fileRatio = (singleFileMap.size()/Double.parseDouble(String.valueOf(fileObjects.size())));
		todayStatistics += wrapChar + "比例计算：实际文件个数 / 文件总个数 = " + String.format("%.2f", fileRatio);
//
//		System.out.println("\n************  今日统计 **************");
//		System.out.println("文件总个数：" + sumFilesNum);
//		System.out.println("重复文件个数：" + sameFileMap.size());
//		System.out.println("不重复文件个数：" + singleFileMap.size());
//		double fileRatio = (singleFileMap.size()/Double.parseDouble(String.valueOf(fileObjects.size())));
//		System.out.println("比例计算：实际文件个数 / 文件总个数 = " + String.format("%.2f", fileRatio));

		todayStatistics += wrapChar + "  - - - - - - - - - - - -  文件个数统计  - - - - - - - - - - - -";
		todayStatistics += wrapChar + "文件总大小：" + sumFileSize;
		todayStatistics += wrapChar + "重复文件大小：" + sumSameFileSize;
		todayStatistics += wrapChar + "不重复文件大小：" + sumSingleFileSize;
		BigDecimal singleFileSizeRatio = (sumSingleFileSize.divide(sumFileSize,2, BigDecimal.ROUND_HALF_UP));
		todayStatistics += wrapChar + "比例计算：实际文件大小 / 文件总大小 = " + String.format("%.2f", singleFileSizeRatio);

//		System.out.println("------------  文件个数统计  ------------");
//		System.out.println("文件总大小：" + sumFileSize);
//		System.out.println("重复文件大小：" + sumSameFileSize);
//		System.out.println("不重复文件大小：" + sumSingleFileSize);
//		System.out.println("比例计算：实际文件大小 / 文件总大小 = " + String.format("%.2f", singleFileSizeRatio));


		todayStatistics += wrapChar + " - - - - - - - - - - - -  字符数统计  - - - - - - - - - - - -";
		todayStatistics += wrapChar + "总字符数：" + sumCharNum;
		todayStatistics += wrapChar + "重复字符数：" + sumSameCharNum;
		todayStatistics += wrapChar + "不重复字符数：" + sumSingleCharNum;
		BigDecimal singleCharSizeRatio = (sumSingleCharNum.divide(sumCharNum,2, BigDecimal.ROUND_HALF_UP));
		todayStatistics += wrapChar + "比例计算：实际字符数 / 字符总数 = " + String.format("%.2f", singleCharSizeRatio);

//		System.out.println("------------  字符统计  ------------");
//		System.out.println("文件字符大小：" + sumCharNum);
//		System.out.println("重复字符大小：" + sumSameCharNum);
//		System.out.println("不重复字符大小：" + sumSingleCharNum);
//		System.out.println("比例计算：实际字符大小 / 字符总大小 = " + String.format("%.2f", singleCharSizeRatio));
		return todayStatistics += wrapChar + wrapChar + wrapChar;
	}

	//获取所有文件并放入FileObjectList中。
	public static List<FileObject> getAllFile(String dirPath, List<FileObject> fileObjects) throws Exception{
		File file = new File(dirPath);
		if(!file.exists()) {
			return null;
		}
		
				
		File [] files = file.listFiles();
		String fileType = "file";
		String dirType = "dir";
		for(File f : files) {
			FileObject fileObject = null;
			
			if(f.isFile() && f.getName().endsWith(".txt")) {
				fileObject = new FileObject();
				//类型
				fileObject.setType(fileType);
				//大小
				fileObject.setSize(f.length());
				//所在文件夹
				fileObject.setParentDir(f.getParent().replace("//", "\\"));
				//标题
				if(f.getName().contains(":")) {
					fileObject.setTitle(f.getName().split(":")[0]);
				}else {
					fileObject.setTitle(f.getName().split(".txt")[0]);
				}

				//文本内容
				String filePath = f.getAbsolutePath().replace("//", "\\");
				String content = readSingleFile(filePath);
				fileObject.setContent(content);	
				
				//日期
				Date date = getFileTime(filePath);
				fileObject.setDate(date);
			}
			
			if(f.isDirectory()) {
				fileObject = new FileObject();
				//类型
				fileObject.setType(dirType);
				//路径
				String dirPathTemp = f.getAbsolutePath().replaceAll("//", "\\\\");
				if(dirPathTemp.endsWith("//")){
					dirPathTemp = dirPathTemp.replaceAll("//","");
				}
				fileObject.setParentDir(dirPathTemp);
				//标题
				fileObject.setTitle(f.getName());
				//日期
				Date date = getFileTime(f.getAbsolutePath());
				fileObject.setDate(date);

				getAllFile(f.getAbsolutePath() + "//", fileObjects);
			}
			if(!Objects.isNull(fileObject)){
				fileObjects.add(fileObject);
			}
		}
		
		return fileObjects;
	}
	
	//拼接所要导出的内容 ---- 不重复打印
	public static List<String> fileContentSplitJointAbandonSame(List<List<FileObject>> rusultFileObjects, String order) {
		List<String> fileContents = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") ; //使用了默认的格式创建了一个日期格式化对象。

		for(List<FileObject> fileObjects : rusultFileObjects) {
			int fileNumber = 0;
			for (FileObject f : fileObjects) {
				if (null != f) {
					String fileContent = "";
					if (f.getType().equals("file")) {
						fileContent += "\n" + f.getContent() + "\n";
					}

					fileContents.add(fileContent);
				}
			}
		}
		
		return fileContents;
	}

	//拼接所要导出的内容  ---- 原样打印
	public static List<String> fileContentSplitJoint(List<List<FileObject>> rusultFileObjects, String order) {
		List<String> fileContents = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") ; //使用了默认的格式创建了一个日期格式化对象。

		for(List<FileObject> fileObjects : rusultFileObjects) {
			int fileNumber = 0;
			for (FileObject f : fileObjects) {
				if (null != f) {
					String fileContent = "";

					if (f.getType().equals("dir")) {
						fileContent += "    - - - - - - - - -    " + "****" + "  文件夹: " + f.getTitle() +  "  ****" + "    - - - - - - - - -   \n";
						fileContent += "路径:  " + f.getParentDir() + "\n";
						fileContent += "日期: " + dateFormat.format(f.getDate()) + "\n";
						fileContent += "\n";
					}

					if (f.getType().equals("file")) {
						fileContent += "标题: " + f.getTitle() + "\n";
						fileContent += "所在文件夹:  " + f.getParentDir() + "\n";
						fileContent += "日期: " + dateFormat.format(f.getDate()) + "\n";

						//本文件夹内文件序号
						if (null != order && order.equals("正序")) {
							fileContent += "本文件夹内文件序号：  " + (++fileNumber) + "\n";
						} else {
							fileContent += "本文件夹内文件序号：  " + (fileObjects.size() - fileNumber) + "\n";
							++fileNumber;
						}

						fileContent += "正文:  - - - -   " + "\n" + f.getContent() + "\n";
					}

					fileContents.add(fileContent);
				}
			}
		}
		return fileContents;
	}
	
	
	public static String readSingleFile(String pathName) throws Exception{
		/* 读入TXT文件 */  
//        String pathName = "e:\\in.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
        File f = new File(pathName); // 要读取以上路径的input。txt文件 
        if(!f.exists() || f.isDirectory()) {
        	return null;
        }
        String chartSet = "utf-8";
        String code = getFileCode(f);
        if(null != code && (code.equals("ANSI") || code.equals("ANSI1100") || code.equals("ANSI1000") || code.startsWith("ANSI11100000") || code.equals("ANSI1111"))) {
            chartSet = "GBK";
        }
		String rowContents = new String();
		
        FileInputStream inputStream = new FileInputStream(f); 
        InputStreamReader reader = new InputStreamReader(inputStream, chartSet); 
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
        String line = "";  
        line = br.readLine();  
        while (line != null) {  
            rowContents = rowContents + line + "\n";
            line = br.readLine(); // 一次读入一行数据
            if(null == line) {
            	break;
            }
        }  
        return rowContents;
	}
	
	//获取文件创建时间
	private static Date getFileTime(String filePath){
		File file = new File(filePath);
		BasicFileAttributes attr = null;
		try {
			Path path= Paths.get(filePath);
			BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
			attr = basicview.readAttributes();
			return new Date(file.lastModified());
		} catch (Exception e) {
			e.printStackTrace();
			return new Date(attr.creationTime().toMillis());
		}
	}

	//导出所有文本
	public static void exportByDocFormat(String docFilePath, List<String> fileContents) throws IOException {
		/*写入Word文档 */
        File f = new File(docFilePath); // 相对路径，如果没有则要建立一个新的output。txt文件
        if(!f.exists()) {        	
        	f.createNewFile(); // 创建新文件 
        } 
        fileContents.removeAll(Collections.singleton(null));

        String charset = "utf-8"; 
        // 写字符换转成字节流
        FileOutputStream outputStream = new FileOutputStream(f); 
        OutputStreamWriter bw = new OutputStreamWriter(outputStream, charset); 
        
		for (String fileContent : fileContents) {
				bw.write(fileContent);
		}
		bw.flush();
		bw.close();
	}

	
    public static String getFileCode(File file) throws Exception {
    	int headSize = 6;
		InputStream inputStream = new FileInputStream(file);
		byte[] head = new byte[100];
		inputStream.read(head);
		String code = "";
		code = "UTF-8";

		if (head[0] == -1 && head[1] == -2) {
		    code = "UTF-16";
		    System.err.println("文件编码错误: " + file.getName() + " : " + code);
		} else if (head[0] == -2 && head[1] == -1) {
		    code = "Unicode";
		    System.err.println("文件编码错误: " + file.getName() + " : " + code);
		} else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
		    code = "UTF-8";
		} else {//noBom utf-8
			int i = 0;
		    while (i < headSize - 2) {
		        if ((head[i] & 0x00FF) < 0x80) {// (10000000)值小于0x80的为ASCII字符
		            i++;
		            code = "UTF-8";
		            continue;
		        } else if ((head[i] & 0x00FF) < 0xC0) {// (11000000)值在0x80和0xC0之间的,不是开头第一个
//		            code = "Not UTF-8";
		        	code = "ANSI1000";
		        	System.err.println("文件编码错误: " + file.getName() + " : " + code);
		            break;
		        } else if ((head[i] & 0x00FF) < 0xE0) {// (11100000)此范围内为2字节UTF-8字符
		            if ((head[i + 1] & (0xC0)) != 0x8) {
//		                code = "Not UTF-8";
			        	code = "ANSI1100";
		                System.err.println("文件编码错误: " + file.getName() + " : " + code);
		                break;
		            } else
		                i += 2;
		        } else if ((head[i] & 0x00FF) < 0xF0) {// (11110000)此范围内为3字节UTF-8字符
		            if ((head[i + 1] & (0xC0)) != 0x80 || (head[i + 2] & (0xC0)) != 0x80) {
//		                code = "Not UTF-8";
			        	code = "ANSI1110000";
		                System.err.println("文件编码错误: " + file.getName() + " : " + code + (head[i + 1] & (0xC0)));
		                break;
		            } else
		                i += 3;
		        } else {
//		            code = "Not UTF-8";
		        	code = "ANSI1111";
		            System.err.println("文件编码错误: " + file.getName() + " : " + code);
		            break;
		        }
		    }
		}
		return code;
    }
	
		
	public static void main(String[] args) {
//		List<String> fileContentsTemp = new ArrayList<String>();
//		try { 
//			List<String> fileContents = getAllFileContent("E:\\wong论", fileContentsTemp); 
//			exportByDocFormat("e:\\wong论-随笔 "+ UtilDate.getDateNum() + ".doc", fileContents);
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//
		String orderASC = " 正序 - 所属文件夹内排序";
		String orderDESC = " 倒序 - 所属文件夹内排序";
		String sourceFilesRootDir = "E:\\GUAN论";
		String exportFileDirName = "e:\\guan论-随笔 "+ UtilDate.getDateNum() + " -- " + orderASC + ".doc";
		try {
			exportAllFileFromDir(exportFileDirName, sourceFilesRootDir, orderASC);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}  