package onlineShop;

import java.math.BigDecimal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import onlineShop.log.PaymentAction;

public class Application {
    public static void main(String[] args) {// context 可以理解为container; 会把online shop里面的class实例化成bean;
        ApplicationContext context = new ClassPathXmlApplicationContext("payment.xml");// 这里classPathXmlApplicationContext是spring指定的构造函数
        //它的功能就是payment.xml规定了一个扫描范围（这里是onlineShop这个package下面），然后container在这个范围里面去扫描bean(class);
        PaymentAction paymentAction = (PaymentAction) context.getBean("paymentAction");
        paymentAction.pay(new BigDecimal(2)); 
     }
}

