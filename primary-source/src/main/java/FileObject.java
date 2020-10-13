import java.util.Date;

public class FileObject {

	private String title;
	
	private String content;

	private Date  createDate;

	private Date  lastModifiedDate;
	
	private String type;   // dir:文件夹，file:文件, workDate: 当日日期

	private String fileYMDDate; // dir:创建日期，file：最后修改日期

	private Long size;

	private String parentDir;

	private String path;

	private Integer fileInnerOrder;   //所在文件内排序序号

	private Integer dirInnerFileNum;  //所在文件内文件的总数   注意，按写作日期的是当天的文件夹内的文件总数


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getParentDir() {
		return parentDir;
	}

	public void setParentDir(String parentDir) {
		this.parentDir = parentDir;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileYMDDate() {
		return fileYMDDate;
	}

	public void setFileYMDDate(String fileYMDDate) {
		this.fileYMDDate = fileYMDDate;
	}

	public Integer getFileInnerOrder() {
		return fileInnerOrder;
	}

	public void setFileInnerOrder(Integer fileInnerOrder) {
		this.fileInnerOrder = fileInnerOrder;
	}

	public Integer getDirInnerFileNum() {
		return dirInnerFileNum;
	}

	public void setDirInnerFileNum(Integer dirInnerFileNum) {
		this.dirInnerFileNum = dirInnerFileNum;
	}
}
