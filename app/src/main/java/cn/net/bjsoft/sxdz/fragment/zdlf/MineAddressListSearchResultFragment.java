package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.zdlf.AddressListSearchResultAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListBean;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/3/24.
 */
@ContentView(R.layout.fragment_mine_zdlf_address_list_search)
public class MineAddressListSearchResultFragment extends BaseFragment {
    @ViewInject(R.id.title_back)
    private ImageView title_back;
    @ViewInject(R.id.title_title)
    private TextView title;

    @ViewInject(R.id.search_edittext)
    private EditText search_edit;

    @ViewInject(R.id.address_list_result)
    private ListView resultListView;
    @ViewInject(R.id.address_list_result_hint)
    private TextView hint;


    private ArrayList<String> childAvatarList;
    private ArrayList<String> childNameList;
    private ArrayList<String> childNumList;
    private ArrayList<AddressListBean.AddressListDao> addressList;
    private ArrayList<AddressListBean.AddressListDao> resultList;
    private AddressListSearchResultAdapter resultAdapter;

    private String searchStr = "";

    @Override
    public void initData() {
        title_back.setVisibility(View.VISIBLE);
        title.setText("搜索联系人结果");
        //setTreeView();
        if (childAvatarList == null) {
            childAvatarList = new ArrayList<>();
        }
        childAvatarList.clear();

        if (childNameList == null) {
            childNameList = new ArrayList<>();
        }
        childNameList.clear();

        if (childNumList == null) {
            childNumList = new ArrayList<>();
        }
        childNumList.clear();

        if (resultList == null) {
            resultList = new ArrayList<>();
        }
        resultList.clear();

        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        addressList.clear();

        if (resultAdapter == null) {
            resultAdapter = new AddressListSearchResultAdapter(mActivity, resultList);
        }
        resultListView.setAdapter(resultAdapter);


        //resultList = (ArrayList<AddressListBean.AddressListDao>) getArguments().get("address_list");
        Bundle bundle = getArguments().getBundle("address_list_search_result_bundle");
        childAvatarList.addAll(bundle.getStringArrayList("address_list_avatar"));
        childAvatarList.addAll(bundle.getStringArrayList("address_list_name"));
        childAvatarList.addAll(bundle.getStringArrayList("address_list_num"));
        searchStr = bundle.getString("address_list_search_str").toString();
        LogUtil.e("size" + childAvatarList.size() + "::" + childAvatarList.size() + "::" + childAvatarList.size() + "::");
        searchData(searchStr);


    }


    private void searchData(String searchStr) {
        resultList.clear();
//        for (AddressListBean.AddressListDao dao : addressList) {
//            if (dao.name.contains(searchStr)) {
//                resultList.add(dao);
//                break;
//            }
//            if (dao.phone_number.contains(searchStr)) {
//                resultList.add(dao);
//                break;
//            }
//        }

        LogUtil.e("size" + childAvatarList.size() + "::" + childAvatarList.size() + "::" + childAvatarList.size() + "::");

        for (int i = 0; i < childAvatarList.size(); i++) {
            AddressListBean bean = new AddressListBean();
            AddressListBean.AddressListDao dao = bean.new AddressListDao();
            if (childNameList.get(i).contains(searchStr)) {
                dao.avatar_url = childAvatarList.get(i);
                dao.name = childNameList.get(i);
                dao.phone_number = childNumList.get(i);
            } else if (childNumList.get(i).contains(searchStr)) {
                dao.avatar_url = childAvatarList.get(i);
                dao.name = childNameList.get(i);
                dao.phone_number = childNumList.get(i);
            }
            resultList.add(dao);
        }
        if (resultList.size() > 0) {
            hint.setVisibility(View.GONE);
        } else {
            hint.setVisibility(View.VISIBLE);
        }
        resultAdapter.notifyDataSetChanged();
    }


    /**
     * 功能按钮
     *
     * @param view
     */
    @Event(value = {R.id.title_back
            , R.id.search_delete
            , R.id.search_text})
    private void addressResultOnClick(View view) {
        switch (view.getId()) {
            case R.id.title_back://返回
                mActivity.finish();
                break;

            case R.id.search_delete://清除搜索框
                search_edit.setText("");
                break;

            case R.id.search_text://搜索事件
                searchStr = search_edit.getText().toString().trim();
                if (!searchStr.equals("")) {
                    searchData(searchStr);
                } else {
                    MyToast.showShort(mActivity, "请输入搜索内容!");
                    return;
                }
                break;

        }
    }


}
