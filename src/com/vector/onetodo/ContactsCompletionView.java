package com.vector.onetodo;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

public class ContactsCompletionView extends TokenCompleteTextView {
	public ContactsCompletionView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected View getViewForObject(java.lang.Object object) {
		
		Person p = (Person) object;

		LayoutInflater l = (LayoutInflater) getContext().getSystemService(
				Activity.LAYOUT_INFLATER_SERVICE);
			LinearLayout view = (LinearLayout) l.inflate(R.layout.contact_token,
					(ViewGroup) ContactsCompletionView.this.getParent(), false);
			((TextView) view.findViewById(R.id.name)).setText(p.getEmail());
		
		return view;
	}

	@Override
	protected Person defaultObject(String completionText) {
		// Stupid simple example of guessing if we have an email or not
		int index = completionText.indexOf('@');
		if (index == -1) {
			return new Person(completionText, completionText.replace(" ", "")
					+ "@example.com");
		} else {
			return new Person(completionText.substring(0, index),
					completionText);
		}
	}

}
