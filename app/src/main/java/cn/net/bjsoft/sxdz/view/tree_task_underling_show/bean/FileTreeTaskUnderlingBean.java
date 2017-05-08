package cn.net.bjsoft.sxdz.view.tree_task_underling_show.bean;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;

public class FileTreeTaskUnderlingBean {
    @TreeNodeTaskUnderlingId
    private Long _id;
    @TreeNodeTaskUnderlingPid
    private Long parentId;
    @TreeNodeTaskUnderlingName
    private String name;
    @TreeNodeAddressTaskUnderlingPositionsBean
    private AddressPositionsBean positionsBean;



    public FileTreeTaskUnderlingBean(Long _id, Long parentId, String name, AddressPositionsBean positionsBean) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
        this.positionsBean = positionsBean;
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

    public AddressPositionsBean getPositionsBean() {
        return positionsBean;
    }

    public void setPositionsBean(AddressPositionsBean positionsBean) {
        this.positionsBean = positionsBean;
    }


}
