package com.szrjk.sortlistview;

import java.util.Comparator;

import com.szrjk.entity.AddressListEntity;
import com.szrjk.entity.LibraryEntity;

/**
 * 
 * @author xiaanming
 *
 */
public class AddressBookPinyinComparator implements Comparator<AddressListEntity> {

	@Override
	public int compare(AddressListEntity o1, AddressListEntity o2) {
		if (o1.getUserCard().getSortLetters().equals("@")
				|| o2.getUserCard().getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getUserCard().getSortLetters().equals("#")
				|| o2.getUserCard().getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getUserCard().getSortLetters().compareTo(o2.getUserCard().getSortLetters());
		}
	}

}
