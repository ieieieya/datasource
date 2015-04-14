/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;
import edu.ucdenver.ccp.common.string.StringUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public abstract class DuplicateFieldValueFilter implements DuplicateStatementFilter {

	private static final Logger logger = Logger.getLogger(DuplicateFieldValueFilter.class);

	private final DiskBasedHash hash;
	private List<File> noDupsFiles;
	private String previousFieldValueKey = null;
	private int _3inARowCount = 0;

	public DuplicateFieldValueFilter(DiskBasedHash hash) throws IOException {
		this.hash = hash;
		setNoDupsFiles(new ArrayList<File>());
	}

	/**
	 * Each field value is specified by 3 statements which are written consecutively to the RDF
	 * file. Here we check to make sure we get 3 of the same field URI consecutively, an exception
	 * is thrown if 3-in-a-row are not observed
	 */
	@Override
	public boolean alreadyObservedStatement(Statement stmt) {
		String subject = stmt.getSubject().toString();
		if (isFieldRdfLine(subject)) {
			String fieldValueKey = getFieldValueKey(subject);
			if (!fieldValueKey.equals(previousFieldValueKey)) {
				if (previousFieldValueKey != null && ((_3inARowCount % 3) != 0)) {
					throw new IllegalStateException("3-in-a-row-count not equal to 3 (" + _3inARowCount + "): "
							+ previousFieldValueKey);
				}
				if (previousFieldValueKey != null) {
					try {
						hash.add(previousFieldValueKey);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				previousFieldValueKey = fieldValueKey;
				_3inARowCount = 1;
			} else {
				_3inARowCount++;
			}
			if (!hash.contains(fieldValueKey)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean alreadyObservedRecordUri(URIImpl recordUri) {
		String recordKey = getRecordKey(recordUri);
		return hash.contains(recordKey);
	}

	@Override
	public void logRecordUri(URIImpl recordUri) {
		String recordKey = getRecordKey(recordUri);
		try {
			hash.add(recordKey);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * @param recordUri
	 * @return
	 */
	private String getRecordKey(URIImpl recordUri) {
		String uriStr = recordUri.toString();
		return uriStr.substring(uriStr.lastIndexOf("/"));
	}

	public void createNonRedundantRdfFiles(File baseRdfDirectory) throws IOException {
		Iterator<File> fileIterator = FileUtil.getFileIterator(baseRdfDirectory, true);
		while (fileIterator.hasNext()) {
			File file = fileIterator.next();
			if (file.getAbsolutePath().endsWith(".nt.gz") && !file.getAbsolutePath().endsWith(".nodups.nt.gz")) {
				File noDupsFile = new File(StringUtil.removeSuffix(file.getAbsolutePath(), ".nt.gz") + ".nodups.nt.gz");
				createNonRedundantRdfFile(file, noDupsFile);
				getNoDupsFiles().add(noDupsFile);
			}
		}
	}

	/**
	 * @param file
	 * @param noDupsFile
	 * @throws IOException
	 */
	private void createNonRedundantRdfFile(File inputFile, File noDupsFile) throws IOException {
		logger.info("Creating non-redundant version of file: " + inputFile.getAbsolutePath());
		BufferedWriter writer = FileWriterUtil.initBufferedWriter(
				new GZIPOutputStream(new FileOutputStream(noDupsFile)), CharacterEncoding.UTF_8);
		StreamLineIterator lineIter = new StreamLineIterator(new GZIPInputStream(new FileInputStream(inputFile)),
				CharacterEncoding.UTF_8, null);
		long previousTime = System.currentTimeMillis();
		try {
			int lineCount = 0;
			String previousFieldValueKey = null;
			while (lineIter.hasNext()) {
				if (lineCount++ % 100000 == 0) {
					long timenow = System.currentTimeMillis();
					logger.info("Progress: " + (lineCount - 1) + " Elapsed time: " + (timenow - previousTime) / 1000
							+ "s");
					previousTime = timenow;
				}
				Line line = lineIter.next();
				String lineText = line.getText().trim();
				if (isFieldRdfLine(lineText)) {
					String fieldValueKey = getFieldValueKey(lineText.substring(0, lineText.indexOf(">")));
					if (!fieldValueKey.equals(previousFieldValueKey)) {
						if (previousFieldValueKey != null) {
							hash.add(previousFieldValueKey);
						}
						previousFieldValueKey = fieldValueKey;
					}
					if (!hash.contains(fieldValueKey)) {
						logger.debug("Writing line: " + lineText);
						writer.write(lineText);
						writer.newLine();
					} else {
						logger.debug("SKIPPING: " + lineText);
					}
				} else {
					logger.debug("Writing line: " + lineText);
					writer.write(lineText);
					writer.newLine();
				}
			}
			hash.add(previousFieldValueKey);
		} finally {
			if (writer != null) {
				writer.close();
			}
			lineIter.close();
		}

	}

	/**
	 * @param lineText
	 * @return
	 */
	private String getFieldValueKey(String lineText) {
		int secondUnderscoreIndex = lineText.indexOf("_", lineText.indexOf("_") + 1) + 1;
		// int angleBracketCloseIndex = lineText.indexOf(">");
		// String key = lineText.substring(secondUnderscoreIndex, angleBracketCloseIndex);
		String key = lineText.substring(secondUnderscoreIndex);
		logger.debug("field value key: " + key);
		return key;
	}

	/**
	 * @param lineText
	 * @return
	 */
	private boolean isFieldRdfLine(String lineText) {
		boolean isFieldLine = lineText.matches("^<?http://kabob.ucdenver.edu/iao/[^/]+/F_.*$");
		logger.debug("is field line: " + isFieldLine + " " + lineText);
		return isFieldLine;
	}

	/**
	 * @return the noDupsFiles
	 */
	public List<File> getNoDupsFiles() {
		return noDupsFiles;
	}

	/**
	 * @param noDupsFiles
	 *            the noDupsFiles to set
	 */
	private void setNoDupsFiles(List<File> noDupsFiles) {
		this.noDupsFiles = noDupsFiles;
	}

	@Override
	public void shutdown() throws IOException {
		logger.info("shutting down duplicate triple filter...");
		hash.shutdown();
		// logger.info("non-redundant files created: ");
		// for (File file : noDupsFiles) {
		// logger.info("NON-REDUNDANT FILE: " + file.getAbsolutePath());
		// }
	}

	// /**
	// * @param args
	// * args[0] - base rdf directory<br>
	// * args[1] - cache storage file
	// *
	// */
	// public static void main(String[] args) {
	// BasicConfigurator.configure();
	// Logger.getRootLogger().setLevel(Level.INFO);
	// File baseRdfDirectory = new File(args[0]);
	// File storageFile = new File(baseRdfDirectory, "filter-cache");
	// int transactionFrequency = Integer.parseInt(args[1]);
	// try {
	//
	// // DiskBasedHash hash = new JcsCache();
	// DiskBasedHash hash = new Jdbm2Cache(new File(storageFile, "jdbms"));
	// // DiskBasedHash hash = new Neo4jCache(storageFile, transactionFrequency);
	// // DiskBasedHash hash = new BerkeleyDbCache(storageFile);
	// // DiskBasedHash hash = new HashSetCache();
	//
	// DuplicateFieldValueFilter filter = new DuplicateFieldValueFilter(hash);
	//
	// filter.createNonRedundantRdfFiles(baseRdfDirectory);
	// filter.shutdown();
	// } catch (Exception e) {
	// logger.error("Error while creating non-redundant triple files: ", e);
	// System.exit(-1);
	// }
	//
	// }

}