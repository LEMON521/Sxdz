package cn.net.bjsoft.sxdz.view.tree_message_task_underling.helper;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.app.user.address.AddressPositionsBean;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.bean.TreeMessageTaskUnderlingId;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.bean.TreeMessageTaskUnderlingPid;
import cn.net.bjsoft.sxdz.view.tree_message_task_underling.bean.TreeMessageTaskUnderlingPosition;


/**
 * Created by Zrzc on 2017/3/13.
 */

public class TreeMessageTaskUnderlingHelper {

    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<NodeMessageTaskUnderling> getSortedNodes(List<T> datas,
                                                                    int defaultExpandLevel)
            throws IllegalArgumentException,
            IllegalAccessException

    {
        List<NodeMessageTaskUnderling> result = new ArrayList<NodeMessageTaskUnderling>();
        //将用户数据转化为List<Node>以及设置Node间关系
        List<NodeMessageTaskUnderling> nodes = convetData2Node(datas);
        //拿到根节点
        List<NodeMessageTaskUnderling> rootNodes = getRootNodes(nodes);
        //排序
        for (NodeMessageTaskUnderling node : rootNodes) {
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
    public static List<NodeMessageTaskUnderling> filterVisibleNode(List<NodeMessageTaskUnderling> nodes) {
        List<NodeMessageTaskUnderling> result = new ArrayList<NodeMessageTaskUnderling>();

        for (NodeMessageTaskUnderling node : nodes) {
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
    private static <T> List<NodeMessageTaskUnderling> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<NodeMessageTaskUnderling> nodes = new ArrayList<NodeMessageTaskUnderling>();
        NodeMessageTaskUnderling node = null;

        for (T t : datas) {
            String id = "";
            String pId = "";
            AddressPositionsBean positionsBean = null;

            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeMessageTaskUnderlingId.class) != null) {
                    f.setAccessible(true);
                    id = (String) f.get(t);
                    //LogUtil.e("==========UnderlingId=========" + id);
                }
                if (f.getAnnotation(TreeMessageTaskUnderlingPid.class) != null) {
                    f.setAccessible(true);
                    pId = (String) f.get(t);
                    //LogUtil.e("==========UnderlingpId=========" + pId);
                }
                if (f.getAnnotation(TreeMessageTaskUnderlingPosition.class) != null) {
                    f.setAccessible(true);
                    positionsBean = (AddressPositionsBean) f.get(t);
                    //LogUtil.e("==========UnderlingPosition=========" + positionsBean);
                }

                if (id != "" && pId != "" && positionsBean != null) {
                    break;
                }
            }
            node = new NodeMessageTaskUnderling();//id, pId, positionsBean);
            node.setId(id);
            node.setpId(pId);
            node.setPositionsBean(positionsBean);
            //LogUtil.e("==========node=========" + node.getId() + "::" + node.getpId() + "::" + node.getPositionsBean());
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            NodeMessageTaskUnderling n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                NodeMessageTaskUnderling m = nodes.get(j);
                //LogUtil.e((m.getpId().equals(n.getId())) + "===node==11111111===" + n.getPositionsBean().name + "::" + n.getId() + "----" + m.getPositionsBean().name + "::" + m.getpId());
                //LogUtil.e((m.getId().equals(n.getpId())) + "===node==2222===" + m.getPositionsBean().name + "::" + m.getId() + "----" + n.getPositionsBean().name + "::" + n.getpId());

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
        for (NodeMessageTaskUnderling n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    private static List<NodeMessageTaskUnderling> getRootNodes(List<NodeMessageTaskUnderling> nodes) {
        List<NodeMessageTaskUnderling> root = new ArrayList<NodeMessageTaskUnderling>();
        for (NodeMessageTaskUnderling node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<NodeMessageTaskUnderling> nodes, NodeMessageTaskUnderling node,
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
    private static void setNodeIcon(NodeMessageTaskUnderling node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_extend);
            LogUtil.e("设置了图标---展开" + node.getIcon());
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.subordinate_task_zdlf_open);
            LogUtil.e("设置了图标---合璧" + node.getIcon());
        } else {
            node.setIcon(R.mipmap.subordinate_task_zdlf_post);
            LogUtil.e("设置了图标---末节点" + node.getIcon());
        }

    }
}
