package product.impl;

import product.Product;

import java.math.BigDecimal;

public class AmazonProduct  extends Product {
  public AmazonProduct(String name) {
    super(name);
  }
  public static AmazonProduct make(String name) {
    return new AmazonProduct(name);
  }

  public AmazonProduct init(String describtion, BigDecimal price) {
    super.init(describtion, price);
    this.setPrice(price.multiply(new BigDecimal("0.9")));
    return this;
  }
}
