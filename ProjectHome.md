Based on the ARA (Asterisk Realtime Architecture) and builded with DB-Centric-Systems in mind, JPound is a young but powerful (I hope ;)) idea to make Asterisk a complete, manageable, extensible, flexible Phone System.

Actually, JPound version is 1.0 stable.

The main structure is good defined and it seems to be solid but extensible.
I have many idea about future services and expansions: it's a 'testing-the-field' release.

Required Library:
- Log4J
- Jakarta Commons Configuration
- Asterisk-Java

On the SVN repository, you can see also 2 sub-projects: 1) JPound-SipAddressBook and 2) JPound-UserSearcher.
They are called 'JPoundAgiScripts', programmable extensions for the JPound Fast-AGI part (take a look at related documentation on http://www.voip-info.org).

- JPound-SipAddressBook
> It's the first example of Asterisk service you can create with JPound.
> It permit to call an user 'by nick': you only must digit on the
> phone-keypad the name of the user tou are looking for, using the
> letters associated with any number.
> Take a look here to understand: http://dialabc.com/motion/keypads.html.
- JPound-UserSearcher
> When you call a user, JPound start a 'distribuite call', make trilling
> every phone (soft or hard phones) that user have declared be registered
> to. The structure of this 'Script' is based on the Strategy Pattern,
> making it very extensible and configurable (ex., for new technology
> phone, like Jingle).

