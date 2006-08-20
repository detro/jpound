/**
 * File: ChannelTypeWrapperFactoryConfigurationXPaths.java
 * Created by: detro
 * Created at: Aug 6, 2006 */
package org.jpound.fastagi.usersearcher;

/**
 * Questa Interfaccia raccoglie gli XPath all'interno del file di Configurazione
 * degli oggetti che descrivono i Channel disponibili.
 * 
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher
 * @startdate Aug 6, 2006
 * @type ChannelTypeWrapperFactoryConfigurationXPaths */
public interface ChannelTypeWrapperFactoryConfigurationXPaths {
	public static final String CHANNEL_TYPES = 
			"channels.channel[@type]";
	public static final String CHANNEL_CLASSNAMES = 
			"channels.channel[@classname]";
}
