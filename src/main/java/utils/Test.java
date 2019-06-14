package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.ResultSaver;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class Test {
	public static String PAGE_PARAM = "${image}";
	public static String FILE_PATH = "D:\\Ebook\\Managa\\aa\\";
	public static String slash = System.getProperty("file.separator");

	public static void main(String[] args) throws IOException {
		downloadFromFile("temp/link_1.txt");
	}

	public static void downloadFromFile(String filePath) throws IOException {
		// jmeter.properties
		File jmeterHome = new File("C:\\apache-jmeter-5.0");
		File jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");
		JMeterUtils.setJMeterHome(jmeterHome.getPath());
		JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = "";
			int total = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("ui2")) {
					// Initialize JMeter SaveService
					SaveService.loadProperties();
					// Load existing .jmx Test Plan
					HashTree testPlanTree = SaveService.loadTree(new File("report/temp.jmx"));
					updateTestPlan(testPlanTree, total, line);
					JMeterUtils.initLocale();
					StandardJMeterEngine jm = new StandardJMeterEngine();
					jm.configure(testPlanTree);
					jm.run();
				} else {
					total = Integer.parseInt(line);
					System.out.println(total);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Run JMeter Test
	}

	public static void updateTestPlan(HashTree testPlanTree, int totalPage, String link) {
		TestPlan testPlan = (TestPlan) testPlanTree.getArray()[0];
		ThreadGroup threadGroup = (ThreadGroup) testPlanTree.get(testPlan).getArray()[0];
		LoopController loopController = (LoopController) threadGroup.getProperty("ThreadGroup.main_controller")
				.getObjectValue();
		loopController.setLoops(totalPage / 10 + 1);
		HTTPSamplerProxy httpSamplerProxy = (HTTPSamplerProxy) testPlanTree.get(testPlan).get(threadGroup)
				.getArray()[0];
		httpSamplerProxy.setPath(String.format(link + PAGE_PARAM, new Date().getTime() / 1000));
		ResultSaver resultSaver = (ResultSaver) testPlanTree.get(testPlan).get(threadGroup).get(httpSamplerProxy)
				.getArray()[0];
		resultSaver.setFilename(FILE_PATH + getMangaName(link) + slash);
		CounterConfig counterConfig = (CounterConfig) testPlanTree.get(testPlan).get(threadGroup).get(httpSamplerProxy)
				.getArray()[3];
	}

	public static String getMangaName(String link) {
		String[] temp = link.split("/");
		return temp[temp.length - 2];
	}
}
