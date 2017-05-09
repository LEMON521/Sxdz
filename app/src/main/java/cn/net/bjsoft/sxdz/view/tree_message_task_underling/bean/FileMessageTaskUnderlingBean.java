package cn.net.bjsoft.sxdz.view.tree_message_task_underling.bean;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;

public class FileMessageTaskUnderlingBean {
    @TreeMessageTaskUnderlingId
    private String _id;
    @TreeMessageTaskUnderlingPid
    private String parentId;
    @TreeMessageTaskUnderlingPosition
    private AddressPositionsBean positionsBean;


    public FileMessageTaskUnderlingBean(
            String _id
            , String parentId
            , AddressPositionsBean positionsBean) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.positionsBean = positionsBean;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public AddressPositionsBean getPositionsBean() {
        return positionsBean;
    }

    public void setPositionsBean(AddressPositionsBean positionsBean) {
        this.positionsBean = positionsBean;
    }


}
