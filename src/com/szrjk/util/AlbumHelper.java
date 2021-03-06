package com.szrjk.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.szrjk.entity.ImageInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;

public class AlbumHelper
{
	final String TAG = getClass().getSimpleName();
	Context context;
	ContentResolver cr;
	HashMap<String, String> thumbnailList = new HashMap<String, String>();

//	List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
	List<ImageInfo> albumList = new ArrayList<ImageInfo>();
	HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

	private static AlbumHelper instance;

	private AlbumHelper()
	{
	}

	public static AlbumHelper getHelper()
	{
		if (instance == null)
		{
			instance = new AlbumHelper();
		}
		return instance;
	}

	public void init(Context context)
	{
		if (this.context == null)
		{
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	private void getThumbnail()
	{
		String[] projection =
		{
				BaseColumns._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA
		};
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, null);
		getThumbnailColumnData(cursor);
	}

	private void getThumbnailColumnData(Cursor cur)
	{
		if (cur.moveToFirst())
		{
			int image_id;
			String image_path;
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do
			{
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				thumbnailList.put("" + image_id, image_path);
			}
			while (cur.moveToNext());
		}
	}

	void getAlbum()
	{
		String[] projection =
		{
				BaseColumns._ID, AlbumColumns.ALBUM, AlbumColumns.ALBUM_ART, AlbumColumns.ALBUM_KEY,
				AlbumColumns.ARTIST, AlbumColumns.NUMBER_OF_SONGS
		};
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
		getAlbumColumnData(cursor);

	}

	private void getAlbumColumnData(Cursor cur)
	{
		if (cur.moveToFirst())
		{
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cur.getColumnIndex(BaseColumns._ID);
			int albumColumn = cur.getColumnIndex(AlbumColumns.ALBUM);
			int albumArtColumn = cur.getColumnIndex(AlbumColumns.ALBUM_ART);
			int albumKeyColumn = cur.getColumnIndex(AlbumColumns.ALBUM_KEY);
			int artistColumn = cur.getColumnIndex(AlbumColumns.ARTIST);
			int numOfSongsColumn = cur.getColumnIndex(AlbumColumns.NUMBER_OF_SONGS);

			do
			{
				_id = cur.getInt(_idColumn);
				album = cur.getString(albumColumn);
				albumArt = cur.getString(albumArtColumn);
				albumKey = cur.getString(albumKeyColumn);
				artist = cur.getString(artistColumn);
				numOfSongs = cur.getInt(numOfSongsColumn);

//				HashMap<String, String> hash = new HashMap<String, String>();
//				hash.put("_id", _id + "");
//				hash.put("album", album);
//				hash.put("albumArt", albumArt);
//				hash.put("albumKey", albumKey);
//				hash.put("artist", artist);
//				hash.put("numOfSongs", numOfSongs + "");
				ImageInfo info = new ImageInfo();
				info.set_id(String.valueOf(_id));
				info.setAlbum(album);
				info.setAlbumArt(albumArt);
				info.setAlbumKey(albumKey);
				info.setArtist(artist);
				info.setNumOfSongs(String.valueOf(numOfSongs));
				
				albumList.add(info);

			}
			while (cur.moveToNext());
//			Collections.sort(albumList);  

		}
	}

	boolean hasBuildImagesBucketList = false;
	private ImageBucket bucket;

	public Bitmap revitionImageSize(String path) throws IOException
	{
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true)
		{
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256))
			{
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	void buildImagesBucketList()
	{

		getThumbnail();

		String columns[] = new String[]
		{
				BaseColumns._ID, ImageColumns.BUCKET_ID, ImageColumns.PICASA_ID, MediaColumns.DATA,
				MediaColumns.DISPLAY_NAME, MediaColumns.TITLE, MediaColumns.SIZE,
				ImageColumns.BUCKET_DISPLAY_NAME
		};
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				null);
		if (cur.moveToFirst())
		{
			int photoIDIndex = cur.getColumnIndexOrThrow(BaseColumns._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(MediaColumns.DATA);
			int bucketDisplayNameIndex = cur
					.getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(ImageColumns.BUCKET_ID);
			do
			{
				String _id = cur.getString(photoIDIndex);
				String path = cur.getString(photoPathIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
//				try {
//					Bitmap b = revitionImageSize(path);
//					if(b==null){
//						continue;
//					}
//				} catch (IOException e) {
//					Log.e("error",e.getMessage());
//					continue;
//				}

				bucket = bucketList.get(bucketId);
				if (bucket == null)
				{
					bucket = new ImageBucket();
					bucketList.put(bucketId, bucket);
					bucket.imageList = new ArrayList<ImageItem>();
					bucket.bucketName = bucketName;
				}
				bucket.count++;
				ImageItem imageItem = new ImageItem();
				imageItem.imageId = Integer.valueOf(_id);
				imageItem.imagePath = path;
				imageItem.thumbnailPath = thumbnailList.get(_id);
				bucket.imageList.add(imageItem);

			}
			while (cur.moveToNext());
//			Collections.sort(bucket.imageList);
//			for (int i = 0; i < bucket.imageList.size(); i++) {
//				Log.i("bucket.imageList", bucket.imageList.get(i).imageId+"");
//			}
		}

		hasBuildImagesBucketList = true;
	}

	public List<ImageBucket> getImagesBucketList(boolean refresh)
	{
		if (refresh || (!refresh && !hasBuildImagesBucketList))
		{
			buildImagesBucketList();
		}
		List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
		Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet()
				.iterator();
		while (itr.hasNext())
		{
			Map.Entry<String, ImageBucket> entry = itr
					.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	String getOriginalImagePath(String image_id)
	{
		String path = null;
		String[] projection =
		{
				BaseColumns._ID, MediaColumns.DATA
		};
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
				BaseColumns._ID + "=" + image_id, null, null);
		if (cursor != null)
		{
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA));

		}
		return path;
	}

}
