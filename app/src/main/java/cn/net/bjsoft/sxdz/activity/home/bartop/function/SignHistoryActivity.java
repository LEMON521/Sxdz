package cn.net.bjsoft.sxdz.activity.home.bartop.function;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.BaseActivity;
import cn.net.bjsoft.sxdz.adapter.function.sign.FunctionSignHistoryAdapter;
import cn.net.bjsoft.sxdz.bean.function.sign.FunctionSignHistoryBean;
import cn.net.bjsoft.sxdz.dialog.ListPopupWindow;
import cn.net.bjsoft.sxdz.dialog.PickerDialog;
import cn.net.bjsoft.sxdz.utils.GsonUtil;
import cn.net.bjsoft.sxdz.utils.function.TestAddressUtils;
import cn.net.bjsoft.sxdz.utils.function.Utility;


/**
 * 签到历史页面
 * Created by Zrzc on 2017/1/10.
 */
@ContentView(R.layout.activity_function_sign_history)
public class SignHistoryActivity extends BaseActivity {

    //    @ViewInject(R.id.function_sing_history_partment)
//    private Spinner spinner_partment;
    @ViewInject(R.id.function_sing_history_partment)
    private EditText spinner_partment;
    @ViewInject(R.id.function_sing_history_date)
    private EditText history_date;

    @ViewInject(R.id.function_sing_history_humen)
    private GridView gridView_humen;

    @ViewInject(R.id.function_sing_history_map)


    private FunctionSignHistoryBean signHistoryBean;//总数据

    private ArrayList<FunctionSignHistoryBean.DepartmentListDao> departments;//全部部门集合
    private ArrayList<String> departmentNames;
    private ArrayList<FunctionSignHistoryBean.HumenListDao> humenList;//每个部门的全部的人的集合

    private HashMap<String, ArrayList<FunctionSignHistoryBean.HumenListDao>> departmentMap;//将每个部门分组
    private FunctionSignHistoryAdapter signHistoryAdapter;//分组后(每个部门)的全部的人的GridView的适配器

    private ArrayAdapter spinnerAdapter;

    @ViewInject(R.id.function_sing_history_map)
    private MapView mapView;

    private AMap aMap;

    private ArrayList<Marker> markers;
    private HashMap<String, ArrayList<Marker>> markerMap;
    private ArrayList<MarkerOptions> list;

    private Polyline polyline;
    //private ArrayList<Polyline> polylines;
    private HashMap<String, Polyline> polylinesMap;

    private ArrayList<LatLng> latLngs;

    private ListPopupWindow listPopupWindow;

    @Event(value = {R.id.function_sing_history_date
            , R.id.function_sing_history_partment})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.function_sing_history_date:
                PickerDialog.showDatePickerDialog(this, history_date, "-");
                break;
            case R.id.function_sing_history_partment:
                listPopupWindow.showWindow(departmentNames);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_function_sign_history);

        x.view().inject(this);
