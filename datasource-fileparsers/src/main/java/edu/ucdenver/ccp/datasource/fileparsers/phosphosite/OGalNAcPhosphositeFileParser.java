package edu.ucdenver.ccp.fileparsers.phosphosite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.fileparsers.download.HttpURL;

/**
 * http://www.phosphosite.org/downloads/O-GalNAc_site_dataset.gz
 * 
 * @author Heather Underwood
 * 
 */
public class OGalNAcPhosphositeFileParser extends PhosphositeFileParser {

	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	
	@HttpDownload(url = HttpURL.PHOSPHOSITE_OGALNAC)
	private File OGalNAcFile;

	public OGalNAcPhosphositeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding);
	}

	public OGalNAcPhosphositeFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(OGalNAcFile)), encoding, skipLinePrefix);
	}

}
