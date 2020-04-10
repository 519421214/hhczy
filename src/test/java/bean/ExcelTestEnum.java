package bean;

public enum ExcelTestEnum {
    Man("男",1),
    Woman("女",2);
    private String sex;
    private int index;

    ExcelTestEnum(String sex, int index) {
        this.sex = sex;
        this.index = index;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