//        spinner_partment = (Spinner) findViewById(R.id.function_sing_history_partment);
//        gridView_humen = (GridView) findViewById(R.id.function_sing_history_humen);
        mapView = (MapView) findViewById(R.id.function_sing_history_map);
        mapView.onCreate(savedInstanceState);

        showProgressDialog();
        if (markers == null) {
            markers = new ArrayList<>();
        }

        if (departmentNames == null) {
            departmentNames = new ArrayList<>();
        }

        if (departments == null) {
            departments = new ArrayList<>();
        }

        if (departmentMap == null) {
            departmentMap = new HashMap<>();
        }

        if (humenList == null) {
            humenList = new ArrayList<>();
        }
        if (latLngs == null) {
            latLngs = new ArrayList<LatLng>();
        }
        if (polylinesMap == null) {
            //polylines = new ArrayList<>();
            polylinesMap = new HashMap<>();
        }
        if (markerMap == null) {
            markerMap = new HashMap<>();
        }
        markers.clear();
        latLngs.clear();
        departmentMap.clear();
        departmentNames.clear();
        departments.clear();
        humenList.clear();
        polylinesMap.clear();
        markerMap.clear();


        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams(TestAddressUtils.test_get_lastsign_url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                signHistoryBean = GsonUtil.getSignLastBean(result);
                if (signHistoryBean.result) {
                    LogUtil.e("获取到的条目-----------" + result);
                    departments.addAll(signHistoryBean.data);
                    {//将获取到的信息分类
                        departmentNames.add("请选择部门");
                        departmentMap.put(departmentNames.get(0), humenList);//添加一条空的部门和部门人的列表
                        // for (FunctionSignHistoryBean.DepartmentListDao department:departments){
                        for (int i = 0; i < departments.size(); i++) {
                            if (departments.get(i).department.equals("技术部")) {
                                departmentNames.add(departments.get(i).department);
                                departmentMap.put(departmentNames.get(i + 1), departments.get(i).person);
                            } else if (departments.get(i).department.equals("销售部")) {
                                departmentNames.add(departments.get(i).department);
                                departmentMap.put(departmentNames.get(i + 1), departments.get(i).person);
                            } else {
                                departmentNames.add(departments.get(i).department);
                                departmentMap.put(departmentNames.get(i + 1), departments.get(i).person);
                            }
                        }
                    }

                    init();
                } else {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissProgressDialog();
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void init() {

        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, departmentNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_partment.setAdapter(spinnerAdapter);
//
//        spinner_partment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LogUtil.e("选择的条目id" + position);
//                //if (position != 0) {
//                setGridViewData(departmentMap.get(position + ""));
//                //}
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        spinner_partment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setGridViewData(departmentMap.get(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        listPopupWindow = new ListPopupWindow(this, spinner_partment);
        listPopupWindow.setOnData(new ListPopupWindow.OnGetData() {
            @Override
            public void onDataCallBack(String result) {
                spinner_partment.setText(result);
            }
        });


        history_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * 当日期改变时,查询网络,获取最新数据
                 */
                getData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        spinner_partment.setText(departmentNames.get(0));


        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

    }

    private void setGridViewData(ArrayList<FunctionSignHistoryBean.HumenListDao> humen) {

        humenList.clear();
        if (humen != null) {
            humenList.addAll(humen);
        }

        signHistoryAdapter = null;
        aMap.clear();
        signHistoryAdapter = new FunctionSignHistoryAdapter(SignHistoryActivity.this, humenList);
        gridView_humen.setVisibility(View.VISIBLE);
        gridView_humen.setAdapter(signHistoryAdapter);


        gridView_humen.setOnTouchListener(new View.OnTouchListener() {
            //屏蔽掉滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        gridView_humen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = humenList.get(position).name;
                if (humenList.get(position).isCheck) {
                    if (aMap != null) {
                        //markers.get(position).destroy();
                        markers.clear();
                        markers = markerMap.get(name);
                        for (Marker marker : markers) {
                            marker.remove();
                        }
                        markerMap.remove(name);
                        //view.setBackgroundResource(R.drawable.biankuang_gray);
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        polylinesMap.get(name).remove();
                        //polylines.remove(name);
                        //addMarkersToMap(humenList.get(position));
                    }
                    humenList.get(position).isCheck = false;
                } else {
                    if (aMap != null) {
                        addMarkersToMap(humenList.get(position));
                    }
                    humenList.get(position).isCheck = true;
                    //view.setBackgroundResource(R.drawable.biankuang_blue);
                    view.setBackgroundColor(Color.parseColor("#" + humenList.get(position).color));
                }
                //LogUtil.e(humenList.get(position));
            }
        });
        Utility.getGridViewHeightBasedOnChildren(gridView_humen, 5);
    }


    /**
     * 初始化高德地图
     */
    private void setUpMap() {

        aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        aMap.setMapTextZIndex(2);

        dismissProgressDialog();
        markers = new ArrayList<>();
        list = new ArrayList();
        list.clear();


    }


    /**
     * 在地图上添加marker
     * 在这里为每个人的Marker
     */
    private void addMarkersToMap(final FunctionSignHistoryBean.HumenListDao bean) {
        ArrayList<FunctionSignHistoryBean.SignListDatasDao> signs = bean.sign_last;
        markers.clear();
        latLngs.clear();
        final ArrayList<Marker> markersList = new ArrayList<>();

        /**
         * 添加标记
         */
        for (int i = 0; i < signs.size(); i++) {
            FunctionSignHistoryBean.SignListDatasDao sign = signs.get(i);
            final View viewMarker = View.inflate(this, R.layout.sign_history_marker, null);
            final ImageView imageViewMarker = (ImageView) viewMarker.findViewById(R.id.marker_sign_iv);
            LinearLayout llMarker = (LinearLayout) viewMarker.findViewById(R.id.marker_sign_ll);
            TextView timeMarker = (TextView) viewMarker.findViewById(R.id.marker_sign_tv_time);
            TextView spaceMarker = (TextView) viewMarker.findViewById(R.id.marker_sign_tv_space);
            latLngs.add(new LatLng(sign.latitude, sign.longitude));


            if (!sign.sign_last_time.equals("")) {
                timeMarker.setText("签到时间:" + sign.sign_last_time);
            } else {
                timeMarker.setText("没有签到时间");
            }
            if (!sign.sign_last_place.equals("")) {
                spaceMarker.setText("签到地址:" + sign.sign_last_place);
            } else {
                spaceMarker.setText("没有签到地址");
            }
            if (!sign.pic_url.equals("")) {
                final int finalI = i;
                x.image().bind(imageViewMarker, sign.pic_url, new Callback.CommonCallback<Drawable>() {
                    Marker marker;

                    @Override
                    public void onSuccess(Drawable result) {
                        imageViewMarker.setImageDrawable(result);
                        //图片加载成功再
                        marker = drawMarkerOnMap(latLngs.get(finalI), BitmapDescriptorFactory.fromView(viewMarker), bean.name);

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        //加载失败的时候显示的图片
                        marker = drawMarkerOnMap(latLngs.get(finalI), BitmapDescriptorFactory.fromResource(R.drawable.wlh), bean.name);

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        markersList.add(marker);
                    }
                });
            } else {
                imageViewMarker.setVisibility(View.GONE);
            }

            //容易加载网络图片失败,第二次才能正常显示
//            Marker marker = drawMarkerOnMap(latLngs.get(i), BitmapDescriptorFactory.fromView(viewMarker), bean.name);
//            markersList.add(marker);

            //polylines.add(polyline);
        }
        markerMap.put(bean.name, markersList);

        /**
         * 添加线段
         */
        polyline = aMap.addPolyline(new PolylineOptions()
                .addAll(latLngs)
                .width(10)
                .color(Color.parseColor("#" + bean.color)));
        polylinesMap.put(bean.name, polyline);


    }

    /**
     * 在地图上画marker
     *
     * @param point marker坐标点位置（example:LatLng point = new LatLng(39.963175,116.400244); ）
     * @return Marker对象
     */

    private Marker drawMarkerOnMap(LatLng point, BitmapDescriptor descriptor, String id) {
        if (aMap != null && point != null) {
            Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
                    .position(point)
                    .title(id)
                    .icon(descriptor));
            //.icon(BitmapDescriptorFactory.fromBitmap(markerIcon)));
            return marker;
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


}
