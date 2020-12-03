package onlineShop.log;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentAction {
	@Autowired  // ����ͱ�ʾҪinject һ��Logger
	private Logger logger;// ��logger��dependency inject��paymentAction����

	public void pay(BigDecimal payValue) {
		logger.log("pay begin, payValue is " + payValue);
		logger.log("pay end");
	}
}

