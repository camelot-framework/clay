package ru.qatools.clay.utils.lang3;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A <a href="https://github.com/apache/commons-lang/pull/40">pull request</a>
 * with this class was accepted to Apache Commons Lang project,
 * and this functionality will appear in version 3.4
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("unused")
public class NoClassNameStyle extends ToStringStyle {

    {
        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);
    }
}
