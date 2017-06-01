package cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.helper;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressDeptsBean;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean.TreeNodeAddressDeptsBean;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean.TreeNodeId;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean.TreeNodeName;
import cn.net.bjsoft.sxdz.view.tree_addresslist_zdlf.bean.TreeNodePid;


/**
 * Created by Zrzc on 2017/3/13.
 */

public class TreeHelper {

    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<TreeNode> getSortedNodes(List<T> datas,
                                                    int defaultExpandLevel)
            throws IllegalArgumentException,
            IllegalAccessException

    {
        List<TreeNode> result = new ArrayList<TreeNode>();
        //将用户数据转化为List<Node>以及设置Node间关系
        List<TreeNode> nodes = convetData2Node(datas);
        //拿到根节点
        List<TreeNode> rootNodes = getRootNodes(nodes);
        //排序
        for (TreeNode node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 过滤出所有可见的Node
     *
     * @param nodes
     * @return
     */
    public static List<TreeNode> filterVisibleNode(List<TreeNode> nodes) {
        List<TreeNode> result = new ArrayList<TreeNode>();

        for (TreeNode node : nodes) {
            // 如果为跟节点，或者上层目录为展开状态
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 将我们的数据转化为树的节点
     *
     * @param datas
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static <T> List<TreeNode> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        TreeNode node = null;

        for (T t : datas) {
            String id = "";
            String pId = "";
            String name = null;
            AddressDeptsBean addressDeptsBean = null;

            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeNodeId.class) != null) {
                    f.setAccessible(true);
                    id = (String) f.get(t);
                    LogUtil.e("==========UnderlingId=========" + id);
                }
                if (f.getAnnotation(TreeNodePid.class) != null) {
                    f.setAccessible(true);
                    pId = (String) f.get(t);
                    LogUtil.e("==========UnderlingpId=========" + pId);
                }
                if (f.getAnnotation(TreeNodeName.class) != null) {
                    f.setAccessible(true);
                    name = (String) f.get(t);
                    LogUtil.e("==========UnderlingPosition====name=====" + name);
                }
                if (f.getAnnotation(TreeNodeAddressDeptsBean.class) != null) {
                    f.setAccessible(true);
                    addressDeptsBean = (AddressDeptsBean) f.get(t);
                    LogUtil.e("==========AddressDeptsBean====name=====" + addressDeptsBean);
                }

                if (id != "" && pId != "" && name != null) {
                    break;
                }
            }
            node = new TreeNode(id, pId, name, addressDeptsBean);
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            TreeNode n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                TreeNode m = nodes.get(j);

                LogUtil.e(m.toString());
                LogUtil.e(m.toString());

                LogUtil.e(m.getpId());
                LogUtil.e(m.getId());

                LogUtil.e(n.getpId());
                LogUtil.e(n.getId());


                if (m.getpId().equals(n.getId())) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId().equals(n.getpId())) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        // 设置图片
        for (TreeNode n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    private static List<TreeNode> getRootNodes(List<TreeNode> nodes) {
        List<TreeNode> root = new ArrayList<TreeNode>();
        for (TreeNode node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<TreeNode> nodes, TreeNode node,
                                int defaultExpandLeval, int currentLevel) {

        nodes.add(node);
        if (defaultExpandLeval >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf())
            return;
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }

    /**
     * 设置节点的图标
     *
     * @param node
     */
    private static void setNodeIcon(TreeNode node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.mail_list_zdlf_down_arrow);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.mail_list_zdlf_right_arrow);
        } else
            node.setIcon(-1);
    }
}
