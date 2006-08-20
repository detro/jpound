/**
 * File: AbstractJPoundAgiScript.java
 * Created by: detro
 * Created at: Jul 17, 2006 */
package org.jpound.fastagi;

import org.apache.commons.configuration.Configuration;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.BaseAgiScript;

/**
 * Questa e' una classe di Base di tutti gli {@link AgiScript}
 * usabili in JPound (e pensati in "coerenza" con questo progetto).
 * E' a sua volta estesa a partire da {@link BaseAgiScript}.
 * La differenza sostanziale e' il fatto che prende in input
 * la configurazione di JPound e dispone di alcuni metodi di "Utility".
 * 
 * Importante da notare, e' che ad ogni {@link AbstractJPoundAgiScript} viene
 * fornito (dal Costruttore) un handler di tipo {@link Configuration}
 * al suo "descriptor file": questo perche' c'e' si' in questo file una
 * parte "obbligatoria" che serve ad aiutare la {@link PluggableMappingStrategy}
 * a caricare la Classe, ma il suo formato e' cosi' flessibile da permettere
 * di inserire nel descrittore anche "parametri opzionali speciali", dipendenti
 * dal particolare {@link AbstractJPoundAgiScript}.
 * 
 * La parte obbligatoria del descrittore e' (ad esempio):
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?gt;
 * &lt;agiscriptgt;
 *	&lt;class classname="package.ExampleJPoundAgiScript"gt;
 *		&lt;filenamegt;example.jar&lt;/filenamegt;
 *		&lt;descriptiongt;
 *			Description of the ExampleJPoundAgiScript
 *		&lt;/descriptiongt;
 *	&lt;/classgt;
 *  &lt;!-- Optional Section Start Here --gt;
 * </pre>
 * 
 * Nella parte opzionale e' necessario inserire nodi in formato XML valido:
 * la loro natura e' pero' del tutto arbitraria, cosi' da poter avere una
 * specie di "contenitore universale di configurazione".
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.fastagi
 * @startdate Jul 17, 2006
 * @type AbstractJPoundAgiScript
 * @class AbstractJPoundAgiScript */
public abstract class AbstractJPoundAgiScript extends BaseAgiScript {

	/** Configurazione */
	private final Configuration jpoundConfiguration;
	
	/** Configurazione dell'AgiScript */
	private final Configuration agiScriptConfiguration;
	
	/**
	 * Constructor.
	 * 
	 * @param jpoundConfiguration Configurazione di JPound 
	 * @param agiScriptConfiguration Configurazione dell'AgiScript */
	public AbstractJPoundAgiScript(
			Configuration jpoundConfiguration,
			Configuration agiScriptConfiguration ) {
		super();
		this.jpoundConfiguration = jpoundConfiguration;
		this.agiScriptConfiguration = agiScriptConfiguration;
	}

	/**
	 * Ritorna la Configurazione di questo {@link AbstractJPoundAgiScript}.
	 * Servira' alle classi che estendono questa, cosi' da poter interagire
	 * con il file descriptor.
	 * 
	 * @return Ritorna la Configurazione di questo 
	 * 		{@link AbstractJPoundAgiScript} */
	protected Configuration getAgiScriptConfiguration () {
		return this.agiScriptConfiguration;
	}
	
	/**
	 * Ritorna la Configurazione di JPound.
	 * Servira' alle classi che estendono questa, cosi' da poter interagire
	 * con la configurazione, nel caso ci fosse bisogno di ricavarne
	 * qualche parametro.
	 * 
	 * @return Ritorna la Configurazione di JPound. */
	protected Configuration getJPoundConfiguration () {
		return this.jpoundConfiguration;
	}
	
	/**
	 * Esegue l'Application "Goto" di Asterisk.
	 * 
	 * @param context Contesto di destinazione
	 * @param extension Estensione di destinazione
	 * @param priority Priorita'
	 * @throws AgiException */
	protected void execGoto(
			String context,
			String extension,
			int priority) 
		throws AgiException {
		
		this.exec("Goto", context + '|' + extension + '|' + priority);
	}
	
