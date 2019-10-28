package com.lels.student.main.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.strudentlelts.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class User_message extends Fragment {
	private List<String> mList;
	private ListView mListview;
	private String[] strs = { "�ҵ�Ŀ��", "�ҵ�Ŀ��", "�ҵ�Ŀ��", "�ҵ�Ŀ��", "�ҵ�Ŀ��", "�ҵ�Ŀ��" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.user_message, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		
	}

	private void initView() {
		mListview=(ListView) getActivity().findViewById(R.id.user_message_listview);
		mList=new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			mList.add(strs[i]);
		}
	}

}
