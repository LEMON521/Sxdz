package cn.net.bjsoft.sxdz.fragment.zdlf;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.adapter.zdlf.AddressListSearchResultAdapter;
import cn.net.bjsoft.sxdz.bean.zdlf.address_list.AddressListBean;
import cn.net.bjsoft.sxdz.dialog.DialingPopupWindow;
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
    private LinearLayout hint;


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
        childNameList.addAll(bundle.getStringArrayList("address_list_name"));
        childNumList.addAll(bundle.getStringArrayList("address_list_num"));
        searchStr = bundle.getString("address_list_search_str").toString();

        //addressList
        for (int i = 0; i < childNameList.size(); i++) {
            AddressListBean bean = null;
            bean = new AddressListBean();
            AddressListBean.AddressListDao dao = null;
            dao = bean.new AddressListDao();
            dao.avatar_url = childAvatarList.get(i);
            dao.name = childNameList.get(i);
            dao.phone_number = childNumList.get(i);
            addressList.add(dao);
        }


        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchData(s.toString());
            }
        });
        //searchData(searchStr);


        search_edit.setText(searchStr);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialingPopupWindow window = new DialingPopupWindow(mActivity, view, resultList.get(position).phone_number);
            }
        });

    }


    private void searchData(String searchStr) {
        resultList.clear();

        if (searchStr.equals("")) {
            resultList.addAll(addressList);
        } else {

            for (int i = 0; i < addressList.size(); i++) {
                String name = addressList.get(i).name;
                String number = addressList.get(i).phone_number;

                if (name.contains(searchStr)) {
                    resultList.add(addressList.get(i));
                    break;
                } else if (number.contains(searchStr)) {
                    resultList.add(addressList.get(i));
                }

            }
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
