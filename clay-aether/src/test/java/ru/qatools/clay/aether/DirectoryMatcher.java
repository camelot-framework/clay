package ru.qatools.clay.aether;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.allOf;

public class DirectoryMatcher {

    public static class Contains extends TypeSafeMatcher<File> {

        private String fileName;

        public Contains(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected boolean matchesSafely(File directory) {
            return directory.isDirectory() && !FileUtils.listFiles(
                    directory,
                    new NameFileFilter(fileName),
                    TrueFileFilter.INSTANCE
            ).isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("contains file ").appendValue(fileName);
        }
    }

    public static Matcher contains(String first, String... more) {
        Collection<Contains> result = new ArrayList<>();
        result.add(new Contains(first));
        for (String oneMore : more) {
            result.add(new Contains(oneMore));
        }
        return allOf(result.toArray(new Contains[result.size()]));
    }
}
