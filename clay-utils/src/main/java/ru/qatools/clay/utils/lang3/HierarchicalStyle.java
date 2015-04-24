package ru.qatools.clay.utils.lang3;

import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * @author Alexander Andryashin aandryashin@yandex-team.ru
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("unused")
public class HierarchicalStyle extends ToStringStyle {

    public static final List<String> DEFAULT_LEAF_PACKAGE_NAMES = Arrays.asList(
            "java.lang"
    );

    private final List<String> leafPackageNames;

    public HierarchicalStyle(final String... leafPackageNames) {
        this(new ArrayList<String>(DEFAULT_LEAF_PACKAGE_NAMES) {{
            addAll(Arrays.asList(leafPackageNames));
        }});
    }

    protected HierarchicalStyle(HierarchicalStyle other) {
        this(other.leafPackageNames);
    }

    protected HierarchicalStyle(List<String> leafPackageNames) {
        this.leafPackageNames = leafPackageNames;

        setArrayContentDetail(true);
        setArrayStart("[");
        setArraySeparator(",");
        setArrayEnd("]");
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
        setUseFieldNames(true);

        setContentStart("{");
        setContentEnd("}");
    }

    private boolean isLeaf(Object value) {
        String className = value.getClass().getName();
        for (String packageName : leafPackageNames) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (isLeaf(value)) {
            buffer.append(value);
        } else {
            appendNode(buffer, fieldName, value);
        }
    }

    protected StringBuffer appendNode(StringBuffer buffer, String fieldName, Object value) {
        return buffer.append(reflectionToString(value, this));
    }

    @Override
    public void appendDetail(StringBuffer buffer, String fieldName, Collection value) {
        appendDetail(buffer, fieldName, value.toArray());
    }
}
