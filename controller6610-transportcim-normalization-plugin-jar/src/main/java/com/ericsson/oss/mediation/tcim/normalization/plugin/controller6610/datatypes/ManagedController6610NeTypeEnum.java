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

package com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.datatypes;

/**
 *  Managed for Controller6610 nodeType.
 */
public enum ManagedController6610NeTypeEnum {
    CONTROLLER6610("Controller6610");

    private String value;

    private ManagedController6610NeTypeEnum(String value)
    {
        this.value = value;
    }

    public static boolean contains(String neType) {

        for (ManagedController6610NeTypeEnum netypeManaged : ManagedController6610NeTypeEnum.values()) {
            if (netypeManaged.value.equals(neType)) {
                return true;
            }
        }
        return false;
    }
}