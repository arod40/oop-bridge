package product.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import product.Product;

public class EbayProduct extends Product {

	private static final Map<String, Double> productRank;
    
	static {
        // hack for the illustration
    	Map<String, Double> temporary = new HashMap<>();
        temporary.put("Soap", 2.4);
        temporary.put("Lego", 5.0);
        temporary.put("Tabaco", 4.1);
        temporary.put("Book", 2.1);
        productRank = Collections.unmodifiableMap(temporary);
    }
	
	private double rank = 0.0;
	
	public EbayProduct(String name, String describtion, BigDecimal price, double rank) {
		super(name, describtion, price);
		this.rank = rank;
	}
	
	public EbayProduct(String name) {
		super(name);
		if (productRank.containsKey(name)) {
			rank = productRank.get(name);
		}
	}
	
	public EbayProduct() {
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public static EbayProduct make(String name) {
		return new EbayProduct(name);
	}
	public EbayProduct init(String describtion, BigDecimal price, double rank) {
		super.init(describtion, price);
		this.rank = rank;
		return this;
	}
}
