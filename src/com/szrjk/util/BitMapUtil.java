package com.szrjk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitMapUtil
{
	private static final String TAG = BitMapUtil.class.getName();

	// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
	public static final float DefaultH = 800f; // 目标高度为800f
	public static final float DefaultW = 480f; // 目标宽度为480f
	public static final int SaveSizeInK = 1024; // 安全尺寸为1M,避免在生成图片（BitmapFactory.decodeStream）时溢出
	public static final int DefaultSizeInK = 100; // 目标尺寸为100K

	public static ByteArrayOutputStream compressImage(Bitmap image,
			int maxSizeInK)
	{

		long time = System.currentTimeMillis();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 85, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		Log.d(TAG, "image.compress压缩耗时：" + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();

		int options = 100;
		int curSizeInK = baos.toByteArray().length / 1024;
		Log.d(TAG, "原始尺寸（K）：" + curSizeInK);
		if ((curSizeInK < maxSizeInK)) { return baos; }
		options = (int) (options * Math.sqrt((double) maxSizeInK / curSizeInK)); // 若原图为目标的四倍大，则压缩比为50%
		options = Math.min(90, options); // 起步压缩比为90%

		int i = 1;
		while (curSizeInK > maxSizeInK)
		{ // 循环判断如果压缩后图片是否大于最大尺寸,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			curSizeInK = baos.toByteArray().length / 1024;
			Log.i(TAG, "第" + (i++) + "次压缩，压缩比为：" + options + "，得到尺寸："
					+ curSizeInK + "，累计耗时："
					+ (System.currentTimeMillis() - time));
			if (options > 20)
			{
				options -= 10;// 每次都减少10
			}
			else
			{
				options -= 5;
			}
			if (options <= 0) break;
		}
		return baos;
	}

	public static ByteArrayOutputStream comp(Bitmap image)
	{

		long time = System.currentTimeMillis();

		// 利用质量压缩，将图片尺寸控制在安全范围（1M）以内
		ByteArrayOutputStream baos = compressImage(image, SaveSizeInK);
		if (baos == null)
		{
			// 图片太大，质量压缩失败
			return null;
		}
		Log.i(TAG, "首次压缩耗时：" + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();

		// 检查图片尺寸
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(isBm, null, newOpts);

		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		double be = 0.0;// be=1表示不缩放
		if (w > h && w > DefaultH)
		{// 如果宽度大的话，为横拍图片，根据宽度固定大小缩放
			be = (double) w / DefaultH;
		}
		else if (w < h && h > DefaultH)
		{// 如果高度高的话根据宽度固定大小缩放
			be = (double) h / DefaultH;
		}
		if (be >= 1.5)
		{
			newOpts.inSampleSize = (int) be;// 设置缩放比例
		}
		else
		{
			newOpts.inSampleSize = 1;
		}
		isBm = new ByteArrayInputStream(baos.toByteArray());
		newOpts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		Log.d(TAG, "缩放耗时：" + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();

		ByteArrayOutputStream btOut = compressImage(bitmap, DefaultSizeInK);// 压缩好比例大小后再进行质量压缩
		Log.d(TAG, "二次压缩耗时：" + (System.currentTimeMillis() - time));

		return btOut;
	}
}
