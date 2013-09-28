package com.example.socialctxt_alarm;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BluetoothViewAdaptor extends BaseAdapter {

	private Context ctxt;

	public ArrayList<String> names = new ArrayList<String>();
	public ArrayList<String> phn_num = new ArrayList<String>();

	public BluetoothViewAdaptor(Context c) {
		// TODO put code here for getting contact's names from the
		// contentProvider and storing them in the list

		ctxt = c;
		int cnt =0;
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] 
        {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                "1" + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = ctxt.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        
	    if (cursor != null) {
	        while (cursor.moveToNext()) {
	        	String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
	        	ContentResolver cr = c.getContentResolver();
	        	Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  null,
	                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID
	                               + " = ?", new String[] { id }, null);
	               while (pCur.moveToNext()) {
	                   String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                   	names.add(cnt, cursor.getString(cursor.getColumnIndex("display_name")));
	                   	phn_num.add(cnt, phone);
	                   	cnt++;
	               }
	        }
	    }
	}

	
	
	public int getCount() {
		return names.size();
	}

	public Object getItem(int position) {
		return names.get(position);
	}

	public long getItemId(int position) {
		return position;
	}



	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		TextView textView = new TextView(ctxt);
		textView.setText(names.get(position));
		textView.setTextSize(20);
		textView.setTypeface(null,Typeface.BOLD);
		textView.setTextColor(-16777216);
		return textView;
	}
}