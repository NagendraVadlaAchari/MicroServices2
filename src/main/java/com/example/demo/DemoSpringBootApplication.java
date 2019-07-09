package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.Subject;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;



@SpringBootApplication
@RestController
@ComponentScan("com.example.demo.DemoSpringBootApplication")
public class DemoSpringBootApplication {

	static java.util.Properties props = new java.util.Properties();
	// Properties props = null;
	static Connection connection = null;
	static Domain domain = null;
	static ObjectStore objectstore = null;

	public static Connection getConnection() throws IOException {

		FileInputStream file = null;
		try {
			file = new FileInputStream("C:\\Nagendra\\Nag.properties");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		props.load(file);

		String uri = props.getProperty("URI");
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		String domain = props.getProperty("domain");
		String objectstore = props.getProperty("objectstore");

		// System.setProperty("java.security.auth.login.config", jaasLogin);
		// C:\Nagendra\wsi
		System.setProperty("wasp.location", "C:\\Nagendra\\wsi");
		System.setProperty("filenet.pe.bootstrap.ceuri", uri);

		// we are creating connection object, inorder to get connection
		Connection connection = Factory.Connection.getConnection(uri);

		// we are getting the user context object
		UserContext userContext = UserContext.get();

		// standlone project hence stang name is (null or filenetp8)
		Subject subject = userContext.createSubject(connection, username, password, "FileNetP8WSI");

		userContext.pushSubject(subject);

		System.out.println("CE Connection Establised Sucessfully");

		return connection;
	}

	public static Domain getDomain() throws IOException {

		// System.out.println("Entered into getDomain method");

		Connection connection2 = getConnection();

		Domain domain = Factory.Domain.fetchInstance(connection2, props.getProperty("domain"), null);
		// Domain domain = Factory.Domain.fetchInstance(connection2, null, null);
		// System.out.println("domain name ::::::"+domain.get_Name());

		return domain;
	}

	public static ObjectStore getObjectStore() throws IOException {

		// System.out.println("Entered into getObjectStore");

		Domain domain2 = getDomain();

		ObjectStore objectstore = Factory.ObjectStore.fetchInstance(domain2, props.getProperty("objectstore"), null);

		// System.out.println("Object store name:::::"+objectstore.get_Name());

		return objectstore;
	}

	public void createInstance() {

		try {

			System.out.println("Entred into createCustomObjectClassAPI");
			DemoSpringBootApplication demoSpringBootApplication = new DemoSpringBootApplication();
			Connection con = demoSpringBootApplication.getConnection();
			Domain domain = demoSpringBootApplication.getDomain();
			ObjectStore objectStore = demoSpringBootApplication.getObjectStore();

			InputStreamReader converter = new InputStreamReader(System.in);

			System.out.println("::::::::::::::::" + converter);
			BufferedReader in = new BufferedReader(converter);
			String strClassName = in.readLine();

			System.out.println("123456" + "" + "" + "strClassName");
// {D8330661-BA9E-472B-BE3E-3D8C47A6053A}
			ClassDefinition customclassDefinition = Factory.ClassDefinition.fetchInstance(objectStore,
					"{D8330661-BA9E-472B-BE3E-3D8C47A6053A}", null);

			// ClassDefinition customclassDefinition =
			// Factory.ClassDefinition.fetchInstance(objectStore,
			// "{D32E4F58-AFB2-11D2-8BD6-00E0290F729A}", null);
			ClassDefinition subclass = customclassDefinition.createSubclass();

			LocalizedString localizedString = Factory.LocalizedString.createInstance();

			localizedString.set_LocalizedText(strClassName);
			localizedString.set_LocaleName(objectStore.get_Name());

			subclass.set_DisplayNames(Factory.LocalizedString.createList());
			subclass.get_DisplayNames().add(localizedString);

			subclass.save(RefreshMode.REFRESH);

			System.out.println("Cutome class is created sucessfully with " + customclassDefinition.get_Name());

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}
	/*
	 * public VWSession PEConnection() { VWSession vwSession = null; // VWSession
	 * peSession; try {
	 * 
	 * FileInputStream file = null; try { file = new
	 * FileInputStream("C:\\Nagendra\\Nag.properties"); } catch
	 * (FileNotFoundException e) {
	 * 
	 * e.printStackTrace(); } props.load(file);
	 * 
	 * String uri = props.getProperty("URI"); String username =
	 * props.getProperty("username"); String password =
	 * props.getProperty("password"); String domain = props.getProperty("domain");
	 * String objectstore = props.getProperty("objectstore"); Connection connection2
	 * = getConnection(); Domain domain1 = Factory.Domain.fetchInstance(connection2,
	 * props.getProperty("domain"), null);
	 * System.setProperty("filenet.pe.bootstrap.ceuri", uri); // vwSession = new
	 * VWSession();
	 * 
	 * System.out.println(domain1.get_Name() + "......domain name..!"); // vwSession
	 * = new VWSession(domain1.get_Name(), username, //
	 * password,props.getProperty("connectionPointName")); vwSession = new
	 * VWSession(domain1.get_Name(), username, password, "FNCP");
	 * 
	 * 
	 * vwSession.setBootstrapCEURI(domain1, props.getProperty("uri"));
	 * vwSession.logon(props.getProperty("username"), props.getProperty("password"),
	 * props.getProperty("connectionPointName"));
	 * 
	 * return vwSession;
	 * 
	 * } catch (Exception e) { // TODO: handle exception
	 * System.out.println(e.getMessage()); return vwSession; }
	 * 
	 * }
	 */
	/*
	 * public static void main1(String[] args) throws IOException {
	 * 
	 * demoSpringBootApplication connectToFilenet = new demoSpringBootApplication();
	 * 
	 * Connection connection2 = connectToFilenet.getConnection();
	 * System.out.println(connection2+"...connection2..."); Domain domain2 =
	 * connectToFilenet.getDomain();
	 * System.out.println(domain2.get_Name()+"...domain2..."); ObjectStore
	 * objectStore2 = connectToFilenet.getObjectStore();
	 * System.out.println(objectStore2+"...objectStore2...");
	 * 
	 * Folder folderInstance = Factory.Folder.createInstance(objectStore2,
	 * "AO_AccountOpening"); Folder get_RootFolder = objectStore2.get_RootFolder();
	 * folderInstance.getProperties().putValue("AO_FirstName", "Nag");
	 * folderInstance.getProperties().putValue("AO_LastName", "Chinna");
	 * folderInstance.save(RefreshMode.REFRESH);
	 * System.out.println("Properties r Updated Successfully.,,");
	 * 
	 * //connectToFilenet.createInstance(); VWSession peConnection =
	 * connectToFilenet.PEConnection();
	 * System.out.println(peConnection.getConnectionPointName()+"...peConnection..."
	 * ); String[] workClassNames = peConnection.fetchWorkClassNames(true); for (int
	 * i=0;i<workClassNames.length;i++){
	 * 
	 * System.out.println(workClassNames[i]+"...." );
	 * 
	 * }
	 */

	/*
	 * VWStepElement createWorkflow =
	 * peConnection.createWorkflow("AO_AccountOpeningTask") ;
	 * System.out.println(createWorkflow+"..createWorkflow...");
	 * createWorkflow.setParameterValue("F_Subject", "AO_AccountOpeningTask", true);
	 * createWorkflow.doDispatch();
	 */
//	}

	@GetMapping("/Name/{name}")
	public String getName1(@PathVariable String name) {
		System.out.println("getName api is called {}" + name);

		try {

			name = null;

			DemoSpringBootApplication connectToFilenet = new DemoSpringBootApplication();
			Connection connection2 = connectToFilenet.getConnection();
			System.out.println(connection2 + "...connection2...");
			Domain domain2 = connectToFilenet.getDomain();
			System.out.println(domain2.get_Name() + "...domain2...");
			ObjectStore objectStore2 = connectToFilenet.getObjectStore();
			System.out.println(objectStore2 + "...objectStore2...");

			Folder folderInstance = Factory.Folder.createInstance(objectStore2, "AO_AccountOpening");
			Folder get_RootFolder = objectStore2.get_RootFolder();
			folderInstance.getProperties().putValue("AO_FirstName", "Nag");
			folderInstance.getProperties().putValue("AO_LastName", "Chinna");
			folderInstance.save(RefreshMode.REFRESH);
			System.out.println("Properties r Updated Successfully.,,");
			name = "Properties r Updated Successfully..!";

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in catch block..!" + e.getMessage());
			name = e.getMessage();
		}

		// logger.info("message {}", this.message);
		return name;
		// return "<h1>Success</h1>";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);
	}

}
