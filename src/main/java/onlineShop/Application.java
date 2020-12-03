package onlineShop;

import java.math.BigDecimal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import onlineShop.log.PaymentAction;

public class Application {
    public static void main(String[] args) {// context �������Ϊcontainer; ���online shop�����classʵ������bean;
        ApplicationContext context = new ClassPathXmlApplicationContext("payment.xml");// ����classPathXmlApplicationContext��springָ���Ĺ��캯��
        //���Ĺ��ܾ���payment.xml�涨��һ��ɨ�跶Χ��������onlineShop���package���棩��Ȼ��container�������Χ����ȥɨ��bean(class);
        PaymentAction paymentAction = (PaymentAction) context.getBean("paymentAction");
        paymentAction.pay(new BigDecimal(2)); 
     }
}

