public enum TagType {

    TAG_TYPE_SORT_BY_CATALOGUE(" - 目录排序", "catalogue"), TAG_TYPE_SORT_BY_WORK_DAY(" - 写作日期排序", "workDay");

    static String tag = " - 所属文件夹内排序";
    // 成员变量
    private String name;
    private String index;
    // 构造方法
    private TagType(String name, String index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(String index) {
        for (TagType c : TagType.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}