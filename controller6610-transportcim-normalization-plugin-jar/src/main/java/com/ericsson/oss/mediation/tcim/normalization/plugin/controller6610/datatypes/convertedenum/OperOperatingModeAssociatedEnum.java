/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2022
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.datatypes.convertedenum;

import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.ETH_100MB;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.ETH_10GB;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.ETH_1GB;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.FULL;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.HALF;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.HUNDRED_MB;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.ONE_GB;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.TEN_GB;

/**
 * operOperatingModeAssociated
 */
public enum OperOperatingModeAssociatedEnum {
    ANY("ANY", ETH_1GB, null, null),
    HALF_100_MB("100M_HALF", ETH_100MB, HALF, HUNDRED_MB),
    FULL_100_MB("100M_FULL", ETH_100MB, FULL, HUNDRED_MB),

    FULL_1_GB("1G_FULL", ETH_1GB, FULL, ONE_GB),
    FULL_SLAVE_1_GB("1G_FULL_SLAVE", ETH_1GB, FULL, ONE_GB),
    FULL_MASTER_1_GB("1G_FULL_MASTER", ETH_1GB, FULL, ONE_GB),

    FULL_10_GB("10G_FULL", ETH_10GB, FULL, TEN_GB),
    FULL_SLAVE_10_GB("10G_FULL_SLAVE", ETH_10GB, FULL, TEN_GB),
    FULL_MASTER_10_GB("10G_FULL_MASTER", ETH_10GB, FULL, TEN_GB),

    FULL_MASTER_40_GB("40G_FULL", null, null, null),
    FULL_MASTER_100_GB("100G_FULL", null, null, null),
    HALF_10_MB("10M_HALF", null, null, null),
    FULL_10_MB("10M_FULL", null, null, null);

    private String valueMirror;
    private String valueNormLayerRate;
    private String valueNormDuplex;
    private String valueNormSpeed;

    OperOperatingModeAssociatedEnum(final String valueMirror, final String valueNormLayerRate, final String valueNormDuplex,
                                    final String valueNormSpeed) {
        this.valueMirror = valueMirror;
        this.valueNormLayerRate = valueNormLayerRate;
        this.valueNormDuplex = valueNormDuplex;
        this.valueNormSpeed = valueNormSpeed;
    }

    public static String getNormalizedLayerRate(final String valueMirror) {
            for (final OperOperatingModeAssociatedEnum valueManaged : OperOperatingModeAssociatedEnum.values()) {
                if (valueManaged.valueMirror.equals(valueMirror)) {
                    return valueManaged.valueNormLayerRate;
                }
            }
            return null;
    }

    public static String getNormalizedDuplex(final String valueMirror) {
        for (final OperOperatingModeAssociatedEnum valueManaged : OperOperatingModeAssociatedEnum.values()) {
            if (valueManaged.valueMirror.equals(valueMirror)) {
                return valueManaged.valueNormDuplex;
            }
        }
        return null;
    }

    public static String getNormalizedSpeed(final String valueMirror) {
        for (final OperOperatingModeAssociatedEnum valueManaged : OperOperatingModeAssociatedEnum.values()) {
            if (valueManaged.valueMirror.equals(valueMirror)) {
                return valueManaged.valueNormSpeed;
            }
        }
        return null;
    }
}