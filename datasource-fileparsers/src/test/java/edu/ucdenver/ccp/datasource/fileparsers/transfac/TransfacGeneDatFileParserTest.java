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
package edu.ucdenver.ccp.fileparsers.transfac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.transfac.TransfacFactorID;
import edu.ucdenver.ccp.datasource.identifiers.transfac.TransfacGeneID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class TransfacGeneDatFileParserTest extends RecordReaderTester {

	private final static String SAMPLE_TRANSFAC_GENE_DAT_FILE_NAME = "Transfac_gene.dat";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_TRANSFAC_GENE_DAT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new TransfacGeneDatFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			TransfacGeneDatFileParser parser = new TransfacGeneDatFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				validateRecord(parser.next(), new TransfacGeneID("G000001"), (MgiGeneID) null, (EntrezGeneID) null,
						new HashSet<TransfacFactorID>(), "T00915", "T00453", "T00282", "T00915");
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				validateRecord(parser.next(), new TransfacGeneID("G000576"), new MgiGeneID("MGI:97275"),
						new EntrezGeneID(17927), CollectionsUtil.createSet(new TransfacFactorID("T00526")), "T05114",
						"T00278", "T00032", "T00045", "T08433", "T01333", "T08501", "T00752", "T00755");
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				validateRecord(parser.next(), new TransfacGeneID("G000218"), (MgiGeneID) null, new EntrezGeneID(2353),
						CollectionsUtil.createSet(new TransfacFactorID("T00123"), new TransfacFactorID("T08776")),
						"T00685", "T06384", "T08300");
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}
	}

	private void validateRecord(TransfacGeneDatFileData record, TransfacGeneID transfacGeneID, MgiGeneID mgiGeneID,
			EntrezGeneID entrezGeneID, Set<TransfacFactorID> encodedFactors, String... bindingFactorIDs) {
		assertEquals(transfacGeneID, record.getTransfacGeneID());
		assertEquals(mgiGeneID, record.getMgiDatabaseReferenceID());
		assertEquals(entrezGeneID, record.getEntrezGeneDatabaseReferenceID());
		assertEquals(encodedFactors, record.getEncodedFactorIDs());
		Set<TransfacFactorID> expectedBindingFactorIDs = new HashSet<TransfacFactorID>();
		for (String bindingFactorID : bindingFactorIDs)
			expectedBindingFactorIDs.add(new TransfacFactorID(bindingFactorID));
		assertEquals(expectedBindingFactorIDs, record.getBindingFactorIDs());

	}

	
	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/transfac/";
		List<String> lines = CollectionsUtil
				.createList(
						NS + "G000001_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/transfac/TransfacGeneIce1> .",
						NS + "G000001_ICE> <http://www.gene-regulation.com/transfac/hasTransfacGeneID> \"G000001\"@en .",
						NS + "G000001_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.gene-regulation.com/transfac/G000001> .",
						NS + "G000001_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/J01901_ICE> .",
						NS + "G000576_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/transfac/TransfacGeneIce1> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/hasTransfacGeneID> \"G000576\"@en .",
						NS + "G000576_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.gene-regulation.com/transfac/G000576> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/hasTransfacEncodedFactors> <http://www.gene-regulation.com/transfac/T00526_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/hasTransfacBindingFactors> <http://www.gene-regulation.com/transfac/T00526_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_17927_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_97275_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC103613_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/X61655_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M18779_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AK076157_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AK142859_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M84918_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC103618_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC103619_ICE> .",
						NS + "G000218_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/transfac/TransfacGeneIce1> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacGeneID> \"G000218\"@en .",
						NS + "G000218_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.gene-regulation.com/transfac/G000218> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacEncodedFactors> <http://www.gene-regulation.com/transfac/T00123_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacEncodedFactors> <http://www.gene-regulation.com/transfac/T08776_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacBindingFactors> <http://www.gene-regulation.com/transfac/T00123_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacBindingFactors> <http://www.gene-regulation.com/transfac/T08776_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_2353_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AY212879_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BX647104_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AF111167_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/S65138_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC004490_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/CR542267_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/CR541785_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/K00650_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AB022275_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AB022276_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/X53723_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AK097379_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/V01512_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M15429_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "transfac-gene.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("transfac-gene.nt", 41);
		counts.put("kabob-meta-transfac-gene.nt", 6);
		return counts;
	}

}
