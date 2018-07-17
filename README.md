# ConcurrentBanker
Simple implementation of Banker's algorithm using concurrent approach. Clients and the Banker are represented as threads. There is one resource - *florens*.
# General assumptions :
- when Banker rejects to give a loan to client with id *i* for *n* times in a row, because such capital is unavailable, banker will refuse to give further loans until other clients return money and client with id *i* comes to the bankers and asks for the loan.
- Every clients has a maximum number of loan requests which is the termination condition.
- The mechanism that is used to ensure that only one client is visiting the Banker at the time is Semaphore. Sempahore is used here to implement monitor on *Banker.meetClient* method. One of other posibilities would be to use *synchronized* keyword there.
