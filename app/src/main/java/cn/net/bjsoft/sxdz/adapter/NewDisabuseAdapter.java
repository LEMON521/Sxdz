package cn.net.bjsoft.sxdz.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.activity.home.bartop.video.PhotoActivity;
import cn.net.bjsoft.sxdz.bean.community.DisabuseNewPhotoBean;
import cn.net.bjsoft.sxdz.dialog.AppProgressDialog;
import cn.net.bjsoft.sxdz.fragment.bartop.community.disabuse.NewDisabuseFragment;
import cn.net.bjsoft.sxdz.utils.function.PhotoOrVideoUtils;


/**
 * Created by Zrzc on 2017/1/12.
 */

public class NewDisabuseAdapter extends BaseAdapter {
    private FragmentActivity context;
    private ArrayList<DisabuseNewPhotoBean> list;
    private NewDisabuseFragment fragment;
    private AppProgressDialog progressDialog;
    private PopupWindow popupWindow;

    private static int REQUEST_CODE_TAKE_PHOTO = 100;
    private static int REQUEST_CODE_GET_PHOTO = 200;

    public NewDisabuseAdapter(FragmentActivity context, ArrayList<DisabuseNewPhotoBean> list, NewDisabuseFragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
        LogUtil.e("context" + context);
        LogUtil.e("fragment" + fragment);
    }

    public void refresh(ArrayList<DisabuseNewPhotoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder tag = null;
        ContentResolver cr = context.getContentResolver();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_childtask, null);
            tag = new Holder();
            tag.name = (TextView) convertView.findViewById(R.id.name);
            tag.delect = (ImageView) convertView.findViewById(R.id.delete);
            tag.imgview = (ImageView) convertView.findViewById(R.id.imgview);
            convertView.setTag(tag);
        }
        //Log.e("tag",videoTaskArrayList.get(position).getMediaurl());
        //设置数据
        Holder holder = (Holder) convertView.getTag();
        //holder.imgview.

        if (list.get(position).tag.equals("1")) {
            holder.imgview.setImageResource(R.drawable.common_add);
            holder.imgview.setTag("add");
            holder.name.setText(list.get(position).name);
            holder.delect.setVisibility(View.GONE);
        } else if (list.get(position).tag.equals("2")) {
            //直接传递URI设置图片
            //holder.imgview.setImageURI(list.get(position).imgUri);
            x.image().bind(holder.imgview,list.get(position).imgUrl);
            //MyBitmapUtils.getInstance(context).display(holder.imgview,list.get(position).imgUrl);
            holder.imgview.setTag("img");
            holder.name.setText(list.get(position).name);
            holder.delect.setVisibility(View.VISIBLE);
        }

//        //点击名称修改
//        holder.name.setTag(position);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView;
                popupWindow = null;
                if (popupWindow == null) {
                    LayoutInflater mLayoutInflater = LayoutInflater.from(context);
                    contentView = mLayoutInflater.inflate(R.layout.window_updata, null);
                    final EditText name = (EditText) contentView.findViewById(R.id.name);
                    name.setText(list.get(position).name);
                    TextView sure = (TextView) contentView.findViewById(R.id.sure);
                    final TextView cancle = (TextView) contentView.findViewById(R.id.cancle);
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*if(videoTaskArrayList.get(i).getId().equals("")){
                        videoTaskArrayList.get(i).setName(name.getText().toString().trim());
                        refresh(videoTaskArrayList);
                    }else{
                        updataName(i,name.getText().toString().trim());
                    }*/
                            list.get(position).name = (name.getText().toString().trim());
                            refresh(list);
                            popupWindow.dismiss();
                        }
                    });
                    popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }

                ColorDrawable cd = new ColorDrawable(0x000000);
                popupWindow.setBackgroundDrawable(cd);
                //产生背景变暗效果
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 0.4f;
                ((Activity) context).getWindow().setAttributes(lp);

                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation((View) v.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.update();
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    //在dismiss中恢复透明度
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                        lp.alpha = 1f;
                        ((Activity) context).getWindow().setAttributes(lp);
                    }
                });

            }
        });
//
        //点击图片拍摄
        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("add")) {
                    getphoto(v);

                } else if (v.getTag().toString().equals("img")) {
                    //MyToast.showShort(context, "我是一张图片！");
                    Intent intent = new Intent();
                    intent.setClass(context, PhotoActivity.class);
                    //LogUtil.e("list.get(position).imgUrl￥￥￥￥￥￥￥" + list.get(position).imgUrl);
                    intent.putExtra("url", list.get(position).imgUrl.toString());
                    context.startActivity(intent);
                }
            }
        });

        //删除图片
        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).tag.equals("1"))
                {
                    return;
                }
                list.remove(position);
                refresh(list);
            }
        });
        return convertView;
    }

    public static class Holder {
        public TextView name;
        public ImageView delect, imgview;
    }

    /**
     * 设置图片
     * 选择是相册还是拍照
     *
     * @param v
     */
    private void getphoto(View v) {
        PhotoOrVideoUtils.doPhoto(context,fragment,v);
    }


}
