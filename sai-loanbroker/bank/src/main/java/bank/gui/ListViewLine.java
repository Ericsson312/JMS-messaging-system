package bank.gui;


import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;

/**
 * This class is an item/line for a ListViewLine. It makes it possible to put both BankInterestRequest and BankInterestReply object in one item in a ListViewLine.
 */
class ListViewLine {
	
	private BankInterestRequest bankInterestRequest;
	private BankInterestReply bankInterestReply;
	
	public ListViewLine(BankInterestRequest bankInterestRequest) {
		setBankInterestRequest(bankInterestRequest);
		setBankInterestReply(null);
	}	
	
	public BankInterestRequest getBankInterestRequest() {
		return bankInterestRequest;
	}
	
	private void setBankInterestRequest(BankInterestRequest bankInterestRequest) {
		this.bankInterestRequest = bankInterestRequest;
	}

	public void setBankInterestReply(BankInterestReply bankInterestReply) {
		this.bankInterestReply = bankInterestReply;
	}

    /**
     * This method defines how one line is shown in the ListViewLine.
     * @return
     *  a) if BankInterestReply is null, then this item will be shown as "bankInterestRequest.toString ---> waiting for loan reply..."
     *  b) if BankInterestReply is not null, then this item will be shown as "bankInterestRequest.toString ---> bankInterestReply.toString"
     */
	@Override
	public String toString() {
	   return bankInterestRequest.toString() + "  --->  " + ((bankInterestReply !=null) ? bankInterestReply.toString() : "waiting for loan reply...");
	}
	
}
