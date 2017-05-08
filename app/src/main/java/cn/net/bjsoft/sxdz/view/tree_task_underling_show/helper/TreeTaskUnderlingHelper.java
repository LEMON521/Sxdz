package cn.net.bjsoft.sxdz.view.tree_task_underling_show.helper;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.bean.TreeNodeAddressTaskUnderlingPositionsBean;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.bean.TreeNodeTaskUnderlingId;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.bean.TreeNodeTaskUnderlingName;
import cn.net.bjsoft.sxdz.view.tree_task_underling_show.bean.TreeNodeTaskUnderlingPid;


/**
 * Created by Zrzc on 2017/3/13.
 */

public class TreeTaskUnderlingHelper {

    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<TreeTaskUnderlingNode> getSortedNodes(List<T> datas,
                                                                 int defaultExpandLevel)
            throws IllegalArgumentException,
            IllegalAccessException

    {
        List<TreeTaskUnderlingNode> result = new ArrayList<TreeTaskUnderlingNode>();
        //将用户数据转化为List<Node>以及设置Node间关系
        List<TreeTaskUnderlingNode> nodes = convetData2Node(datas);
        //拿到根节点
        List<TreeTaskUnderlingNode> rootNodes = getRootNodes(nodes);
        //排序
        for (TreeTaskUnderlingNode node : rootNodes) {
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
    public static List<TreeTaskUnderlingNode> filterVisibleNode(List<TreeTaskUnderlingNode> nodes) {
        List<TreeTaskUnderlingNode> result = new ArrayList<TreeTaskUnderlingNode>();

        for (TreeTaskUnderlingNode node : nodes) {
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
    private static <T> List<TreeTaskUnderlingNode> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<TreeTaskUnderlingNode> nodes = new ArrayList<TreeTaskUnderlingNode>();
        TreeTaskUnderlingNode node = null;

        for (T t : datas) {
            Long id = -1l;
            Long pId = -1l;
            String name = null;
            AddressPositionsBean positionsBean = null;

            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeNodeTaskUnderlingId.class) != null) {
                    f.setAccessible(true);
                    id = (Long) f.get(t);
                    LogUtil.e("=======TaskUnderlingId======="+id);
                }
                if (f.getAnnotation(TreeNodeTaskUnderlingPid.class) != null) {
                    f.setAccessible(true);
                    pId = (Long) f.get(t);
                    LogUtil.e("=======TaskUnderlingpId======="+pId);
                }
                if (f.getAnnotation(TreeNodeTaskUnderlingName.class) != null) {
                    f.setAccessible(true);
                    name = (String) f.get(t);
                    LogUtil.e("=======TaskUnderlingName======="+name);
                }
                if (f.getAnnotation(TreeNodeAddressTaskUnderlingPositionsBean.class) != null) {
                    f.setAccessible(true);
                    positionsBean = (AddressPositionsBean) f.get(t);
                    LogUtil.e("=======TaskUnderlingpositionsBean======="+positionsBean);
                }


                if (id != -1 && pId != -1 && name != null) {
                    break;
                }
            }
            node = new TreeTaskUnderlingNode(id, pId, name, positionsBean);
            nodes.add(node);
            LogUtil.e("=======------------------------n======="+positionsBean);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            TreeTaskUnderlingNode n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                TreeTaskUnderlingNode m = nodes.get(j);
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
        for (TreeTaskUnderlingNode n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    private static List<TreeTaskUnderlingNode> getRootNodes(List<TreeTaskUnderlingNode> nodes) {
        List<TreeTaskUnderlingNode> root = new ArrayList<TreeTaskUnderlingNode>();
        for (TreeTaskUnderlingNode node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<TreeTaskUnderlingNode> nodes, TreeTaskUnderlingNode node,
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
    private static void setNodeIcon(TreeTaskUnderlingNode node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_extend);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_open);
        } else {
            node.setIcon(R.mipmap.subordinate_task_zdlf_post);
        }
    }
}
