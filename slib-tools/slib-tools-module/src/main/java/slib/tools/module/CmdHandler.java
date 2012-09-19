/*

Copyright or © or Copr. Ecole des Mines d'Alès (2012) 

This software is a computer program whose purpose is to 
process semantic graphs.

This software is governed by the CeCILL  license under French law and
abiding by the rules of distribution of free software.  You can  use, 
modify and/ or redistribute the software under the terms of the CeCILL
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info". 

As a counterpart to the access to the source code and  rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability. 

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate,  and  that  also
therefore means  that it is reserved for developers  and  experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or 
data to be ensured and,  more generally, to use and operate it in the 
same conditions as regards security. 

The fact that you are presently reading this means that you have had
knowledge of the CeCILL license and that you accept its terms.

 */
 
 
package slib.tools.module;

import java.util.Comparator;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import slib.utils.ex.SGL_Ex_Critic;
import slib.utils.ex.SGL_Exception;

/**
 * Abstract command handler
 * 
 * @author Sebastien Harispe
 *
 */
public abstract class CmdHandler {
	
	public ModuleCst cst;
	public ModuleCmdHandlerCst cstCmd;
	
	public Options options;
	public HelpFormatter helpFormatter;
	
	public Map<Option,Integer> optionsOrder;

	private String USAGE;
	private final String HEADER = "----------------------------------------------------------------------";
	private String FOOTER = HEADER;
	
	static Logger logger = LoggerFactory.getLogger(CmdHandler.class);

	Comparator<Option> comparator = new Comparator<Option>() {

		public int compare(Option o1, Option o2) {
			if(optionsOrder.get(o1) > optionsOrder.get(o2))
				return 1;
			else
				return 0;
		}
	};
	
	public CmdHandler(ModuleCst cst,ModuleCmdHandlerCst cstCmd, String[] args) throws SGL_Exception{
		this.cst = cst;
		this.cstCmd = cstCmd;
		
		USAGE = "java -jar "+this.cstCmd.getAppCmdName()+" [args see below]";
		
		this.optionsOrder = cstCmd.getOptionOrder();
		
		logger.info(HEADER);
		logger.info(cst.getAppName()+" "+cst.getVersion());
		logger.info(HEADER);
		showRef();
		logger.info(HEADER);
		
		buildOptions();
		
		helpFormatter = new HelpFormatter();
		helpFormatter.setOptionComparator(comparator);
		
		processArgs(args);
	}
	

	public abstract void processArgs(String[] args) throws SGL_Exception;

	
	public void showCmdLineExamples() throws SGL_Exception{
		throw new SGL_Ex_Critic("No command line examples, sorry");
	}

	public void showDescription() {
		if(cst.getDescription() != null)
			logger.info(cst.getDescription());
	}
	
	public void showReportBug() {
		if(cst.getReportBug() != null)
			logger.info("Bugs : "+cst.getReportBug());
	}

	public void showRef() {
		if(cst.getReference() != null)
			logger.info("Please cite: "+cst.getReference());
	}

	public void ending(String message,boolean showHelp,boolean showDesc,boolean showBubreport){

		if(message != null)
			logger.info(message);

		logger.info(HEADER);
		
		if(showDesc)
			showDescription();
		if(showBubreport)
			showReportBug();
		if(showHelp)
			helpFormatter.printHelp( USAGE, HEADER, options, FOOTER );

		System.exit(-1);

	}
	

	public void ending(String message,boolean showHelp){


		logger.info(HEADER);
		showDescription();
		showReportBug();
		
		if(message != null)
			logger.info(message);

		
		if(showHelp)
			helpFormatter.printHelp( USAGE, HEADER, options, FOOTER );

		System.exit(-1);

	}
	
	private void buildOptions(){

		options = new Options();

		for(Option o : optionsOrder.keySet()){
			options.addOption(o);
		}
	}






}