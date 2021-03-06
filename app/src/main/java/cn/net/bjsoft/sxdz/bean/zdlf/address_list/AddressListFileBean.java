package cn.net.bjsoft.sxdz.bean.zdlf.address_list;



public class AddressListFileBean {
    @TreeNodeId
    private Long _id;
    @TreeNodePid
    private Long parentId;
    @TreeNodeLabel
    private String name;
    @TreeNodeUrl
    private String url;
    @TreeNodeStation
    private String station;
    @TreeNodePhone_number
    private String phone_number;
    @TreeNodeType
    private String type;
    @TreeNodeAvatar_url
    private String avatar_url;


    private long length;
    private String desc;


    public AddressListFileBean(Long _id, Long parentId, String name, String station, String phone_number, String type, String avatar_url) {
        super();
//        set_id(_id);
        this._id = _id;
//        setParentId(parentId);
        this.parentId = parentId;
//        setName(name);
        this.name = name;
//        setStation(station);
        this.station = station;
//        setPhone_number(phone_number);
        this.phone_number = phone_number;
//        setType(type);
        this.type = type;
//        setAvatar_url(avatar_url);
        this.avatar_url = avatar_url;

    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
