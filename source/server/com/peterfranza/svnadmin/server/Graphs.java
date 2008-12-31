package com.peterfranza.svnadmin.server;

import java.io.OutputStream;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import SigarUtils.SigarFactory;

public class Graphs {
	
	

	public static void getDiskFreeJpg(OutputStream out) throws Exception {

		Sigar sigar = SigarFactory.getInstance();
		DefaultCategoryDataset pieDataset = new DefaultCategoryDataset();
		
		for(FileSystem system: sigar.getFileSystemList()) {
			if(system.getType() == FileSystem.TYPE_LOCAL_DISK) {
				System.out.println(system.getDevName());
//				String path = system.getDevName();

				pieDataset.addValue(sigar.getFileSystemUsage(system.getDevName()).getAvail(), 
						system.getDevName(), "F");
				pieDataset.addValue(sigar.getFileSystemUsage(system.getDevName()).getUsed(), 
						system.getDevName(), "U");
			}
			
		}
		
		JFreeChart chart = ChartFactory.createMultiplePieChart("Disk Usage", pieDataset, 
				org.jfree.util.TableOrder.BY_ROW, false, false, false);
	
		ChartUtilities.writeChartAsJPEG(out, chart, 600, 600);
						
	}
	
}
