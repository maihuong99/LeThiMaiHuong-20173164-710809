package shippingfees;

import entity.order.Order;
import entity.order.OrderMedia;
import utils.Configs;
import java.util.Random;

public class CalculateNewShippingFees implements ShippingFeeCalculator {

	@Override
	public int calculateShippingFees(Order order) {
		Random rand = new Random();
		
		order.setWeightOrder(1);
		
    	int alterWeight = 0;
    	for (int i = 0; i < order.getlstOrderMedia().size(); i++) {
    		OrderMedia ordMedia = (OrderMedia) order.getlstOrderMedia().get(i);
    		int iLength, iWidth, iWeight;
    		
    		iLength = ordMedia.getMedia().getLength();
    		iWeight = ordMedia.getMedia().getWeight();
    		iWidth = ordMedia.getMedia().getWidth();
    	
    		alterWeight += iLength * iWeight * iWidth / 6000; // CONSTANTS = 6000
    	}
		return (int) ((order.getWeightOrder()+alterWeight)*(rand.nextFloat()/10));
	}

}