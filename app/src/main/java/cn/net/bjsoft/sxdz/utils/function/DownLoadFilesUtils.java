package cn.net.bjsoft.sxdz.utils.function;

import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import cn.net.bjsoft.sxdz.utils.MyToast;

/**
 * Created by Zrzc on 2017/4/11.
 */

public class DownLoadFilesUtils {

    //下载文件保存的地址
    private static String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "shuxin" + File.separator + "download" + File.separator;

    private  String file_url = "";

    private FragmentActivity mActivity;
    private boolean isSuccess = false;

    /**
     * 下载附件
     *
     * @param url 文件地址
     */
    public boolean downloadFile(FragmentActivity activity, String url) {
        if (TextUtils.isEmpty(url)) {
            MyToast.showShort(activity,"下载地址错误,终止下载!");
            return false;
        }
        file_url = url;
        mActivity = activity;
        String file_name = file_url.substring(file_url.lastIndexOf("/"));
        String path = "";
        // 首先判定是否有SDcard,并且可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //一定要指定文件名，不然会将下载的文件保存成为.tmp的文件
            path = BASE_PATH + file_name;
        } else {
            path = mActivity.getFilesDir().getAbsolutePath() + File.separator + file_name;
            Toast.makeText(mActivity, "SD卡不可用,将下载到手机内部", Toast.LENGTH_SHORT).show();
        }

        File file = new File(path);
        MyToast.showShort(mActivity, "开始下载附件!");
        if (file.exists() && file.length() > 0) {
            MyToast.showShort(mActivity, "文件已经下载过了,无需重新下载");
            return false;
        } else {
            RequestParams params = new RequestParams(url);
            params.setSaveFilePath(path);
            //设置断点续传
            params.setAutoResume(true);

            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {

                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    // 设置下载进度信息
                    if (isDownloading) {
                    }
                }

                @Override
                public void onSuccess(File file) {
                    // 下载成功之后就安装akp
                    isSuccess = true;
                    MyToast.showShort(mActivity, "下载成功\n请到相册或文件管理器查看");

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    isSuccess = false;
                    MyToast.showShort(mActivity, "下载失败了");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

            });
        }
        return isSuccess;
    }
}
