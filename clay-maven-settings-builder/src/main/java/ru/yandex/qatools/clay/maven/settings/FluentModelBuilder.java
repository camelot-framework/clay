package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * @author innokenty
 */
@SuppressWarnings("JavaDoc")
public class FluentModelBuilder {

    private final Model model;

    private FluentModelBuilder(Model model) {
        this.model = model;
    }

    public static FluentModelBuilder newModel() {
        return newPom();
    }

    public static FluentModelBuilder newPom() {
        return new FluentModelBuilder(new Model());
    }

    public static FluentModelBuilder newModel(FileInputStream inputStream)
            throws IOException, XmlPullParserException {
        return newPom(inputStream);
    }

    protected static FluentModelBuilder newPom(FileInputStream inputStream)
            throws IOException, XmlPullParserException {
        return new FluentModelBuilder(new MavenXpp3Reader().read(inputStream));
    }

    public FluentModelBuilder marshalTo(File file) throws IOException {
        marshalTo(new FileOutputStream(file));
        return this;
    }

    public FluentModelBuilder marshalTo(FileOutputStream outputStream) throws IOException {
        new MavenXpp3Writer().write(outputStream, model);
        return this;
    }

    public FluentModelBuilder marshalTo(Writer writer) throws IOException {
        new MavenXpp3Writer().write(writer, model);
        return this;
    }

    public Model build() {
        return model;
    }

    /* DELEGATED METHODS */

    /**
     * Method addContributor.
     *
     * @param contributor
     */
    public FluentModelBuilder withContributor(Contributor contributor) {
        model.addContributor(contributor);
        return this;
    }

    /**
     * Method addDeveloper.
     *
     * @param developer
     */
    public FluentModelBuilder withDeveloper(Developer developer) {
        model.addDeveloper(developer);
        return this;
    }

    /**
     * Method addLicense.
     *
     * @param license
     */
    public FluentModelBuilder withLicense(License license) {
        model.addLicense(license);
        return this;
    }

    /**
     * Method addMailingList.
     *
     * @param mailingList
     */
    public FluentModelBuilder withMailingList(MailingList mailingList) {
        model.addMailingList(mailingList);
        return this;
    }

    /**
     * Method addProfile.
     *
     * @param profile
     */
    public FluentModelBuilder withProfile(Profile profile) {
        model.addProfile(profile);
        return this;
    }

    /**
     * Set the identifier for this artifact that is unique within
     * the group given by the
     *             group ID. An artifact is something that is
     * either produced or used by a project.
     *             Examples of artifacts produced by Maven for a
     * project include: JARs, source and binary
     *             distributions, and WARs.
     *
     * @param artifactId
     */
    public FluentModelBuilder withArtifactId(String artifactId) {
        model.setArtifactId(artifactId);
        return this;
    }

    /**
     * Set information required to build the project.
     *
     * @param build
     */
    public FluentModelBuilder withBuild(Build build) {
        model.setBuild(build);
        return this;
    }

    /**
     * Set the project's continuous integration information.
     *
     * @param ciManagement
     */
    public FluentModelBuilder withCiManagement(CiManagement ciManagement) {
        model.setCiManagement(ciManagement);
        return this;
    }

    /**
     * Set describes the contributors to a project that are not yet
     * committers.
     *
     * @param contributors
     */
    public FluentModelBuilder withContributors(List<Contributor> contributors) {
        model.setContributors(contributors);
        return this;
    }

    /**
     * Set a detailed description of the project, used by Maven
     * whenever it needs to
     *             describe the project, such as on the web site.
     * While this element can be specified as
     *             CDATA to enable the use of HTML tags within the
     * description, it is discouraged to allow
     *             plain text representation. If you need to modify
     * the index page of the generated web
     *             site, you are able to specify your own instead
     * of adjusting this text.
     *
     * @param description
     */
    public FluentModelBuilder withDescription(String description) {
        model.setDescription(description);
        return this;
    }

    /**
     * Set describes the committers of a project.
     *
     * @param developers
     */
    public FluentModelBuilder withDevelopers(List<Developer> developers) {
        model.setDevelopers(developers);
        return this;
    }

    /**
     * Set a universally unique identifier for a project. It is
     * normal to
     *             use a fully-qualified package name to
     * distinguish it from other
     *             projects with a similar name (eg.
     * <code>org.apache.maven</code>).
     *
     * @param groupId
     */
    public FluentModelBuilder withGroupId(String groupId) {
        model.setGroupId(groupId);
        return this;
    }

