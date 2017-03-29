package cn.net.bjsoft.sxdz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.view.annotation.ContentView;

import cn.net.bjsoft.sxdz.R;

/**
 * Created by Zrzc on 2017/2/13.
 */

@ContentView(R.layout.fragment_logo)
public class LogoFragment  extends BaseFragment {
    @Override
    public void initData() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_logo, container, false);
    }
}
