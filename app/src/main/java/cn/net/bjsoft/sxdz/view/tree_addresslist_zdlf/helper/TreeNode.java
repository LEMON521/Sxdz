package cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;

public class TreeNode {

    private String id;
    /**
     * 根节点pId为0
     */
    private String pId;


    //显示样式
    private String name;

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
    private List<TreeNode> children = new ArrayList<TreeNode>();

    /**
     * 父Node
     */
    private TreeNode parent;


    private AddressDeptsBean addressDeptsBean;


    public TreeNode(String id, String pId, String name, AddressDeptsBean addressDeptsBean) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.addressDeptsBean = addressDeptsBean;

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

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
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

            for (TreeNode node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}