package cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;

public class FileTreeBean {
    @TreeNodeId
    private Long _id;
    @TreeNodePid
    private Long parentId;
    @TreeNodeName
    private String name;
    @TreeNodeAddressDeptsBean
    private AddressDeptsBean addressDeptsBean;


    public FileTreeBean(Long _id, Long parentId, String name,AddressDeptsBean addressDeptsBean) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
        this.addressDeptsBean = addressDeptsBean;
    }


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDeptsBean getAddressDeptsBean() {
        return addressDeptsBean;
    }

    public void setAddressDeptsBean(AddressDeptsBean addressDeptsBean) {
        this.addressDeptsBean = addressDeptsBean;
    }


}
