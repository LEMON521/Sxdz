package cn.net.bjsoft.sxdz.view.treeview.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodeAvatar_url;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodeId;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodeLabel;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodePhone_number;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodePid;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodeStation;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodeType;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.TreeNodeUrl;


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
    public static <T> List<Node> getSortedNodes(List<T> datas,
                                                int defaultExpandLevel)
            throws IllegalArgumentException,
            IllegalAccessException

    {
        List<Node> result = new ArrayList<Node>();
        //将用户数据转化为List<Node>以及设置Node间关系
        List<Node> nodes = convetData2Node(datas);
        //拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        //排序
        for (Node node : rootNodes) {
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
    public static List<Node> filterVisibleNode(List<Node> nodes) {
        List<Node> result = new ArrayList<Node>();

        for (Node node : nodes) {
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
    private static <T> List<Node> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<Node> nodes = new ArrayList<Node>();
        Node node = null;

        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String label = null;
            String url = null;
            String station = null;
            String phone_number = null;
            String type = null;
            String avatar_url = null;


            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeNodeId.class) != null) {
                    f.setAccessible(true);
                    id = f.getInt(t);
                }
                if (f.getAnnotation(TreeNodePid.class) != null) {
                    f.setAccessible(true);
                    pId = f.getInt(t);
                }
                if (f.getAnnotation(TreeNodeLabel.class) != null) {
                    f.setAccessible(true);
                    label = (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodeUrl.class) != null) {
                    f.setAccessible(true);
                    url = (String) f.get(t);
                }

                if (f.getAnnotation(TreeNodeStation.class) != null) {
                    f.setAccessible(true);
                    station = (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodePhone_number.class) != null) {
                    f.setAccessible(true);
                    phone_number = (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodeType.class) != null) {
                    f.setAccessible(true);
                    type = (String) f.get(t);
                }
                if (f.getAnnotation(TreeNodeAvatar_url.class) != null) {
                    f.setAccessible(true);
                    avatar_url = (String) f.get(t);
                }

                if (id != -1 && pId != -1 && label != null) {
                    break;
                }
            }
            node = new Node(id, pId, label);
            node.setUrl(url);
            node.setStation(station);
            node.setPhone_number(phone_number);
            node.setType(type);
            node.setAvatar_url(avatar_url);
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
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
        for (Node n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<Node> nodes, Node node,
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
    private static void setNodeIcon(Node node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.mipmap.mail_list_zdlf_down_arrow);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.mipmap.mail_list_zdlf_right_arrow);
        } else
            node.setIcon(-1);
    }
}
