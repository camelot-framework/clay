package ru.qatools.clay.utils.lang3;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * @author Alexander Andryashin aandryashin@yandex-team.ru
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("unused")
public class HierarchicalStyle extends ToStringStyle {

    {
        setArrayContentDetail(true);
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
        setUseFieldNames(true);
    }

    private final List<String> leafPackageNames = new ArrayList<String>() {{
        add("java.lang");
    }};

    public HierarchicalStyle(String... leafPackageNames) {
        this.leafPackageNames.addAll(Arrays.asList(leafPackageNames));
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
            buffer.append(ReflectionToStringBuilder.toString(value, this));
        }
    }

    @Override
    public void appendDetail(StringBuffer buffer, String fieldName, Collection value) {
        appendDetail(buffer, fieldName, value.toArray());
    }
}
