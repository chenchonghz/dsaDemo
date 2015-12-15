package com.szrjk.config;

import java.util.ArrayList;
import java.util.List;

import com.szrjk.util.ImageItem;

public class PhotoConstant
{
	//发普通帖子
	public static List<ImageItem> postItems = new ArrayList<ImageItem>();
	public static int postCount;
	//发病例分析
	public static List<ImageItem> caseItems = new ArrayList<ImageItem>();
	public static List<ImageItem> checkItems = new ArrayList<ImageItem>();
	public static List<ImageItem> treatItems = new ArrayList<ImageItem>();
	public static List<ImageItem> visitItems = new ArrayList<ImageItem>();
	public static int caseCount;
	public static int checkCount;
	public static int treatCount;
	public static int visitCount;
	//疑难求助
	public static List<ImageItem> puzzleCases = new ArrayList<ImageItem>();
	public static List<ImageItem> puzzleChecks = new ArrayList<ImageItem>();
	public static List<ImageItem> puzzletreat = new ArrayList<ImageItem>();
	public static int puzzleCaseCount;
	public static int puzzleCheckCount;
	public static int puzzletreatCount;
}
