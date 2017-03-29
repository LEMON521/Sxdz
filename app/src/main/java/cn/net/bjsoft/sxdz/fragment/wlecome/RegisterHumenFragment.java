package cn.net.bjsoft.sxdz.fragment.wlecome;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xutils.common.util.LogUtil;
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
@ContentView(R.layout.fragment_register_humen)
public class RegisterHumenFragment extends BaseFragment {
//    @ViewInject(R.id.register_humen_back)
//    private ImageView back;//返回

    @ViewInject(R.id.register_humen_editer_code)
    private EditText code;//身份证号
    @ViewInject(R.id.register_humen_editer_emile_code)
    private EditText emile;//电子邮箱
    @ViewInject(R.id.register_humen_editer_organization_code)
    private EditText organization;//所在机构


    @ViewInject(R.id.regiest_humen_ll_front)
    private LinearLayout ll_front;//营业执照照片
    @ViewInject(R.id.regiest_humen_ll_back)
    private LinearLayout ll_back;//营业执照照片

    @ViewInject(R.id.regiest_humen_iv_front)
    private ImageView front;//营业执照照片
    @ViewInject(R.id.regiest_humen_iv_front_delete)
    private ImageView front_delete;//营业执照照片
    @ViewInject(R.id.regiest_humen_iv_back)
    private ImageView back;//营业执照照片
    @ViewInject(R.id.regiest_humen_iv_back_delete)
    private ImageView back_delete;//营业执照照片


    @ViewInject(R.id.register_humen_btn_submit)
    private ImageView over;//完成按钮

    private String[] filePath = {"", ""};//图片绝对路径
    private Uri[] fileUri = {null, null};//图片uri
    private boolean[] isSet = {false, false};//图片是否设置的开关
    private String whitchImage = "front";//"front","back"---是哪个图片点击事件而想要获取照片的标识符

    @Override
    public void initData() {
        //ll_back.setVisibility(View.GONE);
    }

    @Event(value = {R.id.register_humen_back,
            R.id.regiest_humen_iv_front,
            R.id.regiest_humen_iv_front_delete,
            R.id.regiest_humen_iv_back,
            R.id.regiest_humen_iv_back_delete,
            R.id.register_humen_btn_submit})
    private void registerhumenOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_humen_back://返回
                getActivity().finish();
                break;

            case R.id.regiest_humen_iv_front://设置照片
                if (!isSet[0]) {//没有设置
                    //MyToast.showShort(getActivity(), "拍摄照片");
                    whitchImage = "front";
                    PhotoOrVideoUtils.doPhotoOrVideo(mActivity, this, front);
//                    ll_back.setVisibility(View.VISIBLE);
                } else {//设置过图片，点击就是预览图片
                    Intent intent = new Intent(mActivity, PhotoActivity.class);
                    intent.putExtra("url", filePath[0]);
                    startActivity(intent);
                }
                break;

            case R.id.regiest_humen_iv_front_delete://删除照片
                front.setImageResource(R.drawable.common_photo);
                fileUri[0] = null;
                filePath[0] = "";
                isSet[0] = false;
                break;

            case R.id.regiest_humen_iv_back://设置背面照片
                if (!isSet[1]) {//没有设置
                    whitchImage = "back";
                    //MyToast.showShort(getActivity(), "拍摄照片");
                    PhotoOrVideoUtils.doPhoto(mActivity, this, back);
                } else {//设置过图片，点击就是预览图片
                    Intent intent = new Intent(mActivity, PhotoActivity.class);
                    intent.putExtra("url", filePath[1]);
                    startActivity(intent);
                }
                break;
            case R.id.regiest_humen_iv_back_delete://删除背面照片
//                if (!isSet[0]){//如果正面不是默认的话，
//                    ll_back.setVisibility(View.GONE);
//                }
                back.setImageResource(R.drawable.common_photo);
                fileUri[1] = null;
                filePath[1] = "";
                isSet[1] = false;
                break;
            case R.id.register_humen_btn_submit://访问网络--注册
                submit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoOrVideoUtils.getFileUri(requestCode, resultCode, data);
        if (uri != null) {
            if (!uri.toString().equals("")) {
                if (whitchImage.equals("front")) {
                    fileUri[0] = uri;
                    LogUtil.e("获取到的uri为====="+uri);
                    front.setImageURI(fileUri[0]);//将获取到的图片设置到控件上
                    filePath[0] = PhotoOrVideoUtils.getPath(mActivity, fileUri[0]);
                    isSet[0] = true;
                }
                if (whitchImage.equals("back")) {
                    fileUri[1] = uri;
                    back.setImageURI(fileUri[1]);//将获取到的图片设置到控件上
                    filePath[1] = PhotoOrVideoUtils.getPath(mActivity, fileUri[1]);
                    isSet[1] = true;
                }
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
