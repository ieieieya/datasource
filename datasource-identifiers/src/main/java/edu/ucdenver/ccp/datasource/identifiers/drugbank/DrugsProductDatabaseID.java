package edu.ucdenver.ccp.datasource.identifiers.drugbank;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

/**
 * This class represents the Entrez Gene ID concept. Entrez Gene IDs are unique integers
 * representing gene concepts in the Entrez Gene database:
 * http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public class DrugsProductDatabaseID extends IntegerDataSourceIdentifier {

	public DrugsProductDatabaseID(Integer resourceID) {
		super(resourceID);
	}

	public DrugsProductDatabaseID(String geneIDStr) {
		super(geneIDStr);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DPD;
	}
}