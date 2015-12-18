package com.szrjk.entity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.szrjk.config.Constant;
import com.szrjk.dhome.DHomeApplication;
import com.szrjk.util.SharePerferenceUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author Sodino E-mail:sodinoopen@hotmail.com
 * @version Time：2011-6-9 下午11:50:43
 */
public class UEHandler implements Thread.UncaughtExceptionHandler {
    private DHomeApplication softApp;
//    private File fileErrorLog;

    public static final String PATH_ERROR_LOG = File.separator + "data" + File.separator + "data"
            + File.separator + "com.szrjk.dhome" + File.separator + "files" + File.separator
            + "error123.log";

    public UEHandler(DHomeApplication app) {
        softApp = app;
//        fileErrorLog = new File(PATH_ERROR_LOG);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // fetch Excpetion Info
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            ex.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            info = new String(data);
            data = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // print
        long threadId = thread.getId();
        String s = PATH_ERROR_LOG;
        Log.d("ANDROID_LAB", "Thread.getName()=" + thread.getName() + " id=" + threadId + " state=" + thread.getState());
        Log.d("ANDROID_LAB", "Error[" + info + "]");
        if (threadId != 1) {
            // 此处示例跳转到汇报异常界面。
//            Intent intent = new Intent(softApp, Register1Activity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("error", info);
//            intent.putExtra("by", "uehandler");
//            softApp.startActivity(intent);
        } else {
            // 此处示例发生异常后，重新启动应用
//            Intent intent = new Intent(softApp, ActOccurError.class);
//            // 如果<span style="background-color: rgb(255, 255, 255); ">没有NEW_TASK标识且</span>是UI线程抛的异常则界面卡死直到ANR
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            softApp.startActivity(intent);
            // write 2 /data/data/<app_package>/files/error.log

//            String to = et_to.getText().toString();
//            String subject = et_subject.getText().toString();
//            String content = et_content.getText().toString();
            // 创建Intent


//            Intent emailIntent = new Intent(
//                    android.content.Intent.ACTION_SEND);
//            //设置内容类型
//            emailIntent.setType("plain/text");
//            //设置额外信息
//            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "4454368484@qq.com" });
//            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"111");
//            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, info);
//            //启动Activity
//            softApp.startActivity(Intent.createChooser(emailIntent, "发送邮件..."));

            SharePerferenceUtil perferenceUtil = SharePerferenceUtil.getInstance(softApp, Constant.APP_INFO);
            perferenceUtil.setStringValue(Constant.ERRORMSG, info);
            //自动重新启动app 进入登录界面
            Intent intent = new Intent(); 
            intent.setClassName("com.szrjk.dhome", "com.szrjk.dhome.SplashActivity");
            PendingIntent restartIntent = PendingIntent.getActivity(softApp, -1, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            AlarmManager am = (AlarmManager) softApp.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC,  System.currentTimeMillis(), restartIntent);
//            write2ErrorLog(fileErrorLog, info);
//            // kill App Progress
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void write2ErrorLog(File file, String content) {
        FileOutputStream fos = null;
        try {
            if (file.exists()) {
                // 清空之前的记录
                file.delete();
            } else {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}