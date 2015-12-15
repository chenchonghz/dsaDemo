package com.szrjk.dhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.szrjk.config.Constant;
import com.szrjk.util.SharePerferenceUtil;

/**
 * @author Sodino E-mail:sodinoopen@hotmail.com
 * @version Time：2011-6-12 下午01:34:17
 */
public class ActErrorReport extends Activity {
    private DHomeApplication softApplication;
    private String info;



    /** 标识来处。 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        softApplication = (DHomeApplication) getApplication();

        TextView tv_errormsg = (TextView) findViewById(R.id.tv_errormsg);
        Button btn_clear = (Button) findViewById(R.id.btn_clear);

        info = getIntent().getStringExtra("error");
        tv_errormsg.setText(info);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 SharePerferenceUtil.getInstance(ActErrorReport.this,
                        Constant.APP_INFO).setStringValue(Constant.ERRORMSG,"");

                Intent intent = new Intent(ActErrorReport.this, LoginActivity.class);
                startActivity(intent);
            }
        });



//        by = getIntent().getStringExtra("by");
//        info = getIntent().getStringExtra("error");
//        TextView txtHint = (TextView) findViewById(R.id.txtErrorHint);
//        txtHint.setText(getErrorHint(by));
//        EditText editError = (EditText) findViewById(R.id.editErrorContent);
//        editError.setText(info);
//        btnListener = new BtnListener();
//        btnReport = (Button) findViewById(R.id.btnREPORT);
//        btnCancel = (Button) findViewById(R.id.btnCANCEL);
//        btnReport.setOnClickListener(btnListener);
//        btnCancel.setOnClickListener(btnListener);
    }
//    private String getErrorHint(String by) {
//        String hint = "";
//        String append = "";
//        if ("uehandler".equals(by)) {
//            append = " when the app running";
//        } else if ("error.log".equals(by)) {
//            append = " when last time the app running";
//        }
//        hint = String.format(getResources().getString(R.string.errorHint), append, 1);
//        return hint;
//    }
//    public void onStart() {
//        super.onStart();
//        if (softApplication.need2Exit()) {
//            // 上一个退栈的Activity有执行“退出”的操作。
//            Log.d("ANDROID_LAB", "ActErrorReport.finish()");
//            ActErrorReport.this.finish();
//        } else {
//            // go ahead normally
//        }
//    }
//    class BtnListener implements Button.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            if (v == btnReport) {
//                // 需要 android.permission.SEND权限
//                Intent mailIntent = new Intent(Intent.ACTION_SEND);
//                mailIntent.setType("plain/text");
//                String[] arrReceiver = { "sodinoopen@hotmail.com" };
//                String mailSubject = "App Error Info[" + getPackageName() + "]";
//                String mailBody = info;
//                mailIntent.putExtra(Intent.EXTRA_EMAIL, arrReceiver);
//                mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
//                mailIntent.putExtra(Intent.EXTRA_TEXT, mailBody);
//                startActivity(Intent.createChooser(mailIntent, "Mail Sending..."));
//                ActErrorReport.this.finish();
//            } else if (v == btnCancel) {
//                ActErrorReport.this.finish();
//            }
//        }
//    }
//    public void finish() {
//        super.finish();
//        if ("error.log".equals(by)) {
//            // do nothing
//        } else if ("uehandler".equals(by)) {
//            // 1.
//            // android.os.Process.killProcess(android.os.Process.myPid());
//            // 2.
//            // ActivityManager am = (ActivityManager)
//            // getSystemService(ACTIVITY_SERVICE);
//            // am.restartPackage("lab.sodino.errorreport");
//            // 3.
//            // System.exit(0);
//            // 1.2.3.都失效了，Google你让悲催的程序员情何以堪啊。
//            softApplication.setNeed2Exit(true);
//            // ////////////////////////////////////////////////////
//            // // 另一个替换方案是直接返回“HOME”
//            // Intent i = new Intent(Intent.ACTION_MAIN);
//            // // 如果是服务里调用，必须加入newtask标识
//            // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            // i.addCategory(Intent.CATEGORY_HOME);
//            // startActivity(i);
//            // ////////////////////////////////////////////////////
//        }
//    }
}