    /**
     * Set the year of the project's inception, specified with 4
     * digits. This value is
     *             used when generating copyright notices as well
     * as being informational.
     *
     * @param inceptionYear
     */
    public FluentModelBuilder withInceptionYear(String inceptionYear) {
        model.setInceptionYear(inceptionYear);
        return this;
    }

    /**
     * Set the project's issue management system information.
     *
     * @param issueManagement
     */
    public FluentModelBuilder withIssueManagement(IssueManagement issueManagement) {
        model.setIssueManagement(issueManagement);
        return this;
    }

    /**
     * Set this element describes all of the licenses for this
     * project.
     *             Each license is described by a
     * <code>license</code> element, which
     *             is then described by additional elements.
     *             Projects should only list the license(s) that
     * applies to the project
     *             and not the licenses that apply to dependencies.
     *             If multiple licenses are listed, it is assumed
     * that the user can select
     *             any of them, not that they must accept all.
     *
     * @param licenses
     */
    public FluentModelBuilder withLicenses(List<License> licenses) {
        model.setLicenses(licenses);
        return this;
    }

    /**
     * Set contains information about a project's mailing lists.
     *
     * @param mailingLists
     */
    public FluentModelBuilder withMailingLists(List<MailingList> mailingLists) {
        model.setMailingLists(mailingLists);
        return this;
    }

    /**
     * Set the modelEncoding field.
     *
     * @param modelEncoding
     */
    public FluentModelBuilder withModelEncoding(String modelEncoding) {
        model.setModelEncoding(modelEncoding);
        return this;
    }

    /**
     * Set declares to which version of project descriptor this POM
     * conforms.
     *
     * @param modelVersion
     */
    public FluentModelBuilder withModelVersion(String modelVersion) {
        model.setModelVersion(modelVersion);
        return this;
    }

    /**
     * Set the full name of the project.
     *
     * @param name
     */
    public FluentModelBuilder withName(String name) {
        model.setName(name);
        return this;
    }

    /**
     * Set this element describes various attributes of the
     * organization to which the
     *             project belongs. These attributes are utilized
     * when documentation is created (for
     *             copyright notices and links).
     *
     * @param organization
     */
    public FluentModelBuilder withOrganization(Organization organization) {
        model.setOrganization(organization);
        return this;
    }

    /**
     * Set the type of artifact this project produces, for example
     * <code>jar</code>
     *               <code>war</code>
     *               <code>ear</code>
     *               <code>pom</code>.
     *             Plugins can create their own packaging, and
     *             therefore their own packaging types,
     *             so this list does not contain all possible
     * types.
     *
     * @param packaging
     */
    public FluentModelBuilder withPackaging(String packaging) {
        model.setPackaging(packaging);
        return this;
    }

    /**
     * Set the location of the parent project, if one exists.
     * Values from the parent
     *             project will be the default for this project if
     * they are left unspecified. The location
     *             is given as a group ID, artifact ID and version.
     *
     * @param parent
     */
    public FluentModelBuilder withParent(Parent parent) {
        model.setParent(parent);
        return this;
    }

    /**
     * Set describes the prerequisites in the build environment for
     * this project.
     *
     * @param prerequisites
     */
    public FluentModelBuilder withPrerequisites(Prerequisites prerequisites) {
        model.setPrerequisites(prerequisites);
        return this;
    }

    /**
     * Set a listing of project-local build profiles which will
     * modify the build process
     *             when activated.
     *
     * @param profiles
     */
    public FluentModelBuilder withProfiles(List<Profile> profiles) {
        model.setProfiles(profiles);
        return this;
    }

    /**
     * Set specification for the SCM used by the project, such as
     * CVS, Subversion, etc.
     *
     * @param scm
     */
    public FluentModelBuilder withScm(Scm scm) {
        model.setScm(scm);
        return this;
    }

    /**
     * Set the URL to the project's homepage.
     *             <br /><b>Default value is</b>: parent value [+
     * path adjustment] + artifactId.
     *
     * @param url
     */
    public FluentModelBuilder withUrl(String url) {
        model.setUrl(url);
        return this;
    }

    /**
     * Set the current version of the artifact produced by this
     * project.
     *
     * @param version
     */
    public FluentModelBuilder withVersion(String version) {
        model.setVersion(version);
        return this;
    }

    public FluentModelBuilder withPomFile(File pomFile) {
        model.setPomFile(pomFile);
        return this;
    }

