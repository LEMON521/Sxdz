package cn.net.bjsoft.sxdz.view.tree_task_underling.bean;

public class FileTaskBean {
    @TreeTaskNodeId
    private int _id;
    @TreeTaskNodePid
    private int parentId;
    @TreeTaskNodeLabel
    private String name;
    @TreeTaskNodeUrl
    private String url;
    @TreeTaskNodeDepartment
    private String department;
    @TreeTaskNodeTask_number
    private String task_num;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTask_num() {
        return task_num;
    }

    public void setTask_num(String task_num) {
        this.task_num = task_num;
    }

    public FileTaskBean(int _id
            , int parentId
            , String name
            , String url
            , String department
            , String task_num) {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
        this.url = url;
        this.department = department;
        this.task_num = task_num;
    }


}
