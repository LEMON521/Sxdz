package cn.net.bjsoft.sxdz.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import cn.net.bjsoft.sxdz.R;
import cn.net.bjsoft.sxdz.view.WindowEntryView;
import cn.net.bjsoft.sxdz.view.WindowRecettingPasswordView;

/**
 * Created by Zrzc on 2017/2/21.
 */

public class WindowEntryDialog {
    private Dialog dialog;
    
    public void showDialog(Context context, ArrayList<String> strs){
        if (dialog == null) {
            dialog = new Dialog(context, R.style.MIDialog);
            final WindowEntryView view = new WindowEntryView(context,strs);

            view.getConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.submit();//获取到的数据
                }
            });

            view.getCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.setCancelable(false);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void showRecettingPassword(Context context,final WindowRecettingPasswordView view){
        if (dialog == null) {
            dialog = new Dialog(context, R.style.MIDialog);

            view.getConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.submit();//获取到的数据
                    if (!view.getPassword().equals("")){
                        dialog.dismiss();
                    }
                }
            });

            view.getCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.setCancelable(false);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
