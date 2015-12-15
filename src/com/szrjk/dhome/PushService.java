package com.szrjk.dhome;

import android.content.Context;
import com.szrjk.config.Constant;
import com.szrjk.entity.UserInfo;
import com.szrjk.receiver.DHomePushService;
import com.umeng.message.PushAgent;

/**
 * denggm on 2015/12/2.
 * DHome
 */
public class PushService {


    public static void pushOnAppStart(final Context context){
        // 开启友盟推送
        final PushAgent pushAgent = PushAgent.getInstance(context);
        new Thread(){
            @Override
            public void run(){
                pushAgent.enable();
                pushAgent.onAppStart();
                pushAgent.setPushIntentServiceClass(DHomePushService.class);
                new Thread(new PushRunable(context)).start();
            }
        }.start();
    }


    static class PushRunable implements Runnable
    {

        private Context context ;
        public PushRunable(Context context) {
            this.context = context;
        }

        @Override
        public void run()
        {
            try
            {
                UserInfo uc = Constant.userInfo;
                //				PushAgent pushAgent = PushAgent.getInstance(MainActivity.this);
                //				pushAgent.removeAlias(String.valueOf(uc.getUserSeqId()), "SZRJK");
                //				// 添加友盟推送用户绑定
                //				pushAgent.addAlias(String.valueOf(uc.getUserSeqId()), "SZRJK");
                PushAgent pushAgent = PushAgent.getInstance(context);
                pushAgent.removeAlias(uc.getUserSeqId(), "SZRJK");
                // 添加友盟推送用户绑定
                pushAgent.addAlias(uc.getUserSeqId(), "SZRJK");
//				Log.i("main", "启动友盟");
//				ToastUtils.showMessage(MainActivity.this, "启动友盟");
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }


}
