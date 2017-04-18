package cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean;

public class FileTreeTaskAddAddressListBean {
    @TreeTaskAddAddressListNodeId
    private int _id;
    @TreeTaskAddAddressListNodePid
    private int parentId;
    @TreeTaskAddAddressListNodeName
    private String name;
    @TreeTaskAddAddressListNodeType
    private String type;
    @TreeTaskAddAddressListNodeHumenNum
    private String humen_num;
    @TreeTaskAddAddressListNodeAvatar
    private String avatar;
    @TreeTaskAddAddressListNodeDeparment
    private String department;


    public FileTreeTaskAddAddressListBean(int _id, int parentId, String name, String type, String avatar, String department, String humen_num) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;

        this.type = type;
        this.avatar = avatar;
        this.department = department;
        this.humen_num = humen_num;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHumen_num() {
        return humen_num;
    }

    public void setHumen_num(String humen_num) {
        this.humen_num = humen_num;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


}
