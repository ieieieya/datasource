/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.mgi;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * Data representation of contents of MGI_PhenoGenoMP.rpt file
 * <p>
 * Columns:
 * 
 * <pre>
 * Allelic Composition	Allele Symbol(s)	Genetic Background	Mammalian Phenotype ID	PubMed ID	MGI Marker Accession ID (comma-delimited)
 * </pre>
 * 
 * 
 * Sample Data: Ptk2<tm1Imeg>/Ptk2<tm1Imeg> involves: C57BL/6 * CBA MP:0005221 7478517,7566154
 * MGI:95481 Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr> involves: 129/Sv * C57BL/6 * CD-1
 * MP:0001672 9226455 MGI:1347476,MGI:95841
 * 
 * Fields (examples from first row above): AllelicComposition: Ptk2<tm1Imeg>/Ptk2<tm1Imeg>
 * GeneticBackground involves: C57BL/6 * CBA MammalianPhenotypeID MP:0005221 PubmedIDs 7478517,
 * 7566154 MgiAccessionIDs MGI:95481
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI, label = "MGIPhenoGenoMP record")
public class MGIPhenoGenoMPFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "MGI_PHENO_GENO_FILE_RECORD_";
	private static final Logger logger = Logger.getLogger(MGIPhenoGenoMPFileData.class);

	/*
	 * Allelic Composition Genetic Background Mammalian Phenotype ID PubMed ID MGI Marker Accession
	 * ID (comma-delimited)
	 */
	@RecordField
	private final String allelicComposition;
	@RecordField
	private final String geneticBackground;
	@RecordField
	private final MammalianPhenotypeID mammalianPhenotypeID;
	@RecordField
	private final Set<PubMedID> pubmedIDs;
	@RecordField
	private final Set<MgiGeneID> mgiAccessionIDs;

	public MGIPhenoGenoMPFileData(String allelicComposition, String geneticBackground,
			MammalianPhenotypeID mammalianPhenotypeID, Set<PubMedID> pubmedID, Set<MgiGeneID> mgiAccessionIDs,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.allelicComposition = allelicComposition;
		this.geneticBackground = geneticBackground;
		this.mammalianPhenotypeID = mammalianPhenotypeID;
		this.pubmedIDs = pubmedID;
		this.mgiAccessionIDs = mgiAccessionIDs;
	}

	public String getAllelicComposition() {
		return allelicComposition;
	}

	public String getGeneticBackground() {
		return geneticBackground;
	}

	public MammalianPhenotypeID getMammalianPhenotypeID() {
		return mammalianPhenotypeID;
	}

	public Set<PubMedID> getPubmedIDs() {
		return pubmedIDs;
	}

	public Set<MgiGeneID> getMgiAccessionIDs() {
		return mgiAccessionIDs;
	}

	public static MGIPhenoGenoMPFileData parseMGIPhenoGenoMPLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 7) {
			// there are some instances where there appears to be an extra tab in between columns 5
			// and
			// 6
			toks = new String[] { toks[0], toks[1], toks[2], toks[3], toks[4], toks[6] };
		}
		if (toks.length == 6) {
			String allelicComposition = toks[0];
			// skipping allele symbol
			String geneticBackground = toks[2];
			MammalianPhenotypeID mammalianPhenotypeID = new MammalianPhenotypeID(toks[3]);
			String pubmedIDStr = toks[4];
			String[] pubmedIDToks = pubmedIDStr.split(",");
			Set<PubMedID> pubmedIDs = new HashSet<PubMedID>();
			for (String pubmedID : pubmedIDToks) {
				if (pubmedID.trim().length() > 0) {
					pubmedIDs.add(new PubMedID(pubmedID));
				}
			}

			String mgiAccessionIDStr = toks[5];
			String[] mgiToks = mgiAccessionIDStr.split(",");
			Set<MgiGeneID> mgiAccessionIDs = new HashSet<MgiGeneID>();
			for (String mgiID : mgiToks) {
				mgiAccessionIDs.add(new MgiGeneID(mgiID));
			}
			return new MGIPhenoGenoMPFileData(allelicComposition, geneticBackground, mammalianPhenotypeID, pubmedIDs,
					mgiAccessionIDs, line.getByteOffset(), line.getLineNumber());
		}

		String errorMessage = "Unexpected number of tokens on line (" + toks.length + " != 6 expected): "
				+ line.getText().replaceAll("\\t", " [TAB] ");
		logger.error(errorMessage);
		return null;
	}

}