    /**
     * Method addDependency.
     *
     * @param dependency
     */
    public FluentModelBuilder withDependency(Dependency dependency) {
        model.addDependency(dependency);
        return this;
    }

    /**
     * Method addModule.
     *
     * @param string
     */
    public FluentModelBuilder withModule(String string) {
        model.addModule(string);
        return this;
    }

    /**
     * Method addPluginRepository.
     *
     * @param repository
     */
    public FluentModelBuilder withPluginRepository(Repository repository) {
        model.addPluginRepository(repository);
        return this;
    }

    /**
     * Method addProperty.
     *  @param key
     * @param value
     */
    public FluentModelBuilder withProperty(String key, String value) {
        model.addProperty(key, value);
        return this;
    }

    /**
     * Method addRepository.
     *
     * @param repository
     */
    public FluentModelBuilder withRepository(Repository repository) {
        model.addRepository(repository);
        return this;
    }

    /**
     * Set this element describes all of the dependencies
     * associated with a
     *             project.
     *             These dependencies are used to construct a
     * classpath for your
     *             project during the build process. They are
     * automatically downloaded from the
     *             repositories defined in this project.
     *             See <a
     * href="http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html">the
     *             dependency mechanism</a> for more information.
     *
     * @param dependencies
     */
    public FluentModelBuilder withDependencies(List<Dependency> dependencies) {
        model.setDependencies(dependencies);
        return this;
    }

    /**
     * Set default dependency information for projects that inherit
     * from this one. The
     *             dependencies in this section are not immediately
     * resolved. Instead, when a POM derived
     *             from this one declares a dependency described by
     * a matching groupId and artifactId, the
     *             version and other values from this section are
     * used for that dependency if they were not
     *             already specified.
     *
     * @param dependencyManagement
     */
    public FluentModelBuilder withDependencyManagement(DependencyManagement dependencyManagement) {
        model.setDependencyManagement(dependencyManagement);
        return this;
    }

    /**
     * Set distribution information for a project that enables
     * deployment of the site
     *             and artifacts to remote web servers and
     * repositories respectively.
     *
     * @param distributionManagement
     */
    public FluentModelBuilder withDistributionManagement(DistributionManagement distributionManagement) {
        model.setDistributionManagement(distributionManagement);
        return this;
    }

    /**
     *
     *  @param key
     * @param location
     */
    public FluentModelBuilder withLocation(Object key, InputLocation location) {
        model.setLocation(key, location);
        return this;
    }

    /**
     * Set the modules (sometimes called subprojects) to build as a
     * part of this
     *             project. Each module listed is a relative path
     * to the directory containing the module.
     *             To be consistent with the way default urls are
     * calculated from parent, it is recommended
     *             to have module names match artifact ids.
     *
     * @param modules
     */
    public FluentModelBuilder withModules(List<String> modules) {
        model.setModules(modules);
        return this;
    }

    /**
     * Set the lists of the remote repositories for discovering
     * plugins for builds and
     *             reports.
     *
     * @param pluginRepositories
     */
    public FluentModelBuilder withPluginRepositories(List<Repository> pluginRepositories) {
        model.setPluginRepositories(pluginRepositories);
        return this;
    }

    /**
     * Set properties that can be used throughout the POM as a
     * substitution, and
     *             are used as filters in resources if enabled.
     *             The format is
     * <code>&lt;name&gt;value&lt;/name&gt;</code>.
     *
     * @param properties
     */
    public FluentModelBuilder withProperties(Properties properties) {
        model.setProperties(properties);
        return this;
    }

    /**
     * Set this element includes the specification of report
     * plugins to use
     *             to generate the reports on the Maven-generated
     * site.
     *             These reports will be run when a user executes
     * <code>mvn site</code>.
     *             All of the reports will be included in the
     * navigation bar for browsing.
     *
     * @param reporting
     */
    public FluentModelBuilder withReporting(Reporting reporting) {
        model.setReporting(reporting);
        return this;
    }

    /**
     * Set <b>Deprecated</b>. Now ignored by Maven.
     *
     * @param reports
     */
    public FluentModelBuilder withReports(Object reports) {
        model.setReports(reports);
        return this;
    }

    /**
     * Set the lists of the remote repositories for discovering
     * dependencies and
     *             extensions.
     *
     * @param repositories
     */
    public FluentModelBuilder withRepositories(List<Repository> repositories) {
        model.setRepositories(repositories);
        return this;
    }
}
