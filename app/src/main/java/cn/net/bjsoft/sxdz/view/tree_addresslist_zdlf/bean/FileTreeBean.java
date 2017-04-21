package cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;

public class FileTreeBean {
    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeStyle
    private String style;
    @TreeNodeEmployee
    private AddressEmployeesBean employeesBean;



    public FileTreeBean(int _id, int parentId, String style, AddressEmployeesBean employeesBean) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.style = style;
        this.employeesBean = employeesBean;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public AddressEmployeesBean getEmployeesBean() {
        return employeesBean;
    }

    public void setEmployeesBean(AddressEmployeesBean employeesBean) {
        this.employeesBean = employeesBean;
    }

}
