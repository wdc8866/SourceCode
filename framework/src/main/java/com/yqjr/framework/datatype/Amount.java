package com.yqjr.framework.datatype;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.util.Assert;

import com.yqjr.framework.utils.CommonUtils;

public class Amount implements Serializable {

	private static final long serialVersionUID = 5437365515967117292L;

	private BigDecimal amount;

	private Currency currency;

	public static enum Currency {

		CNY(1), USD(2), EUR(3), JPY(25);

		private int code;

		private Currency(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		/**
		 * Description: 设置币种 <br>
		 * Create By: admin <br>
		 * Create Data: 2017年2月27日 下午4:07:41
		 *
		 * @param code
		 * @return
		 */
		public static Currency setCode(int code) {
			switch (code) {
			case 1:
				return CNY;
			case 2:
				return USD;
			case 3:
				return EUR;
			case 25:
				return JPY;
			default:
				throw new BizzException("不支持的币种");
			}
		}

		/**
		 * 金额信息
		 */
		public String toString() {
			switch (this.code) {
			case 1:
				return "CNY";
			case 2:
				return "USD";
			case 3:
				return "EUR";
			case 25:
				return "JPY";
			default:
				throw new BizzException("不支持的币种");
			}
		}
	}

	public Amount(BigDecimal amount, Integer currencyCode) {
		this(amount, Currency.setCode(currencyCode));
	}

	public Amount() {
		this(BigDecimal.ZERO, Currency.CNY);
	}

	public Amount(Currency currency) {
		this(BigDecimal.ZERO, currency);
	}

	public Amount(BigDecimal amount) {
		this(amount, Currency.CNY);
	}
	
	public Amount(String amount) {
		this(new BigDecimal(amount.replaceAll(",","")));
	}

	public Amount(BigDecimal amount, Currency currency) {
		this.amount = (amount == null ? BigDecimal.ZERO : amount).setScale(10, RoundingMode.HALF_UP);
		this.currency = currency;
	}

	public Amount(Double amount) {
		this(amount, Currency.CNY);
	}

	public Amount(Double amount, Currency currency) {
		this(new BigDecimal(String.valueOf(amount)).setScale(10, RoundingMode.HALF_UP), currency);
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 输出金额
	 */
	public String toString() {
		return CommonUtils.formatMoney(amount);
	}

	/**
	 * Description: 加法运算 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:30:22
	 *
	 * @param amounts
	 * @return
	 */
	public Amount add(Amount... amounts) {
		BigDecimal tmpAmount = this.amount;
		for (Amount amount : amounts) {
			// 如果是同币种才可以进行运算
			if (isSameCurrency(amount)) {
				tmpAmount = tmpAmount.add(amount.getAmount());
			} else {
				throw new BizzException("只有相同币种方可支持运算");
			}
		}
		return new Amount(tmpAmount, this.currency);
	}

	/**
	 * Description: 减法运算 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:37:23
	 *
	 * @param amounts
	 * @return
	 */
	public Amount subtract(Amount... amounts) {
		BigDecimal tmpAmount = this.amount;
		for (Amount amount : amounts) {
			// 如果是同币种才可以进行运算
			if (isSameCurrency(amount)) {
				tmpAmount = tmpAmount.subtract(amount.getAmount());
			} else {
				throw new BizzException("只有相同币种方可支持运算");
			}
		}
		return new Amount(tmpAmount, this.currency);
	}

	/**
	 * Description: 乘法运算 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:42:57
	 *
	 * @param amounts
	 * @return
	 */
	public Amount multiply(BigDecimal multiplicand) {
		return new Amount(this.amount.multiply(multiplicand), this.currency);
	}

	/**
	 * Description: 除法运算 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:45:01
	 *
	 * @return
	 */
	public Amount divide(BigDecimal dividend) {
		return new Amount(this.amount.doubleValue() / dividend.doubleValue(), this.currency);
	}

	/**
	 * Description: 金额比较大小 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:49:49
	 *
	 * @param amount
	 * @return
	 */
	public int compareTo(Amount amount) {
		if (isSameCurrency(amount)) {
			return this.amount.compareTo(amount.getAmount());
		} else {
			throw new BizzException("不同币种无法比较");
		}
	}

	/**
	 * Description: 大于 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:51:35
	 *
	 * @param amount
	 * @return
	 */
	public boolean greaterThan(Amount amount) {
		if (compareTo(amount) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Description: 小于 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:53:16
	 *
	 * @param amount
	 * @return
	 */
	public boolean lessThan(Amount amount) {
		if (compareTo(amount) == -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Description: 大于等于 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:54:22
	 *
	 * @param mount
	 * @return
	 */
	public boolean greaterThanOrEqual(Amount amount) {
		return !lessThan(amount);
	}

	/**
	 * Description: 小于等于 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:55:18
	 *
	 * @param amount
	 * @return
	 */
	public boolean lessThanOrEqual(Amount amount) {
		return !greaterThan(amount);
	}

	/**
	 * Description: 判断是否为相同币种 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年2月27日 下午2:27:41
	 *
	 * @param amount
	 * @return
	 */
	private boolean isSameCurrency(Amount amount) {
		Assert.notNull(amount);
		return this.currency == amount.getCurrency();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Amount))
			return false;
		final Amount other = (Amount) obj;
		return (this.currency == other.getCurrency() && (this.amount.compareTo(other.getAmount()) == 0));
	}

}
