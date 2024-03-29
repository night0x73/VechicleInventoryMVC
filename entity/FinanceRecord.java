package com.vehicleinventory.entity;
import java.math.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="FinanceRecords")
public class FinanceRecord{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="financeId")
	private int financeId;
	
	@NotNull(message="cannot be blank")
	@Min(value=300, message="must be more than 299")
	@Max(value=850, message="must be less than 851")
	@Column(name="creditScore")
	private int creditScore;
	
	@NotNull(message="cannot be blank")
	@Size(min=17, max=17, message="must be 17 Characters")
	@Column(name="vehicleIdNumber")
	private String vehicleIdNumber;
	
	@NotNull(message="cannot be blank")
	@Column(name="vehiclePrice")
	@Min(value=0, message="must be at least $0.00")
	@Max(value=10000000, message="must be less than $10,000,000")
	private double vehiclePrice;
	
	@NotNull(message="cannot be blank")
	@Column(name="termLength")
	@Max(value=84, message="must be less than 85 months")
	private int termLength;
	
	@NotNull(message="cannot be blank")
	@Column(name="paymentsMade")
	private int installmentsPaid;
	
	@NotNull(message="cannot be blank")
	@Column(name="apr")
	private double apr;
	
	@NotNull(message="cannot be blank")
	@Column(name="downPayment")
	private double downPayment;
	
	@NotNull(message="cannot be blank")
	@Column(name="balance")
	@Min(value=0, message="must be at least $0.00")
	private double balance;
	
	@NotNull(message="cannot be blank")
	@Column(name="monthlyPayment")
	@Min(value=0, message="must be at least $0.00")
	private double monthlyPaymentAmount;
	
	@NotNull(message="cannot be blank")
	@Column(name="currCondition")
	private String condition;
	
	@Column(name="paidOff")
	private boolean paidOff;
	
	// Hibernate mappings --------------------------------------------- >
	// mappedBy enables a bi-directional relationship, the property name is the name of the field in the Vehicle Class
	@OneToOne(mappedBy="financeRecord", fetch = FetchType.LAZY)
	private Vehicle vehicle;
	
  	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable=false)
    private CustomerAccount customerAccount;

	// ----------------------------------------------------------------------------------- >
	// Constructors
	
	public FinanceRecord() {}
	
	public FinanceRecord(int financeId){
 		this.financeId = financeId;
	}

	public FinanceRecord(int financeId, int customerId, String vehicleIdNumber){
		this.financeId = financeId;
		this.vehicleIdNumber = vehicleIdNumber;
	}

	
	public FinanceRecord(int financeId, int creditScore, String vehicleIdNumber, int termLength, double apr, double vehiclePrice, 
			String condition, double downPayment, int installmentsPaid){
		
		this.financeId = financeId;
		this.creditScore = creditScore;
		this.vehicleIdNumber = vehicleIdNumber;
		
		this.termLength = termLength;
		this.condition = condition;
		
		if(condition.equals("NEW")) {
			this.apr = calcPreownedInterestRate(creditScore);
		}else {
			this.apr = calcPristineInterestRate(creditScore);
		}
		
		this.installmentsPaid = installmentsPaid;
		this.vehiclePrice = priceFormat(vehicle.getPrice());
		this.downPayment = priceFormat(downPayment);
		
		// loan amount is the amount the customer has to borrow
		double loanAmount = vehiclePrice - downPayment;
		double monthlyPayment = calcMonthlyPayments(loanAmount, this.apr, termLength);
		this.monthlyPaymentAmount = monthlyPayment;
		
		// this is calculating and setting the total amount the customer will pay with interest
		double outstandingBalance = termLength * monthlyPayment;
		this.balance = outstandingBalance - (installmentsPaid * monthlyPaymentAmount);
		
		this.paidOff = allInstallmentsPaid();
	}

	// ------------------------------------------------- 
	// calculation methods:

	public double calcPreownedInterestRate(int score){
		double[] preownedInterestRates = {20.5, 17.75, 11.25, 6.0, 4.5};
		
		if(score <= 579){
			return preownedInterestRates[0];
		}else if(score > 579 && score <= 619){
			return preownedInterestRates[1];
		}else if(score > 619 && score <= 659){
			return preownedInterestRates[2];
		}else if(score > 659 && score <= 719){
			return preownedInterestRates[3];
		}else {
			return preownedInterestRates[4];
		}
	}

	public double calcPristineInterestRate(int score){
		double[] pristineInterestRates = {14.5, 12.0, 7.5, 4.7, 3.7};
		
		if(score <= 579){
			return pristineInterestRates[0];
		}else if(score > 579 && score <= 619){
			return pristineInterestRates[1];
		}else if(score > 619 && score <= 659){
			return pristineInterestRates[2];
		}else if(score > 659 && score <= 719){
			return pristineInterestRates[3];
		}else {
			return pristineInterestRates[4];
		}
	}

	// calculating amortized loan
	public static double calcMonthlyPayments(double principal, double apr, int term){
		// dividing by 100 to convert to decimal percentage, then by 12 to get the monthly decimal percentage
		// therefore dividing by (100 * 12)
		apr /= 1200;
		double monthPayments = principal * ((apr * Math.pow((1 + apr), term))/(((Math.pow(1 + apr, term)) - 1)));
		return monthPayments;
	}
	
	// making a payment
	public void makePayment(){
		balance -= monthlyPaymentAmount; 
		installmentsPaid++;
		paidOff = (balance <= 0.00);
	}
	
	public boolean allInstallmentsPaid() {
		return (installmentsPaid == termLength);
	}
	
	// ----------------------------------------------------------------------------------- >
	// Getters/Setters:
	
	public static double priceFormat(Double price) {
	    BigDecimal bdPrice = BigDecimal.valueOf(price);
	    return bdPrice.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public int getFinanceId() {
		return financeId;
	}

	public void setFinanceId(int financeId) {
		this.financeId = financeId;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public String getVehicleIdNumber() {
		return vehicleIdNumber;
	}

	public void setVehicleIdNumber(String vehicleIdNumber) {
		this.vehicleIdNumber = vehicleIdNumber;
	}

	public double getVehiclePrice() {
		return vehiclePrice;
	}

	public void setVehiclePrice(double vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}

	public int getTermLength() {
		return termLength;
	}

	public void setTermLength(int termLength) {
		this.termLength = termLength;
	}

	public CustomerAccount getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(CustomerAccount customerAccount) {
		this.customerAccount = customerAccount;
	}

	public int getInstallmentsPaid() {
		return installmentsPaid;
	}

	public void setInstallmentsPaid(int installmentsPaid) {
		this.installmentsPaid = installmentsPaid;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public double getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(double downPayment) {
		this.downPayment = downPayment;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getMonthlyPaymentAmount() {
		return monthlyPaymentAmount;
	}

	public void setMonthlyPaymentAmount(double monthlyPaymentAmount) {
		this.monthlyPaymentAmount = monthlyPaymentAmount;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public boolean isPaidOff() {
		return paidOff;
	}

	public void setPaidOff(boolean paidOff) {
		this.paidOff = paidOff;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


}
	
	