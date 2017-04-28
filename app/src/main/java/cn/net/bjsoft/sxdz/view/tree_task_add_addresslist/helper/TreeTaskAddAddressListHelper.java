package cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListNodeAvatar;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListNodeDeparment;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListNodeId;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListNodeName;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListNodePid;
import cn.net.bjsoft.sxdz.view.tree_task_add_addresslist.bean.TreeTaskAddAddressListNodeType;


/**
 * Created by Zrzc on 2017/3/13.
 */

public class TreeTaskAddAddressListHelper {

    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<NodeTaskAddAddressList> getSortedNodes(List<T> datas,
                                                                  int defaultExpandLevel)
            throws IllegalArgumentException,
            IllegalAccessException

    {
        List<NodeTaskAddAddressList> result = new ArrayList<NodeTaskAddAddressList>();
        //将用户数据转化为List<Node>以及设置Node间关系

        List<NodeTaskAddAddressList> nodes = convetData2Node(datas);

        //拿到根节点
        List<NodeTaskAddAddressList> rootNodes = getRootNodes(nodes);
        //排序
        for (NodeTaskAddAddressList node : rootNodes) {
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
    public static List<NodeTaskAddAddressList> filterVisibleNode(List<NodeTaskAddAddressList> nodes) {
        List<NodeTaskAddAddressList> result = new ArrayList<NodeTaskAddAddressList>();

        for (NodeTaskAddAddressList node : nodes) {
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
    private static <T> List<NodeTaskAddAddressList> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<NodeTaskAddAddressList> nodes = new ArrayList<NodeTaskAddAddressList>();
        NodeTaskAddAddressList node = null;


        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String label = null;
            String type = null;
            String avatar = null;
            String department = null;
            String humen_num = null;


            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeTaskAddAddressListNodeId.class) != null) {
                    f.setAccessible(true);
                    id = f.getInt(t);
                }
                if (f.getAnnotation(TreeTaskAddAddressListNodePid.class) != null) {
                    f.setAccessible(true);
                    pId = f.getInt(t);
                }
                if (f.getAnnotation(TreeTaskAddAddressListNodeName.class) != null) {
                    f.setAccessible(true);
                    label = (String) f.get(t);
                }
                if (f.getAnnotation(TreeTaskAddAddressListNodeType.class) != null) {
                    f.setAccessible(true);
                    type = (String) f.get(t);
                }

                if (f.getAnnotation(TreeTaskAddAddressListNodeAvatar.class) != null) {
                    f.setAccessible(true);
                    avatar = (String) f.get(t);
                }
                if (f.getAnnotation(TreeTaskAddAddressListNodeDeparment.class) != null) {
                    f.setAccessible(true);
                    department = (String) f.get(t);
                }
                if (id != -1 && pId != -1 && label != null) {
                    break;
                }
            }
            node = new NodeTaskAddAddressList(id, pId, label, type, avatar, department, "");
            nodes.add(node);
        }


        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            NodeTaskAddAddressList n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                NodeTaskAddAddressList m = nodes.get(j);
                if (m.getpId() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getpId()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        // 设置图片
        for (NodeTaskAddAddressList n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    private static List<NodeTaskAddAddressList> getRootNodes(List<NodeTaskAddAddressList> nodes) {
        List<NodeTaskAddAddressList> root = new ArrayList<NodeTaskAddAddressList>();
        for (NodeTaskAddAddressList node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<NodeTaskAddAddressList> nodes, NodeTaskAddAddressList node,
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
    private static void setNodeIcon(NodeTaskAddAddressList node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_extend);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_open);
        } else
            node.setIcon(-1);
    }
}
