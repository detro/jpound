/**
 * File: AsteriskConfigurationFamily.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.wrapper;

import java.sql.ResultSet;
import java.util.Map;

import org.jpound.exception.UnableToDeleteMember;
import org.jpound.exception.UnableToInsertMember;
import org.jpound.exception.UnableToSelectMember;
import org.jpound.exception.UnableToUpdateMember;

/**
 * Questa interfaccia descrive le funzionalita' di base per poter
 * realizzare una Classe di Wrapping ad una Configuration Family di Asterisk.
 * 
 * Ogni elemento presente in una Family e' rappresentato come una Map e 
 * definito "Member". Il motivo di tale "nome" e' da ricercarsi nel fatto
 * che gli sviluppatori di Asterisk hanno definito questa parte della
 * configurazione Realtime "Families". Quindi ogni elemento nella Family e'
 * un "Membro della Famiglia".
 * 
 * La scelta di utilizzare le Mappe e' stata fatta perche' ogni Family e' 
 * diversa dalle altre (per esempio, una Family "SIP" contiene tutte le
 * informazioni relative ad un account SIP, mentre una Family "Extension" 
 * contiene le poche informazioni che costituiscono una Estensione).
 * La coppia "chiave"-"valore" e' sufficientemente flessibile per poter
 * definire ogni tipo di Family.
 * 
 * Le operazioni di "SELECT", di "UPDATE" e di "DELETE" ovviamente richiedono
 * la presenza di un "criterio": per farlo si e' pensato alla semplice
 * possibilita' di selezionare un Membro in base al valore di determinati
 * campi. Questo motiva le "conditions" fornite come Map.
 * 
 * E' possibile comunque fornire delle "condizioni" in forma arbitraria
 * (ma che dipendono <b>strettamente</b> dall'implementazione interna).
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.wrapper
 * @startdate Jul 23, 2006
 * @type AsteriskConfigurationFamily */
public interface AsteriskConfigurationFamily {
	
	/**
	 * Seleziona una o piu' Membri in base alle condizioni fornite.
	 * Se conditions = "null" vengono selezionate tutti i Membri.
	 * 
	 * @param conditions
	 * @return ResultSet delle Row trovate
	 * @throws UnableToSelectMember */
	public ResultSet selectMember (Map conditions) 
		throws UnableToSelectMember;

	/**
	 * @see AsteriskConfigurationFamily#selectMember(Map)
	 * 
	 * In questo caso e' possibile specificare delle "condition" 
	 * di selezione "arbitrarie".
	 * 
	 * @param conditions
	 * @return ResultSet delle Row trovate
	 * @throws UnableToSelectMember */
	public ResultSet selectMember (String conditions) 
		throws UnableToSelectMember;
	
	/**
	 * Inserisce un Membro nella Family.
	 * 
	 * @param values
	 * @throws UnableToInsertMember */
	public void insertMember (Map values) 
		throws UnableToInsertMember;
	
	/**
	 * 
	 * Aggiorna una o piu' Membri in base alle "conditions".
	 * 
	 * @param values
	 * @param conditions
	 * @return Numero di Row aggiornate
	 * @throws UnableToUpdateMember */
	public int updateMember (Map values, Map conditions) 
		throws UnableToUpdateMember;
	
	/**
	 * @see AsteriskConfigurationFamily#updateMember(Map, Map)
	 * 
	 * In questo caso e' possibile specificare delle "condition" 
	 * di selezione "arbitrarie".
	 * 
	 * @param values
	 * @param conditions
	 * @return Numero di Row aggiornate
	 * @throws UnableToUpdateMember
	 */
	public int updateMember (Map values, String conditions) 
		throws UnableToUpdateMember;
	
	/**
	 * Cancella una o piu' Membri in base alle "conditions".
	 * 
	 * @param conditions
	 * @return Numero di Row cancellate
	 * @throws UnableToDeleteMember */
	public int deleteMember (Map conditions) 
		throws UnableToDeleteMember;
	
	/**
	 * @see AsteriskConfigurationFamily#deleteMember(Map)
	 * 
	 * In questo caso e' possibile specificare delle "condition" 
	 * di selezione "arbitrarie".
	 * 
	 * @param conditions
	 * @return Numero di Row cancellate
	 * @throws UnableToDeleteMember */
	public int deleteMember (String conditions) 
		throws UnableToDeleteMember;
	
	/**
	 * Valida una mappa di valori, controllando che sia valida come Membro
	 * della Family corrente.
	 * 
	 * Per farlo, confronta i nomi delle chiavi e verifica se sono parte o
	 * meno del set di chiavi previste per questa Family.
	 * E' quindi ovvio pensare che ogni Configuration Family sia dotata di
	 * una "mappa" delle chiavi che la constituiscono.
	 * 
	 * @param values
	 * @return "true" se valida, "false" altrimenti */
	public boolean isValidMember (Map valuesToValidate);
}
