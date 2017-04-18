package cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.helper;

import java.util.ArrayList;
import java.util.List;

public class NodeTaskAddAddressList {

    private int id;
    /**
     * 根节点pId为0
     */
    private int pId = 0;

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
    private List<NodeTaskAddAddressList> children = new ArrayList<NodeTaskAddAddressList>();

    /**
     * 父Node
     */
    private NodeTaskAddAddressList parent;


    private String type;//类型

    private String humen_num;//部门中的人数

    private String avatar;//头像

    private String department;//部门所在部门


    public NodeTaskAddAddressList() {
    }

    public NodeTaskAddAddressList(int id, int pId, String name, String type, String avatar, String department, String humen_num) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.type = type;
        this.avatar = avatar;
        this.department = department;
        this.humen_num = humen_num;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<NodeTaskAddAddressList> getChildren() {
        return children;
    }

    public void setChildren(List<NodeTaskAddAddressList> children) {
        this.children = children;
    }

    public NodeTaskAddAddressList getParent() {
        return parent;
    }

    public void setParent(NodeTaskAddAddressList parent) {
        this.parent = parent;
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

            for (NodeTaskAddAddressList node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}