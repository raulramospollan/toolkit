package bigs.core.commands;

import java.io.File;
import java.io.FileOutputStream;

import bigs.api.data.DataItem;
import bigs.api.data.RawDataItem;
import bigs.api.storage.DataSource;
import bigs.api.storage.Result;
import bigs.api.storage.ResultScanner;
import bigs.api.storage.Table;
import bigs.core.BIGS;
import bigs.core.utils.Log;


public class DownloadFiles extends Command {

	@Override
	public String getCommandName() {
		return "download.files";
	}

	@Override
	public String[] getHelpString() {
		return new String[] { "'bigs "+this.getCommandName()+" tableName destinationDir'" };
	}

	@Override
	public void run(String[] args) throws Exception {
		
		String tableName = args[0];
		File destinationDir = new File(".");
		if (args.length==2)  destinationDir = new File(args[1]);
		
    	DataSource dataSource = BIGS.globalProperties.getPreparedDataSource();
    	
    	Table table = dataSource.getTable(tableName);
    	ResultScanner scanner = table.getScan(table.createScanObject());
    	Result r = null;
    	while ( (r=scanner.next())!=null ) {
    		File destFile = new File (destinationDir, r.getRowKey());
    		Log.info("downloading to file "+destFile.getName());
    		DataItem dataItem = DataItem.fromResult(r);
    		FileOutputStream fstream = new FileOutputStream(destFile);
    		fstream.write(dataItem.asFileContent());
    		fstream.close();
    	}
    	
	}
	
	@Override
	public String getDescription() {
		return "downloads files from the specified table";
	}

	@Override
	public Boolean checkCallingSyntax(String[] args) {
		return args.length==1 || args.length==2;
	}

}
