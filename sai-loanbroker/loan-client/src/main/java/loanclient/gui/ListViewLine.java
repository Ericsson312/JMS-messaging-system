package loanclient.gui;


import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

/**
 * This class is an item/line for a ListViewLine. It makes it possible to put both BankInterestRequest and BankInterestReply object in one item in a ListViewLine.
 */
class ListViewLine {
	
	private LoanRequest loanRequest;
	private LoanReply loanReply;
	
	public ListViewLine(LoanRequest loanRequest) {
		setLoanRequest(loanRequest);
		setLoanReply(null);
	}	
	
	public LoanRequest getLoanRequest() {
		return loanRequest;
	}
	
	private void setLoanRequest(LoanRequest loanRequest) {
		this.loanRequest = loanRequest;
	}

	
	public void setLoanReply(LoanReply loanReply) {
		this.loanReply = loanReply;
	}

    /**
     * This method defines how one line is shown in the ListViewLine.
     * @return
     *  a) if BankInterestReply is null, then this item will be shown as "loanRequest.toString ---> waiting for loan reply..."
     *  b) if BankInterestReply is not null, then this item will be shown as "loanRequest.toString ---> loanReply.toString"
     */
	@Override
	public String toString() {
	   return loanRequest.toString() + "  --->  " + ((loanReply !=null)? loanReply.toString():"waiting for loan reply...");
	}
	
}
