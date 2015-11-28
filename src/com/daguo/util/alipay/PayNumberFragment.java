package com.daguo.util.alipay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.operators.MobileAty;
import com.daguo.ui.operators.MoblieOrderAty;

public class PayNumberFragment extends Fragment{
	
	private TextView priceButton, nameButton, detailButton;
	String name = MoblieOrderAty.num_name, detai = "1",
			price = MobileAty.num_price;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pay_goodsfragment, container, false);
		priceButton = (TextView) view.findViewById(R.id.product_price);
		nameButton = (TextView) view.findViewById(R.id.product_subject);
		detailButton = (TextView) view.findViewById(R.id.product_info);
		priceButton.setText(price);
		nameButton.setText(name);
		detailButton.setText(detai);
		return view;

	}

}
