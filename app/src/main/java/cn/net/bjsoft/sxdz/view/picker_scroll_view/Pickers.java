package cn.net.bjsoft.sxdz.view.picker_scroll_view;

/**
 * Created by Zrzc on 2017/4/10.
 */
import java.io.Serializable;

/**
 *
 * @author zengtao 2015年5月20日下午7:18:14
 *
 */
public class Pickers implements Serializable {

    private static final long serialVersionUID = 1L;

    private String showConetnt;
    private String showId;

    public String getShowConetnt() {
        return showConetnt;
    }

    public String getShowId() {
        return showId;
    }

    public Pickers(String showConetnt, String showId) {
        super();
        this.showConetnt = showConetnt;
        this.showId = showId;
    }

    public Pickers() {
        super();
    }

}