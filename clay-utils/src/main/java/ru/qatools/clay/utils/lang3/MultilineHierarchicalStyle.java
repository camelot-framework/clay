package ru.qatools.clay.utils.lang3;

import org.apache.commons.lang3.SystemUtils;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("unused")
public class MultilineHierarchicalStyle extends HierarchicalStyle {

    private static final String INDENT_STEP = "  ";

    private final String indent;

    public MultilineHierarchicalStyle(String... leafPackageNames) {
        super(leafPackageNames);
        indent = "";
        init();
    }

    protected MultilineHierarchicalStyle(MultilineHierarchicalStyle other) {
        super(other);
        indent = other.indent + INDENT_STEP;
        init();
    }

    private void init() {
        setArraySeparator(", ");
        setContentStart(" {");
        setFieldSeparator(SystemUtils.LINE_SEPARATOR + indent + INDENT_STEP);
        setFieldSeparatorAtStart(true);
        setContentEnd(SystemUtils.LINE_SEPARATOR + indent + "}");
    }

    @Override
    protected StringBuffer appendNode(StringBuffer buffer, String fieldName, Object value) {
        return buffer.append(reflectionToString(value, new MultilineHierarchicalStyle(this)));
    }
}
