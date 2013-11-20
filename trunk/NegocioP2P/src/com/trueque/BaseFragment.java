package com.trueque;

import android.app.Activity;
import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {
	ChangeFragment chfragment = null;
	
	public interface ChangeFragment{
		public void changeFragment(int pantalla, Fragment f);
		public void changeuser(String nick);
	}

	@Override
	public void onAttach(Activity activity) {
		if (activity instanceof ChangeFragment){
			chfragment = (ChangeFragment) activity;
		}
		super.onAttach(activity);
	}
	
	public void changeScreen(int pantalla, Fragment f){
		chfragment.changeFragment(pantalla, f);
	}
	
	public void changeUser(String nick){
		chfragment.changeuser(nick);
	}
}

