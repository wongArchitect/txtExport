public enum OrderType {

    ORDER_TYPE_ASC("正序", "asc"), ORDER_TYPE_DESC("倒序", "desc");
    // 成员变量
    private String name;
    private String index;
    // 构造方法
    private OrderType(String name, String index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(String index) {
        for (OrderType c : OrderType.values()) {
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