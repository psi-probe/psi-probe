package psiprobe.tools.logging.jdk;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.logging.FileHandler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import psiprobe.model.Application;

public class Jdk14FileHandlerAccessorTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void getFile() throws Exception {
		Jdk14FileHandlerAccessor handlerAccessor = new Jdk14FileHandlerAccessor();
		handlerAccessor.setLoggerAccessor(new Jdk14LoggerAccessor());

		String testPath = folder.newFolder().getAbsolutePath();
		FileHandler target = new FileHandler(testPath + "test-%g.log", 1024, 3);

		handlerAccessor.setTarget(target);
		handlerAccessor.setIndex(Integer.toString(0));
		Application testApplication = new Application();
		handlerAccessor.setApplication(testApplication);

		File file = handlerAccessor.getFile();

		assertThat(file.getAbsolutePath(), equalTo(testPath + "test-0.log") );

	}

}