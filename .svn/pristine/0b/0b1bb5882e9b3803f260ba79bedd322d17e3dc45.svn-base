package com.daguo.util.alipay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.OrderChart;

public class ExternalFragment extends Fragment {
	private TextView priceButton, nameButton, detailButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pay_external, container, false);
		OrderChart oc = (OrderChart) getArguments()
				.getSerializable("orderInfo");
		priceButton = (TextView) view.findViewById(R.id.product_price);
		nameButton = (TextView) view.findViewById(R.id.product_subject);
		detailButton = (TextView) view.findViewById(R.id.product_info);
		priceButton.setText(oc.getPrice() + " 元");
		nameButton.setText(oc.getName());
		detailButton.setText(oc.getDetail());
		return view;

	}
}
