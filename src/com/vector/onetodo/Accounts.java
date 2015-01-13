package com.vector.onetodo;

import java.io.File;

import uk.me.lewisdeane.ldialogs.BaseDialog.Alignment;
import uk.me.lewisdeane.ldialogs.CustomDialog;
import uk.me.lewisdeane.ldialogs.CustomListDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog.ClickListener;
import uk.me.lewisdeane.ldialogs.CustomListDialog.ListClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.vector.onetodo.utils.Constants;
import com.vector.onetodo.utils.TypeFaces;
import com.vector.onetodo.utils.Utils;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class Accounts extends Fragment {

	AQuery aq,aq_attach,aq_onetodoinfo,aq_buypro,aq_phone,aq_changephone,aq_email,aq_changeemail,aq_name;
	private Uri imageUri;
	File photo;
	private static final int TAKE_PICTURE = 1;
	public static final int RESULT_GALLERY = 0;
	CircularImageView imageEvent;
	Dialog onetodoinfo,buypro,onetodopro,phoneinfo,changephone,emailinfo,changeemail,
	nameinfo,select_image;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.account, container, false);
		imageEvent = (CircularImageView) view.findViewById(R.id.image_event);
		aq = new AQuery(getActivity(), view);
		setFont();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		aq.id(R.id.account_back).clicked(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialoglayout6 = inflater.inflate(R.layout.account_dialog_attachfrom_gallery_camera, null,
				false);
		aq_attach = new AQuery(dialoglayout6);
		AlertDialog.Builder builder6 = new AlertDialog.Builder(getActivity());
		builder6.setView(dialoglayout6);
		select_image = builder6.create();

		View dialoglayout7 = inflater.inflate(R.layout.account_dialog_getonetodo,
				null, false);
		aq_onetodoinfo = new AQuery(dialoglayout7);
		AlertDialog.Builder builder7 = new AlertDialog.Builder(getActivity());
		builder7.setView(dialoglayout7);
		onetodoinfo = builder7.create();
		
		View dialoglayout8 = inflater.inflate(R.layout.account_dialog_phoneno,
				null, false);
		aq_phone = new AQuery(dialoglayout8);
		AlertDialog.Builder builder8 = new AlertDialog.Builder(getActivity());
		builder8.setView(dialoglayout8);
		phoneinfo = builder8.create();
		
		View dialoglayout9 = inflater.inflate(R.layout.account_dialog_email,
				null, false);
		aq_email = new AQuery(dialoglayout9);
		AlertDialog.Builder builder9 = new AlertDialog.Builder(getActivity());
		builder9.setView(dialoglayout9);
		emailinfo = builder9.create();
		View dialoglayout1 = inflater.inflate(R.layout.account_dialog_changename,
				null, false);
		aq_name = new AQuery(dialoglayout1);
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
		builder1.setView(dialoglayout1);
		nameinfo = builder1.create();
		
		View dialoglayout2 = inflater.inflate(R.layout.account_dialog_getbuypro,
				null, false);
		aq_buypro = new AQuery(dialoglayout2);
		AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
		builder2.setView(dialoglayout2);
		buypro = builder2.create();
		
		View dialoglayout3 = inflater.inflate(R.layout.account_dialog_changeemail,
				null, false);
		aq_changeemail = new AQuery(dialoglayout3);
		AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
		builder3.setView(dialoglayout3);
		changeemail = builder3.create();
		
		View dialoglayout4 = inflater.inflate(R.layout.account_dialog_change_country_phoneno,
				null, false);
		aq_changephone = new AQuery(dialoglayout4);
		AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
		builder4.setView(dialoglayout4);
		changephone = builder4.create();
		
		aq_onetodoinfo.id(R.id.ok_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onetodoinfo.dismiss();
				buypro.show();				
			}
		});
		
		aq_onetodoinfo.id(R.id.cancel_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onetodoinfo.dismiss();
			}
		});
		aq.id(R.id.image_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				select_image.show();
			}
		});
		aq_attach.id(R.id.from_camera).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

							select_image.dismiss();
							Intent intent = new Intent(
									"android.media.action.IMAGE_CAPTURE");

							String path = Environment.getExternalStorageDirectory()
									.toString();
							File makeDirectory = new File(path + File.separator
									+ "OneTodo");
							makeDirectory.mkdir();
							photo = new File(Environment
									.getExternalStorageDirectory()
									+ File.separator
									+ "OneToDo" + File.separator, "OneToDo_"
									+ System.currentTimeMillis() + ".JPG");
							intent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(photo));
							imageUri = Uri.fromFile(photo);
							startActivityForResult(intent, TAKE_PICTURE);			
			}
		});
		aq_attach.id(R.id.from_gallery).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					select_image.dismiss();
					Intent galleryIntent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(galleryIntent, RESULT_GALLERY);
			}
		});
		aq.id(R.id.timeformat_lay).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onetodoinfo.show();
			}
	});
		aq.id(R.id.phone_layout).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phoneinfo.show();
			}
		});
		aq_phone.id(R.id.ok_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phoneinfo.dismiss();
				
			}
		});
		aq_phone.id(R.id.cancel_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phoneinfo.dismiss();
				changephone.show();
			}
		});
		aq.id(R.id.dateformat_lay).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailinfo.show();
			}
		});
		aq_email.id(R.id.ok_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailinfo.dismiss();
				
			}
		});
		aq_email.id(R.id.cancel_event).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailinfo.dismiss();
				changeemail.show();
			}
		});
		aq.id(R.id.sound_lay).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameinfo.show();
			}
		});
		aq_name.id(R.id.ok_event).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameinfo.dismiss();
			}
		});
		aq_name.id(R.id.cancel_event).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameinfo.dismiss();
			}
		});
		aq_changeemail.id(R.id.ok_event).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeemail.dismiss();
			}
		});
		aq_changeemail.id(R.id.cancel_event).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeemail.dismiss();
			}
		});
		aq_changephone.id(R.id.ok_event).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changephone.dismiss();
			}
		});
		aq_changephone.id(R.id.cancel_event).clicked(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changephone.dismiss();
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case TAKE_PICTURE:

			if (resultCode == Activity.RESULT_OK){
				Picasso.with(getActivity()).load(imageUri).resize(100, 100)
				.into(imageEvent);
				aq.id(R.id.image_event).background(R.drawable.round_photobutton);
			}
		case RESULT_GALLERY:
			if (null != data) {
				Uri selectedImage = data.getData();
				Picasso.with(getActivity()).load(selectedImage).resize(100, 100)
				.into(imageEvent);
				aq.id(R.id.image_event).background(R.drawable.round_photobutton);
			}
			break;
		default:
			break;
		}
	}
	public void setFont() {
		Utils.RobotoRegular(getActivity(), aq.id(R.id.account).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.accounts).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.email).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.onetodo).getTextView());
		
		Utils.RobotoRegular(getActivity(), aq.id(R.id.mynumber).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.personal).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.name).getTextView());
		Utils.RobotoRegular(getActivity(), aq.id(R.id.display).getTextView());

		Utils.RobotoMedium(getActivity(), aq.id(R.id.email1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.onetodo1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.mynumber1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.name1).getTextView());
		Utils.RobotoMedium(getActivity(), aq.id(R.id.display1).getTextView());
	}

}
