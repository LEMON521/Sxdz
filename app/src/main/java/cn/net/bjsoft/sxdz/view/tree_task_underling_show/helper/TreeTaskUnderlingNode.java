package cn.net.bjsoft.sxdz.view.tree_task_underling_show.helper;

import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;

public class TreeTaskUnderlingNode {

    private Long id;
    /**
     * 根节点pId为0
     */
    private Long pId = 0l;


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
    private List<TreeTaskUnderlingNode> children = new ArrayList<TreeTaskUnderlingNode>();

    /**
     * 父Node
     */
    private TreeTaskUnderlingNode parent;


    private AddressPositionsBean positionsBean;


    public TreeTaskUnderlingNode(Long id, Long pId, String name, AddressPositionsBean positionsBean) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.positionsBean = positionsBean;

    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<TreeTaskUnderlingNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeTaskUnderlingNode> children) {
        this.children = children;
    }

    public TreeTaskUnderlingNode getParent() {
        return parent;
    }

    public void setParent(TreeTaskUnderlingNode parent) {
        this.parent = parent;
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

            for (TreeTaskUnderlingNode node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}