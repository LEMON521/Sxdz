package cn.net.bjsoft.sxdz.fragment.barbotton;

import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Field;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.barbotton.ScanResultActivity;
import cn.net.bjsoft.sxdz.externaltools.zbar.CameraManager;
import cn.net.bjsoft.sxdz.externaltools.zbar.CameraPreview;
import cn.net.bjsoft.sxdz.fragment.BaseFragment;

/**
 * Created by 靳宁宁 on 2017/1/6.
 */

public class BottonScanFragment extends BaseFragment {

   // private View view;
    private String text = "";
    private String url = "";

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;

    private FrameLayout scanPreview;

    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private ImageView iv_flashlight;
    private TextView title;
    private ImageView back;

    private boolean light = true;
    private boolean playBeep;

    private Rect mCropRect = null;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private ImageScanner mImageScanner = null;


    static {
        System.loadLibrary("iconv");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, null);
        text = getArguments().getString("text");
        url = getArguments().getString("url");
        LogUtil.e("BottonScanFragment=="+text);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        findViewById();
        LogUtil.e("onActivityCreated");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate");
    }

    private void findViewById() {
        //title = (TextView) view.findViewById(R.id.scan_title_include).findViewById(R.id.title_title);
        //title.setText(text);
        scanPreview = (FrameLayout) view.findViewById(R.id.capture_preview);
        iv_flashlight = (ImageView) view.findViewById(R.id.iv_flashlight);
        scanCropView = (RelativeLayout) view.findViewById(R.id.capture_crop_layout);
        scanLine = (ImageView) view.findViewById(R.id.capture_scan_line);
        //back = (ImageView) view.findViewById(R.id.scan_title_include).findViewById(R.id.title_back);
        iv_flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeng();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initViews() {
        mImageScanner = new ImageScanner();
        mImageScanner.setConfig(0, Config.X_DENSITY, 3);
        mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(mActivity);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(mActivity, mCamera, previewCb, autoFocusCB);

        scanPreview.addView(mPreview);
//        scanPreview = mPreview;
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.85f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        scanLine.startAnimation(animation);
    }



    public void onPause() {
        super.onPause();
        releaseCamera();
        LogUtil.e("onPause");
    }



    @Override
    public void onResume() {
        super.onResume();
        initViews();
        LogUtil.e("onResume");
//        LogUtil.e("mCamera"+mCamera.toString());
        if (barcodeScanned) {
            barcodeScanned = false;
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
    }


    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();

            // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++)
                    rotatedData[x * size.height + size.height - y - 1] = data[x
                            + y * size.width];
            }

            // 宽高也要调整
            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;

            initCrop();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(rotatedData);
            barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
                    mCropRect.height());

            int result = mImageScanner.scanImage(barcode);
            String resultStr = null;

            if (result != 0) {
                SymbolSet syms = mImageScanner.getResults();
                for (Symbol sym : syms) {
                    resultStr = sym.getData();
                }
            }

            if (!TextUtils.isEmpty(resultStr)) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                barcodeScanned = true;
                Intent i=new Intent(getActivity(), ScanResultActivity.class);
                i.putExtra("scan",resultStr);
                startActivity(i);
                //scanResult.setText("barcode result " + resultStr);
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanPreview.getWidth();
        int containerHeight = scanPreview.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void openDeng()
    {
        if (this.light)
        {

            mCameraManager.openLight();
            this.light = false;
            this.iv_flashlight.setImageDrawable(getResources().getDrawable(R.drawable.btn_ligh_s));
            return;
        }else{
            mCameraManager.offLight();
            this.light = true;
            this.iv_flashlight.setImageDrawable(getResources().getDrawable(R.drawable.btn_ligh_n));
        }
    }



}
