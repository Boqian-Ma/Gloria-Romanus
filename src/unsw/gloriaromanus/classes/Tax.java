package unsw.gloriaromanus.classes;

public class Tax implements TaxBehaviour {
    private double taxRate;
    private double wealthRate;

    public Tax(double taxRate, double wealthRate) {
        this.taxRate = taxRate;
        this.wealthRate = wealthRate;
    }
    @Override
    public double getTaxRate() {
        return taxRate;
    }

    public double getWealthRate() {
        return wealthRate;
    }
    @Override
    public double CalculateTax(double wealth) {
        return wealth * this.taxRate;
    }
    @Override
    public double getWealth() {
        //System.out.println("inside wealth and wealth rate is = " + this.wealthRate);
        return wealthRate;
    }
}
