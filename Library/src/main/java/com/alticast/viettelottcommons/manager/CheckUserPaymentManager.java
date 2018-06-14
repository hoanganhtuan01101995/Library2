package com.alticast.viettelottcommons.manager;


import com.alticast.viettelottcommons.resource.response.PaidAmountRes;

public class CheckUserPaymentManager {

	private static CheckUserPaymentManager instance = null;
	public CheckUserPaymentManager() {
	}

	public synchronized static CheckUserPaymentManager getInstance() {
		if (instance == null) {
			instance = new CheckUserPaymentManager();
		}
		return instance;
	}

	public void checkPaymentWarning(PaidAmountRes payment, paymentCheckListener callback) {
		if (payment == null) {
			return;
		}

		int limit = payment.getLimitValue();
		float totalValue = payment.getPriceValue(PaidAmountRes.TYPE_TOTAL);
		float dcValue = payment.getPriceValue(PaidAmountRes.TYPE_DISCOUNT);
		if (callback == null) {
			return;
		}
		boolean isAvailable = false;
		if (limit < totalValue - dcValue) {
			isAvailable = false;
		} else {
			isAvailable = true;
		}

		callback.paymentAvailable(isAvailable,limit);
	}

	public void checkAvailablePoint(float point, pointAvailableListener callback) {

		if (callback == null) {
			return;
		}
		boolean isAvailable = false;
		if (point > 0) {
			isAvailable = false;
		} else {
			isAvailable = true;
		}
		callback.isAvailable(isAvailable);
	}

	public interface paymentCheckListener {
		public void paymentAvailable(boolean available, float value);
	}

	public interface pointAvailableListener {
		public void isAvailable(boolean available);
	}
	

}
