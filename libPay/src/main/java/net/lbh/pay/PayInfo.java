
package net.lbh.pay;

import java.io.Serializable;

public final class PayInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** 商品名称*/
    private String subject;

    /** 商品详细信息  商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字*/
    private String body;

    /** 商品价格*/
    private String price;

    /** 商品订单号*/
    private String orderNo;
    
    /** 支付通知地址*/
    private String notifyUrl;
    

    public String getBody() {
        return body;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getPrice() {
        return price;
    }

    public String getSubject() {
        return subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

	/**
	 * @return the notifyUrl
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * @param notifyUrl the notifyUrl to set
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
    
    
}
