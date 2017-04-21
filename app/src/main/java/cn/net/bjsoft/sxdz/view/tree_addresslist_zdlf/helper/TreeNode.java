package cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressEmployeesBean;

public class TreeNode {

    private int id;
    /**
     * 根节点pId为0
     */
    private int pId = 0;

    //显示样式
    private String style;

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


    private AddressEmployeesBean employeesBean;


    public TreeNode(int id, int pId, String style, AddressEmployeesBean employeesBean) {
        super();
        this.id = id;
        this.pId = pId;
        this.style = style;
        this.employeesBean = employeesBean;

    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
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