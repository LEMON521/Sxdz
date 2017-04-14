package cn.net.bjsoft.sxdz.view.tree_task_underling.helper;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.TreeTaskNodeDepartment;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.TreeTaskNodeId;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.TreeTaskNodeLabel;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.TreeTaskNodeTask_number;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.TreeTaskNodePid;
import cn.net.bjsoft.sxdz.view.tree_task_underling.bean.TreeTaskNodeUrl;


/**
 * Created by Zrzc on 2017/3/13.
 */

public class TreeTaskHelper {

    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<NodeTaskUnderling> getSortedNodes(List<T> datas,
                                                             int defaultExpandLevel)
            throws IllegalArgumentException,
            IllegalAccessException

    {
        List<NodeTaskUnderling> result = new ArrayList<NodeTaskUnderling>();
        //将用户数据转化为List<Node>以及设置Node间关系
        List<NodeTaskUnderling> nodes = convetData2Node(datas);
        //拿到根节点
        List<NodeTaskUnderling> rootNodes = getRootNodes(nodes);
        //排序
        for (NodeTaskUnderling node : rootNodes) {
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
    public static List<NodeTaskUnderling> filterVisibleNode(List<NodeTaskUnderling> nodes) {
        List<NodeTaskUnderling> result = new ArrayList<NodeTaskUnderling>();

        for (NodeTaskUnderling node : nodes) {
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
    private static <T> List<NodeTaskUnderling> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<NodeTaskUnderling> nodes = new ArrayList<NodeTaskUnderling>();
        NodeTaskUnderling node = null;

        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String label = null;
            String url = null;
            String department = null;
            String task_num = null;


            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeTaskNodeId.class) != null) {
                    f.setAccessible(true);
                    id = f.getInt(t);
                }
                if (f.getAnnotation(TreeTaskNodePid.class) != null) {
                    f.setAccessible(true);
                    pId = f.getInt(t);
                }
                if (f.getAnnotation(TreeTaskNodeLabel.class) != null) {
                    f.setAccessible(true);
                    label = (String) f.get(t);
                }
                if (f.getAnnotation(TreeTaskNodeUrl.class) != null) {
                    f.setAccessible(true);
                    url = (String) f.get(t);
                }

                if (f.getAnnotation(TreeTaskNodeDepartment.class) != null) {
                    f.setAccessible(true);
                    department = (String) f.get(t);
                }
                if (f.getAnnotation(TreeTaskNodeTask_number.class) != null) {
                    f.setAccessible(true);
                    task_num = (String) f.get(t);
                }

                if (id != -1 && pId != -1 && label != null) {
                    break;
                }
            }
            node = new NodeTaskUnderling(id, pId, label, url, department, task_num);
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            NodeTaskUnderling n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                NodeTaskUnderling m = nodes.get(j);
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
        for (NodeTaskUnderling n : nodes) {
            setNodeIcon(n);
            LogUtil.e("设置了图标---展开"+node.getIcon());
        }
        return nodes;
    }

    private static List<NodeTaskUnderling> getRootNodes(List<NodeTaskUnderling> nodes) {
        List<NodeTaskUnderling> root = new ArrayList<NodeTaskUnderling>();
        for (NodeTaskUnderling node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<NodeTaskUnderling> nodes, NodeTaskUnderling node,
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
    private static void setNodeIcon(NodeTaskUnderling node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_open);
            LogUtil.e("设置了图标---展开" + node.getIcon());
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_extend);
            LogUtil.e("设置了图标---展开" + node.getIcon());
        } else {
            node.setIcon(R.mipmap.subordinate_task_zdlf_post);
            LogUtil.e("设置了图标---展开"+node.getIcon());
        }

    }
}
