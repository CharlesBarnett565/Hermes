package onlineShop.log;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentAction {
	@Autowired  // 这里就表示要inject 一个Logger
	private Logger logger;// 把logger的dependency inject到paymentAction里面

	public void pay(BigDecimal payValue) {
		logger.log("pay begin, payValue is " + payValue);
		logger.log("pay end");
	}
}

