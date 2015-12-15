package com.szrjk.fire;

import android.widget.TextView;

public interface MessageDisplay
{
	/**
	 * Dismiss the setMessage
	 * 
	 * @param field
	 *            Target view.
	 */
	void dismiss(TextView field);

	/**
	 * Show the setMessage
	 * 
	 * @param field
	 *            Target view.
	 * @param message
	 *            Message to show.
	 */
	void show(TextView field, String message);
}
