/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.irefweb;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.obo.MiOntologyIdTermPair;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import lombok.Getter;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
@Record(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD, dataSource = DataSource.IREFWEB, label = "source db")
@Getter
public class IRefWebInteractionSourceDatabase extends MiOntologyIdTermPair {

	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD___SOURCE_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final MolecularInteractionOntologyTermID sourceDatabaseId;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD___SOURCE_DATABASE_NAME_FIELD_VALUE)
	private final String sourceDatabaseName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractionSourceDatabase(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.sourceDatabaseId = getId();
		this.sourceDatabaseName = getTermName();
	}

	public IRefWebInteractionSourceDatabase(MolecularInteractionOntologyTermID id) {
		super(id);
		this.sourceDatabaseId = getId();
		this.sourceDatabaseName = getTermName();
	}

}
