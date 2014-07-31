package ru.yandex.qatools.clay.utils.lang3;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author innokenty
 */
@SuppressWarnings("unused")
public class NoClassNameStyle extends ToStringStyle {

    {
        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);
    }
}
