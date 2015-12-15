package com.szrjk.dhome;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_digital_doctor_platform_service_contract)
public class DigitalDoctorPlatformServiceContractActivity extends BaseActivity {

	@ViewInject(R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(R.id.tv_main8)
	private TextView tv_main8;

	@ViewInject(R.id.tv_main15)
	private TextView tv_main15;

	@ViewInject(R.id.tv_main16)
	private TextView tv_main16;

	@ViewInject(R.id.tv_main17)
	private TextView tv_main17;

	@ViewInject(R.id.tv_main18)
	private TextView tv_main18;

	@ViewInject(R.id.tv_main19)
	private TextView tv_main19;

	@ViewInject(R.id.tv_main21)
	private TextView tv_main21;

	@ViewInject(R.id.tv_main23)
	private TextView tv_main23;

	@ViewInject(R.id.tv_main24)
	private TextView tv_main24;

	@ViewInject(R.id.tv_main25)
	private TextView tv_main25;

	@ViewInject(R.id.tv_main27)
	private TextView tv_main27;

	private DigitalDoctorPlatformServiceContractActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		initLayout();
	}

	private void initLayout() {
		String main8 = new String(
				"【用户资格】您确认，在您开始注册程序使用数字医生平台服务前，您应当是符合中华人民共和国国务院教育和卫生行政部门规定的医务工作者（包括经县级以上人民政府卫生行政部门确定的传统医学专业组织或者医疗、预防、保健机构的工作人员）、高等院校医学专业的在校师生或者毕业生。医务工作者应当具有相应的医师执业资格证书、助理医师执业资格证书、护士执业资格证书、药师执业资格证书等。");
		String main15 = new String(
				"【更新维护】您应当及时更新您提供的信息，在法律有明确规定要求数字人作为平台服务提供者必须对部分用户的信息进行核实的情况下，数字人将依法不时地对您的信息进行检查核实，您应当配合提供最新、真实、完整的信息。如数字人按您最后一次提供的信息与您联系未果、您未按数字人的要求及时提供信息、您提供的信息存在明显不实的，您将承担因此对您自身、他人及数字人造成的全部损失与不利后果。");
		String main16 = new String(
				"数字人为数字医生平台向您提供的服务付出了大量的成本，向您提供的服务目前是免费的。如未来数字人向您收取合理费用，数字人会采取合理途径并以足够合理的期限提前通过法定程序并以本协议第八条约定的方式通知您，确保您有充分选择的权利。");
		String main17 = new String(
				"【不可抗力】数字人负责'按现状'和'可得到'的状态向您提供数字医生平台服务。数字人依法律规定承担基础保障义务，但无法对由于信息网络设备维护、连接故障，电脑、通讯或其他系统的故障，电力故障，罢工，暴乱，火灾，洪水，风暴，爆炸，战争，政府行为，司法行政机关的命令或因第三方原因而给您造成的损害结果承担责任。");
		String main18 = new String(
				"【信息与言论】数字人仅向会员免费提供查阅、参考、讨论的平台服务，不具其他任何营利性目的，鉴于数字医生平台的内容全都系由网友提供或整理，难免会存在风险和瑕疵，平台上的文字和图片仅供合理使用，禁止作商业用途，否则视为违约。若您恶意复制、下载数字医生平台的文字和图片造成数字人及他人的合法权益遭受损失的，应承担赔偿责任。");
		String main19 = new String(
				"【美文推荐】欢迎会员推荐好的文章与观点，提供疑难杂症供会员研究探讨，数字人将及时予以刊登。文章最好是原创，若是摘录，请标明作者及出处。如果您有充分证据认为数字医生平台侵犯您的合法权益的，请及时向数字人说明并举证，数字人将及时作出删除、屏蔽等处理。");
		String main21 = new String(
				"【禁止性信息】您应当确保您所发布的信息和言论不包含以下内容：（一）违反国家法律法规禁止性规定的；（二）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；（三）欺诈、虚假、不准确或存在误导性的；（四）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；（五）侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益的；（六）存在可能破坏、篡改、删除、影响数字医生平台任何系统正常运行或未经授权秘密获取数字医生平台及其他用户的数据、个人资料的病毒、木马、爬虫等恶意软件、程序代码的；（七）内容是垃圾信息，广告宣传的；（八）其他违背社会公共利益或公共道德或依据相关数字人平台协议、规则的规定不适合在数字医生平台上发布的。");
		String main23 = new String(
				"【授权使用】对于您提供及发布除个人信息外的文字、图片、视频、音频等非个人信息，将视为在版权保护期内您免费授予数字人使用，数字人有权进行存储、使用、复制、修订、编辑、发布、展示、翻译、分发您的非个人信息或制作其派生作品，并以已知或日后开发的形式、媒体或技术将上述信息纳入其它作品内。");
		String main24 = new String(
				"【违约认定】发生如下情形之一的，视为您违约：（一）使用数字医生平台服务时违反有关法律法规规定的；（二）违反本协议或本协议补充协议约定的。");
		String main25 = new String(
				"【违约处理措施】您在数字医生平台上发布的信息或言论构成违约的，数字人可根据相应规则立即对相应信息进行删除、屏蔽处理甚至中止对您的服务。造成数字人损失的（包括自身的直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失），应赔偿数字人全部损失，数字人保留对您提起诉讼的权利。");
		String main27 = new String(
				"【商业贿赂】如您向数字人的雇员或顾问等提供实物、现金、现金等价物、劳务、旅游等价值明显超出正常商务洽谈范畴的利益，则可视为您存在商业贿赂行为。发生上述情形的，数字人可立即终止与您的所有合作并向您收取违约金及/或赔偿金，该等金额以数字人因您的贿赂行为而遭受的经济损失和商誉损失作为计算依据。");

		TextPaint tp = tv_main21.getPaint();
		tp.setFakeBoldText(true);
		
		TextPaint tp2 = tv_main24.getPaint();
		tp2.setFakeBoldText(true);
		
		SpannableStringBuilder ssb = new SpannableStringBuilder(main8); 
		ssb.setSpan(new MyStyleSpan(Typeface.NORMAL), 142, 179, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		tv_main8.setText(ssb); 
		tv_main8.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb2 = new SpannableStringBuilder(main15); 
		ssb2.setSpan(new MyStyleSpan(Typeface.NORMAL), 61, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main15.setText(ssb2);
		tv_main15.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb3 = new SpannableStringBuilder(main16); 
		ssb3.setSpan(new MyStyleSpan(Typeface.NORMAL), 36, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main16.setText(ssb3);
		tv_main16.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb4 = new SpannableStringBuilder(main17); 
		ssb4.setSpan(new MyStyleSpan(Typeface.NORMAL), 38, 150, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main17.setText(ssb4);
		tv_main17.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb5 = new SpannableStringBuilder(main18); 
		ssb5.setSpan(new MyStyleSpan(Typeface.NORMAL), 78, 156, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main18.setText(ssb5);
		tv_main18.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb6 = new SpannableStringBuilder(main19); 
		ssb6.setSpan(new MyStyleSpan(Typeface.NORMAL), 6, 122, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main19.setText(ssb6);
		tv_main19.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb7 = new SpannableStringBuilder(main23); 
		ssb7.setSpan(new MyStyleSpan(Typeface.NORMAL), 21, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb7.setSpan(new MyStyleSpan(Typeface.NORMAL), 50, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb7.setSpan(new MyStyleSpan(Typeface.NORMAL), 67, 139, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main23.setText(ssb7);
		tv_main23.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb9 = new SpannableStringBuilder(main25); 
		ssb9.setSpan(new MyStyleSpan(Typeface.NORMAL), 50, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb9.setSpan(new MyStyleSpan(Typeface.NORMAL), 59, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb9.setSpan(new MyStyleSpan(Typeface.NORMAL), 81, 120, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main25.setText(ssb9);
		tv_main25.setMovementMethod(LinkMovementMethod.getInstance());
		
		SpannableStringBuilder ssb10 = new SpannableStringBuilder(main27); 
		ssb10.setSpan(new MyStyleSpan(Typeface.NORMAL), 128, 137, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_main27.setText(ssb10);
		tv_main27.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public class MyStyleSpan extends StyleSpan {
		public MyStyleSpan(int style) {
			super(style);
		}

		@Override
		public int describeContents() {
			return super.describeContents();
		}

		@Override
		public int getSpanTypeId() {
			return super.getSpanTypeId();
		}

		@Override
		public int getStyle() {
			return super.getStyle();
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setFakeBoldText(true);
			super.updateDrawState(ds);
		}

		@Override
		public void updateMeasureState(TextPaint paint) {
			paint.setFakeBoldText(true);
			super.updateMeasureState(paint);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
		}
	}

	@OnClick(R.id.iv_back)
	public void backClick(View view) {
		instance.finish();
	}

}
