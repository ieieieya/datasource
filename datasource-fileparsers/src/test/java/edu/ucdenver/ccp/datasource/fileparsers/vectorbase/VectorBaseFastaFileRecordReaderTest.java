package edu.ucdenver.ccp.datasource.fileparsers.vectorbase;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VectorBaseID;

public class VectorBaseFastaFileRecordReaderTest extends RecordReaderTester {

	private static final String SAMPLE_INPUT_FILE_NAME = "sample_TRANSCRIPTS.fa.gz";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INPUT_FILE_NAME;
	}

	@Override
	protected VectorBaseFastaFileRecordReader initSampleRecordReader() throws IOException {
		System.out.println("sample: " + sampleInputFile.getAbsolutePath());
		return new VectorBaseFastaFileRecordReader(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		VectorBaseFastaFileRecordReader rr = initSampleRecordReader();

		assertTrue(rr.hasNext());
		VectorBaseFastaFileRecord record = rr.next();
		assertEquals("supercont1.1:2714033:2718806:-1", record.getContig());
		assertEquals(new VectorBaseID("AAEL000004"), record.getGeneId());
		assertEquals(new VectorBaseID("AAEL000004-RA"), record.getSequenceId());
		assertEquals("glycosyltransferase", record.getSequenceName());
		assertEquals("protein_coding", record.getSequenceType());
		assertEquals("CGCCAGATAGCACTACCTGATTAGCCTTTGAGCAGAATTAAATGTCTGGCAGCACCAGCGTCTAGACGCATACGCATTCT"
				+ "CGTTGCAGACGTGTGAGTGCAAGCGAAAGTTTATTTTTGTCGCGTGAGTGGAACCGTTTTTCAAGCGTTTCGCTTACCTC"
				+ "CCGGCATGAACCGATCCTCAGTGGTTTGTGTGTGTGTGAGAAGAAAGTTCATTTATGTTGAAGCGCTGATCCATCACTTT"
				+ "ACCCCAGTCAGGTACAGTCGAGTGCTTTCTTCGAGGAGGCCCCTCTGCCGCGAATTTGGTTTCCGTAGTTTAGTGCTAAA"
				+ "TTTTACGTTGCTGGTAACAGTTTTGGTTGATAATTAACAAGTTAAAGTTTTGTTGGCGTCGTTCCGCCTGAAATGTGCTA"
				+ "GTGTTCGGTGATCTAAACGCTTATTCGTAAGTGAAATCAGCCAGTGAAAATTTTGGGGTTTGGAAGAAGCACGCGACGCA"
				+ "TGGTTGCGGCATTTATTTTGATAAACGAAAACCATCCCACCAGAAACTTATGACGAGATGCTGAATAAAGTGTTGCTATT"
				+ "CTTCGTCGCCCTGCGATTGGCGTCCGTTTTTATCGTGCGAACCTGGTATGTCCCGGACGAGTATTGGCAGAGTTTGGAAG"
				+ "TTGCTCATCGGCTTGCATTTGGCTACGGTCACCTAACATGGGAATGGACGTATGGTATCCGAAGTTACGTTTACCCATCG"
				+ "ATCATAGCCGGTCTGTACAAGCTGTTGGCATTGGTGAAACTTGACTGCGTAGAACTGCTTGTTCTGCTGCCACGAGTTCT"
				+ "GCAGGCCTTACTTTCGGCTTACTCGGATTATCGGTTCTACTTGTGGTCGAACAAGAGCAAGTGGTCCGTCTTCATGATAG"
				+ "CCACTTCGTGGTTCTGGTTCTACACGGCGTCCCGTACGCTGTCGAATACTTTGGAAGCATCACTCACGGTCATAGCTCTC"
				+ "AGCTATTTCCCCTGGTCCGGATCGGAATGCACTGCCTTCTTGTGGCCAGTAGCGATTTCAGTTTTCTTGAGACCTACTTC"
				+ "TGCTATCCCATGGATTCCACTGTGCCTGTATCATATGAAAAAGTCGTCCTATCCAGTTTGGGAGCTGTTACTGAAACGTT"
				+ "ATCTAGTGATCGGATTCGTTGTGGGCACGTGTGCAGTTGCCATTGATACATTCGTACATGGGTCTCTGTTGTTATCTCCG"
				+ "TACGAGTTTATACGCTTCAACGTATATGAGGGAATCGGAAGCTTCTACGGTGAACATCCATGGTACTGGTATGTAAATGT"
				+ "AGGTCTTCCAACTGTTCTGGGAGTTGCGACATTGCCGTTCCTGTTTGCTGCAGTGGAAACCATTCGCCATCGTCAGGTCT"
				+ "ACAAGGAAAGGGCTATTTTGCTTCTTAGTGTACTTTTCACTGTCGTGGTCTATTCTTTGTTACCACACAAGGAGTTCCGT"
				+ "TTCATGCTGCAGATTTTACCAATTTGCCTCTACGTTTCAGCAGATTTCATGTCCCGTTGGAGCAGAAAAGCTTCTGGTAA"
				+ "ACTGGTGTGGCTCGCTGCTTTGGTTCTACTAGTTTTGAACGCCATTCCGGCAGGTTATTTGAGTTTGGTTCATCAGCGTG"
				+ "GCACATTGGATGTAATGTCTTCGCTGCAGAATATCGCCAGAAATTATCAAGATGAAAGTGGTCAAGGCGCTAAATTTCTG"
				+ "TTTTTGATGCCATGTCATTCCACACCTTATCATAGCCATATCCATCACAATACGACCATGCGCTTTTTGCGCTGTGAACC"
				+ "CCACTTCTATGAAAACCACGTTTTCAGAGACGAAGCCGAGACATTCTACAAGGATCCTATCAGTTGGCTACGACGAAATA"
				+ "TCCCCGTTCACCCACGCAGCGCCATGCCATCCCACGTGATTGCATTCGATGTTCTGGAACCTGAGATCAAAGATTTTCTT"
				+ "ACCAACTATGAGAAACTAGATTCATTTTTCCATTCCGATTATAAAACCGAAAGAATTGGTGGAAATGTGGTTATCTACGA"
				+ "AAGATACAATCCGAGCAAACCTACACCAACACCGATAGTGACAAAGGAGGAACCTGACAATCAACCGACTGAGGGAGAAA"
				+ "CAGAGGCCTAAGCAGCTCCAACCTCTTAAGCTTAAGATTTCGTGTGCATTTGACATTTCTTTTAATTTGTGTCTTTAAAA"
				+ "GTTAACATAGAACCAAATTATCACTCTTTTGGTTGAAGTTTATCAATATATTTTTTTTTAAATTAACTGTGCGGCAATAA"
				+ "TAATAGGTTTTGAACATAAAGTTTGGCAATTGAATTCTACATTCGATACGACAACGTACTTTTCTTTGCTCTTCATCTAG"
				+ "TATAAGAAATGAACATACTTCCTCTGGGGAAGACGATGCAGCAATGTTTTTGACTGTAGATTTTTTTAGATATGTAAACT"
				+ "GACAAACTCGATTACCCGACAACATTGTGTCAAACTGAGATGTGTCTCACGGTGGAAGATTTGACCCCACCGTTGTAGAA"
				+ "CAACGTCCATTAGAAATAGTTTCCTAATTAAACAAAACCTTGCTAAGTTTACCACGTTTTTCTTGAAATAAAGTGTTCAT" + "ATTTTAATAAT",
				record.getSequence());

		assertTrue(rr.hasNext());
		record = rr.next();
		assertEquals("supercont1.1:4208813:4221459:1", record.getContig());
		assertEquals(new VectorBaseID("AAEL000005"), record.getGeneId());
		assertEquals(new VectorBaseID("AAEL000005-RB"), record.getSequenceId());
		assertNull(record.getSequenceName());
		assertEquals("protein_coding", record.getSequenceType());
		assertEquals("GCAAACAGCAACAAAAGGCGTGCGGTGCGAAGAAACTTTGCGAGTCCGAAACCGATACAGTAATTTTGTGGTTTTCCCGT"
				+ "GGAAAATTTTGCCGTGGTGATACAATCTCACTGTTTGGTTGGTTCTCGTGGAAAACTTGTTCGCAACCGTCTCGTGCACT"
				+ "TGGGACGATCACTTGCCCTCGAAAATCTGCCAGTCCTGCCAACTACGCCTCCAAGACGCCTTCAATTTCATAATGGAATG"
				+ "CTGCGATAACAGCGTGTTGCTCGAATCGTTTAAAAGGGAAGCCCTCGAGCATTTCTTCAGAGCAAATCGGATAGAGGTGA"
				+ "AGGAACTGCTGAACGACGACGATGAGGGGTCCGAACTGGGAGAATCTCACAGCACTCTGCAATCGAACGCGGTCGGAGGA"
				+ "GGTGCCAAAGAAATACTTCAAATCGAACGTCTGGAAACGCCGAACGATAACTTCATGATATTCGAAATCGGCGAAGACTT"
				+ "TGAAATGCAACAGCAGCAGCAAATGAAAGTCGAGCCACCGGAAGAGCTCGTAACGACACATGATGAATCGACTGGTGAAG"
				+ "AGATTGAAACCAATGCCGTGGACTTCATTCTGAACAACCTGAAGCCGAGGGAGAGAAAACGAAACAGCGCTACGGGATCA"
				+ "AAACGAAAAGCTGCATCGGATAAGGCCTACAAGTGCGAGGTGTGCGGCAAAAGGTTCCAACGCAAGTCCAACTTGGTCGA"
				+ "CCATCTACGATTGCATGCCAACGTGAAGCTTTTCTCGTGCAGCTACTGCAGTGCATCCTTCGTTCAGGCCGGTAACTTGA"
				+ "AGAGTCACATCCGTCGACACACCATGGAGAAACCTTACAAGTGCGAGTATTGTGGAAAGTGCTACACTCAGTCGAGTGCC"
				+ "CTCAAAACTCATGTCAGATCTCACACAAACGAGCGGCACTATGTGTGCGACGTATGCCAGAAAGGGTTCACTAACAGCTC"
				+ "CGATATGAACAAGCACAAAATGACCCATTCGGATGTGCGCTTCTACCAGTGCGTTGTGTGCGTCAAGCGGTACTTCACGC"
				+ "AGAAGGTACACCTGAAGAAGCATCTAAGCACGTACCATCAGGATGGTGATTTTGACGAGCTACTGCAGCAGGGCACCCTG"
				+ "AAGGAAGGCGTGAATGTTGCGGTTAAACCTAAAAAGCAGGTCGTATCGCAAGCTATCGATGCGTGAAGTGTATGCACAAG"
				+ "ATAATCTTAAGATGAAGGCTCTTTTTGTACAGAAATATAACCACTGAAGTACTGTGAACTGAACTACG", record.getSequence());

		assertTrue(rr.hasNext());
		record = rr.next();
		assertEquals("supercont1.1166:68964:69675:1", record.getContig());
		assertEquals(new VectorBaseID("AAEL014561"), record.getGeneId());
		assertEquals(new VectorBaseID("AAEL014561-RA"), record.getSequenceId());
		assertEquals("Vitelline membrane protein 15a-3 {ECO:0000303|PubMed:9887508}", record.getSequenceName());
		assertEquals("protein_coding", record.getSequenceType());
		assertEquals("CGTTCAAAACAAGAGGACCAAAGTACAAAATTCATGGCTGCAAACCAAACAGGTGCAGTAAGCAATTTCCAATTATAAAA"
				+ "CCCGGTGGATTCGGTGGATTGGAGCATCAGTTCGCTACACAGTTTCAGTGCAGTTACATAGTATTTCCAACGACTTCGGA"
				+ "AGGAATCCATCCAACTCAACCGGAACCATGAACAAGTTCATCATCTTGGCTCTCTTCGCCGTGGCCGCCGCCAGTGCCAT"
				+ "GCCAAACTACCCACCCCCACCACCGAAGCCATACCACGCTCCACCCCCACCACCACACCACGCTCACCCACCACCACCAC"
				+ "CACCACCACCAGCCCACTACGGACACCACGCTCACCCAGCACCAGCTCCAGTTGTCCACACTTACCCAGTGCACGCTCCT"
				+ "CACGCCAAGTGCGGAGCCAACCTGTTGGTCGGATGCGCCCCAAGCGTAGCACATGCCCCATGTGTCCCCTTGCACGGACA"
				+ "TGGACACGGATACCCCGCACCAGCTCCCCACTACAGGGCTCCGGAATCCGACTCGTTCGATCAGTTCGAAGAATAAGCGG"
				+ "ATCATCGATTGGACTGACGCGAAGGAACAACTTGCCTGAGATGTGAACATACTCGACTGAATGAAAAGTGTTTGAATGGT"
				+ "GTAAACGTTCAACTAATTGTGGAGAAGTGTATTAAGTTTGACCAACGAATAAACCTAATTTATTGAAATTTA", record.getSequence());

		assertFalse(rr.hasNext());

	}

}