public enum FileType {

    FILE_TYPE_FOLDER("folder", 1), FILE_TYPE_DOC("doc", 2), FILE_TYPE_WORK_DATE("workDate", 3);
    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private FileType(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (FileType c : FileType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}