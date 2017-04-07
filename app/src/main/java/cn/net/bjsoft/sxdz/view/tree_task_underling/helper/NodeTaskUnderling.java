package cn.net.bjsoft.sxdz.view.tree_task_underling.helper;

import java.util.ArrayList;
import java.util.List;

public class NodeTaskUnderling {

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
    private List<NodeTaskUnderling> children = new ArrayList<NodeTaskUnderling>();

    /**
     * 父Node
     */
    private NodeTaskUnderling parent;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    private String number = "";

    private String position = "";


    public NodeTaskUnderling() {
    }

    public NodeTaskUnderling(int id, int pId, String name, String position, String number) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.number = number;
        this.position = position;


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

    public List<NodeTaskUnderling> getChildren() {
        return children;
    }

    public void setChildren(List<NodeTaskUnderling> children) {
        this.children = children;
    }

    public NodeTaskUnderling getParent() {
        return parent;
    }

    public void setParent(NodeTaskUnderling parent) {
        this.parent = parent;
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

            for (NodeTaskUnderling node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}