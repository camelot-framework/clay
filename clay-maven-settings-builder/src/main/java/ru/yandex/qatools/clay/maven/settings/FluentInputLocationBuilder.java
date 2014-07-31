package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.model.InputLocation;
import org.apache.maven.model.InputSource;

import java.util.Map;

/**
 * @author innokenty
 */
public class FluentInputLocationBuilder {

    private final InputLocation location;

    private FluentInputLocationBuilder(InputLocation location) {
        this.location = location;
    }

    public static FluentInputLocationBuilder newInputLocation(int lineNumber, int columnNumber) {
        return new FluentInputLocationBuilder(new InputLocation(lineNumber, columnNumber));
    }

    public static FluentInputLocationBuilder newInputLocation(int lineNumber, int columnNumber, InputSource source) {
        return new FluentInputLocationBuilder(new InputLocation(lineNumber, columnNumber, source));
    }

    public InputLocation build() {
        return location;
    }

    /* DELEGATED METHODS */

    public FluentInputLocationBuilder withLocation(Object key, InputLocation location) {
        this.location.setLocation(key, location);
        return this;
    }

    public FluentInputLocationBuilder setLocations(Map<Object, InputLocation> locations) {
        location.setLocations(locations);
        return this;
    }
}
