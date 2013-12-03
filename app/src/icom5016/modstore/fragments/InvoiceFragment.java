package icom5016.modstore.fragments;

import icom5016.modstore.activities.MainActivity;
import icom5016.modstore.activities.R;
import icom5016.modstore.resources.ConstantClass;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InvoiceFragment extends Fragment implements View.OnClickListener{

	private Button btn;
	private MainActivity ma;
	private int id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cart_invoice, container, false);
		this.btn = (Button) view.findViewById(R.id.btnViewDetails);
		this.btn.setOnClickListener(this);
		this.ma = (MainActivity) this.getActivity();
		id = 0;
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnViewDetails:
			Bundle bundle = new Bundle();
			bundle.putInt(ConstantClass.ORDERID_KEY, id);
			MyOrderDetailsListFragment modlf = new MyOrderDetailsListFragment();
			modlf.setArguments(bundle);
			this.ma.fragmentStack.pop();
			this.ma.loadFragmentInMainActivityStack(MainActivity.getContainerId(), modlf);
			break;

		default:
			break;
		}
	}
}