	/**
	 * Esegue l'Application "Goto" di Asterisk.
	 * @see AbstractJPoundAgiScript#execGoto(String, String, int)
	 * 
	 * La priorita' e' settata ad "1".
	 * 
	 * @param context Contesto di destinazione
	 * @param extension Estensione di destinazione
	 * @throws AgiException */
	protected void execGoto(
			String context,
			String extension) 
		throws AgiException {
		
		this.execGoto(context, extension, 1);
	}
	
	/**
	 * 
	 * Esegue l'Application "Dial" di Asterisk.
	 * 
	 * @param type Tipo di Device da chiamare: SIP, IAX2, Zap, ...
	 * @param identifier Account da Chiamare
	 * @param timeout 
	 * @param options
	 * 	<ul>
	 *		<li> <b>t</b>: Allow the <i>called</i> user to transfer the call by hitting #
	 *		</li><li> <b>T</b>: Allow the <i>calling</i> user to transfer the call by hitting #
	 *		</li><li> <b>r</b>: Generate a ringing tone for the calling party, passing no audio from the called channel(s) until one answers.   Without this option, Asterisk will generate ring tones automatically where it is appropriate to do so; however, "r" will force Asterisk to generate ring tones, even if it is not appropriate.  For example, if you used this option to force ringing but the line was busy the user would hear "RING RIBEEP BEEP BEEP" (thank you tzanger), which is potentially confusing and/or unprofessional.  However, the option is necessary in a couple of places.  For example, when you're dialing multiple channels, call progress information is not consistantly passed back.
	 *		</li><li> <b>R</b>: Indicate ringing to the calling party when the called party indicates ringing, pass no audio until answered. This is available only if you are using kapejod's <a title="" href="/wiki/index.php?page=Asterisk+zaphfc">bristuff</a>.
	 *		</li><li> <b>m</b>: Provide Music on Hold to the calling party until the called channel answers. This is mutually exclusive with option 'r', obviously.  Use m(class) to specify a class for the music on hold.
	 *		
	 *		</li><li> <b>n</b>: (Asterisk 1.1 and later) July 2005 <a class="external" href="http://bugs.digium.com/view.php?id=752">bug 752</a> was included in CVS (Asterisk 1.1) and enhances the privacy manager considerably. As part of this patch, the 'n' flag to Dial got changed to be used as part of the privacy features, instead of being the 'dont jump to +101' flag. That flag is now 'j'.
	 *		</li><li> <b>o</b>: Restore the Asterisk v1.0 CallerId behaviour (send the original caller's ID) in Asterisk v1.2 (default: send this extension's number)
	 *		</li><li> <b>j</b>: Asterisk 1.2 and later: Jump to priority n+101 if all of the requested channels were busy (just like behaviour in Asterisk 1.0.x)
	 *		</li><li> <b>M(</b><i>x</i><b>)</b>: Executes the macro (x) upon connect of the call (i.e. when the called party answers)
	 *		
	 *		</li><li> <b>h</b>: Allow the callee to hang up by dialing <b>*</b>
	 *		</li><li> <b>H</b>: Allow the caller to hang up by dialing <b>*</b>
	 *		</li><li> <b>C</b>: Reset the CDR (Call Detail Record) for this call. This is like using the <a title="" href="/wiki/index.php?page=Asterisk+cmd+NoCDR">NoCDR</a> command
	 *		</li><li> <b>P(</b><i>x</i><b>)</b>: Use the <a title="" href="/wiki/index.php?page=Asterisk+cmd+PrivacyManager">PrivacyManager</a>, using <i>x</i> as the database (<i>x</i> is optional)
	 *			
	 *		</li><li> <b>g</b>: When the called party hangs up, exit to execute more commands in the current context.
	 *		</li><li> <b>G(context^exten^pri)</b>: If the call is answered, transfer both parties to the specified context and extension. The calling party is transferred to priority x, and the called party to priority x+1. This allows the dialplan to distinguish between the calling and called legs of the call (new in v1.2).
	 *		</li><li> <b>A(</b><i>x</i><b>)</b>: Play an announcement (<i>x</i>.gsm) to the called party. 
	 *		</li><li> <b>S(</b><i>n</i><b>)</b>: Hangup the call <i>n</i> seconds AFTER called party picks up. 
	 *			
	 *		</li><li> <b>d</b>: This flag trumps the 'H' flag and intercepts any dtmf while waiting for the call to be answered and returns that value on the spot. This allows you to dial a 1-digit exit extension while waiting for the call to be answered - see also <a title="" href="/wiki/index.php?page=Asterisk+cmd+RetryDial">RetryDial</a>
	 *		</li><li> <b>D(</b><i>digits</i><b>)</b>: After the called party answers, send <i>digits</i> as a DTMF stream, then connect the call to the originating channel.  (You can also use 'w' to produce  .5 second pauses.)
	 *		</li><li> <b>L(</b>x[:y][:z]<b>)</b>: Limit the call to 'x' ms, warning when 'y' ms are left, repeated every 'z' ms) Only 'x' is required, 'y' and 'z' are optional. The following special variables are optional for limit calls: (pasted from app_dial.c)
	 *			
	 *		<ul><li> <b>LIMIT_PLAYAUDIO_CALLER</b> - yes|no (default yes) - Play sounds to the caller.
	 *		</li><li> <b>LIMIT_PLAYAUDIO_CALLEE</b> - yes|no - Play sounds to the callee.
	 *		</li><li> <b>LIMIT_TIMEOUT_FILE</b> - File to play when time is up.
	 *		</li><li> <b>LIMIT_CONNECT_FILE</b> - File to play when call begins.
	 *		</li><li> <b>LIMIT_WARNING_FILE</b> - File to play as warning if 'y' is defined. If <b>LIMIT_WARNING_FILE</b> is not defined, then the default behaviour is to announce ("You have [XX minutes] YY seconds").
	 *			
	 *		</li></ul></li><li> <b>f</b>: forces callerid to be set as the extension of the line making/redirecting the outgoing call. For example, some PSTNs don't allow callerids from other extensions than the ones that are assigned to you.
	 *		</li><li> <b>w</b>: Allow the <i>called</i> user to start recording after pressing *1 or what defined in <a title="" href="/wiki/index.php?page=Asterisk+config+features.conf">features.conf</a> (Asterisk v1.2.x); requires Set(DYNAMIC_FEATURES=automon)
	 *		</li><li> <b>W</b>: Allow the <i>calling</i> user to start recording after pressing *1 or what defined in <a title="" href="/wiki/index.php?page=Asterisk+config+features.conf">features.conf</a> (Asterisk v1.2.x); requires Set(DYNAMIC_FEATURES=automon)
	 *			
	 *		</li>
	 *	</ul>
	 *
	 * @throws AgiException */
	protected void execDial(
			String type,
			String identifier,
			int timeout,
			String options) throws AgiException {
		
		this.exec("Dial", 
				type + '/' + identifier + '|' + // ex. SIP/001
				timeout + 						// ex. 20
				( (options.equals("")) ? "" : '|' + options ) ); // ex. rT
	}
	
