package cn.net.bjsoft.sxdz.view.tree_message_task_underling.helper;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;

public class NodeMessageTaskUnderling {

    private String id;
    /**
     * 根节点pId为0
     */
    private String pId = "";

    /**
     * 当前的级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    private int icon;

    /**
     * 下一级的子Node
     */
    private List<NodeMessageTaskUnderling> children = new ArrayList<NodeMessageTaskUnderling>();

    /**
     * 父Node
     */
    private NodeMessageTaskUnderling parent;


    private AddressPositionsBean positionsBean;


    public NodeMessageTaskUnderling() {
        super();
    }

    public NodeMessageTaskUnderling(String _id
            , String _pId
            , AddressPositionsBean positionsBean) {
        super();
        this.id = _id;
        this.pId = _pId;
        this.positionsBean = positionsBean;


    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<NodeMessageTaskUnderling> getChildren() {
        return children;
    }

    public void setChildren(List<NodeMessageTaskUnderling> children) {
        this.children = children;
    }

    public NodeMessageTaskUnderling getParent() {
        return parent;
    }

    public void setParent(NodeMessageTaskUnderling parent) {
        this.parent = parent;
    }
    public AddressPositionsBean getPositionsBean() {
        return positionsBean;
    }

    public void setPositionsBean(AddressPositionsBean positionsBean) {
        this.positionsBean = positionsBean;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * 获取level
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * 设置展开
     *
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {

            for (NodeMessageTaskUnderling node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}