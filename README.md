# JMS-Messaging-System

The current system consists of several java desktop applications. The applications send and receive messages using JMS system.
The system simulates the behaviour of loan-broker enterprise applications.

1) LoanClient sends loan request(s) to the LoanBroker app.
2) When the LoanBroker receives a new loan request, it creates a BankInterestRequest and sends it to a bank.
3) When bank receives a new BankInterestRequest, it:
  a. shows it on the screen and waits for the user to enter an interest rate for this BankInterestRequest;
  b. when the user of bank enters an interest rate for one of the received BankInterestRequests, bank creates a new BankInterestReply and        sends it back to the LoanBroker.
4) When loan-broker receives a new BankInterestReply, it creates a new LoanReply and sends it back to loan-client.

![](project%20architecture%20images/LoanBrokerSystem.png)
Figure 1. The Loan Broker system