	/**
	 * Esegue l'Application "Dial" di Asterisk.
	 * Il parametro "<code>options</code>" e' settato a "<code>rT</code>".
	 * @see AbstractJPoundAgiScript#execDial(String, String, int, String)
	 * 
	 * @param type Tipo di Device da chiamare: <code>SIP</code>, 
	 * 		<code>IAX2</code>, <code>Zap</code>, ...
	 * 
	 * @param identifier Account da Chiamare
	 * @param timeout 
	 * @throws AgiException */
	protected void execDial(
			String type,
			String identifier,
			int timeout) throws AgiException {
		
		this.execDial(type, identifier, timeout, "");
	}
	
	/**
	 * Esegue l'Application "Voicemail" di Asterisk.
	 * 
	 * @param voicemail Casella di posta dove lasciare un messaggio
	 * @param context Contesto in cui si trova la casella
	 * @throws AgiException */
	protected void execVoicemail(
			String voicemail,
			String context) throws AgiException {
		
		this.exec("Voicemail",
				voicemail + '@' + context);
	}
	
	/**
	 * @see org.asteriskjava.fastagi.AgiScript#service(AgiRequest, AgiChannel) 
	 * @param request AgiRequest da gestire 
	 * @param channel AgiChannel su cui operare per gestire la Request 
	 * @throws AgiException */
	public abstract void service(AgiRequest request, AgiChannel channel) 
		throws AgiException;
}
