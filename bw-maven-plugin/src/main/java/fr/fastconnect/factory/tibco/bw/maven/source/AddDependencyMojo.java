/**
 * (C) Copyright 2011-2015 FastConnect SAS
 * (http://www.fastconnect.fr/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.fastconnect.factory.tibco.bw.maven.source;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * <p>
 * This goal adds a dependency to the POMs of the defined projects list.
 * <br />
 * Refer to <a href="./list-maven-projects-mojo.html">bw:list-maven-projects</a>
 * goal for an explanation about projects lists.
 * </p>
 * 
 * @example.
 * mvn bw:add-dependency -Dworkspace.root=C:\workspace\bw -DdoIt=true
 * -DdependencyGroupId=com.company.project.bw
 * -DdependencyArtifactId=project-utils
 * -DdependencyType=projlib
 * -DdependencyVersion=1.0.0
 * 
 * @author Mathieu Debove
 *
 */
@Mojo ( name = "add-dependency", requiresProject = false )
public class AddDependencyMojo extends AbstractDependencyMojo {
	
	@Override
	protected String getActionFailure() { return " project failed to add the dependency."; }
	@Override
	protected String getActionFailures() { return " projects failed to add the dependency."; }
	@Override
	protected String getActionSuccess() { return " project successfully added the dependency."; }
	@Override
	protected String getActionSuccesses() { return " projects successfully added the dependency."; }
	
	@Override
	protected void displayProject(AbstractProject p) {
		super.displayProject(p);
		
		Dependency d = createDependency();

		getLog().info("");
		getLog().info("Adding dependency '" + d.getManagementKey() + "' to '" + p.getProjectName() + "'");
	}
	
	@Override
	protected boolean performAction(AbstractProject p) {
		File pom = new File(p.getMandatoryFilePath());

		Dependency d = createDependency();
		
		try {
			if (!POMManager.dependencyExists(pom, d, getLog())) {
				if (!dependencyManagement) {
					POMManager.addDependency(pom, d, getLog());
				} else {
					POMManager.addDependencyManagement(pom, d, getLog());
				}
				return true;
			} else {
				getLog().info("Skipping : '" + d.getManagementKey() + "' is already a dependency in '" + pom + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}

}
