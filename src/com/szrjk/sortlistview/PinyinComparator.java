package com.szrjk.sortlistview;

import java.util.Comparator;

import com.szrjk.entity.LibraryEntity;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<LibraryEntity> {

	public int compare(LibraryEntity o1, LibraryEntity o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
