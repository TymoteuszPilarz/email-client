# EmailClient
Simple email client with graphical user interface written in java.<br/>
SMPT and POP3 protocols are used for sending and receiving emails respectively. Currently only polish email service provider - "Wirtualna Polska" is supported but it's easy to add your own by providing appropriate class in src/main/java/emailclient/providers, similar to the already created "WPClient". Note that some more secure email service providers cannot be added since they may require protocols like OAuth, which are not implemented in this application.

# Demonstration
<img width="1096" alt="screenshot1" src="https://github.com/TymoteuszPilarz/email-client/assets/122737837/d57c8097-046e-4deb-a2d2-c6343a724699">
<b>Fig. 1 Sign in window </b>
<img width="1097" alt="screenshot2" src="https://github.com/TymoteuszPilarz/email-client/assets/122737837/408e31f2-bfe5-463a-a159-7b3e73b2a535">
<b>Fig. 2 Mailbox window </b>
