package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.video.PhotoActivity;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;
import cn.net.bjsoft.sxdz.utils.MyToast;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;

/**
 * Created by 靳宁 on 2017/2/14.
 */
@ContentView(R.layout.fragment_register_company)
public class RegisterCompanyFragment extends BaseFragment {
    @ViewInject(R.id.register_company_back)
    private ImageView back;//返回

    @ViewInject(R.id.register_company_editer_code)
    private EditText code;//营业执照号
    @ViewInject(R.id.register_company_editer_emile_code)
    private EditText emile;//电子邮箱
    @ViewInject(R.id.register_company_editer_organization_code)
    private EditText organization;//企业名称

    @ViewInject(R.id.regiest_company_iv_front)
    private ImageView front;//营业执照照片
    @ViewInject(R.id.regiest_company_iv_front_delete)
    private ImageView delete;//营业执照照片

    @ViewInject(R.id.register_company_btn_submit)
    private ImageView over;//完成按钮

    private String filePath = "";//图片绝对路径
    private Uri fileUri = null;//图片uri
    private boolean isSet = false;//图片是否设置的开关

    @Override
    public void initData() {
        //LogUtil.e("Fragment中@@@@@@@" + "getActivity()::" + getActivity() + "/n" + "getContext()::" + getContext());
    }


    @Event(value = {R.id.register_company_back,
            R.id.regiest_company_iv_front,
            R.id.regiest_company_iv_front_delete,
            R.id.register_company_btn_submit})
    private void registerCompanyOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_company_back://返回
                getActivity().finish();
                break;
            case R.id.regiest_company_iv_front://设置照片
                if (!isSet) {//没有设置
                    MyToast.showShort(getActivity(), "拍摄照片");
                    PhotoOrVideoUtils.doPhoto(mActivity, this, front);
                }else {//设置过图片，点击就是预览图片
                    Intent intent = new Intent(mActivity,PhotoActivity.class);
                    intent.putExtra("url",filePath);
                    startActivity(intent);
                }
                break;
            case R.id.regiest_company_iv_front_delete://删除照片
                front.setImageResource(R.drawable.common_photo);
                fileUri = null;
                filePath = "";
                isSet = false;
                break;
            case R.id.register_company_btn_submit://访问网络--注册
                submit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fileUri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (fileUri != null) {
            if (!fileUri.toString().equals("")) {
                front.setImageURI(fileUri);//将获取到的图片设置到控件上
                filePath = PhotoOrVideoUtils.getPath(mActivity, fileUri);
                isSet = true;
            } else {
                MyToast.showShort(mActivity, "选择图片出错，请重新选择图片");
            }
        } else {
            MyToast.showShort(mActivity, "没有选择图片，请重新选择图片");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 将页面的数据提交到服务器的方法
     */
    private void submit() {
        MyToast.showShort(getActivity(), "将数据提交到了服务器");
    }
}
