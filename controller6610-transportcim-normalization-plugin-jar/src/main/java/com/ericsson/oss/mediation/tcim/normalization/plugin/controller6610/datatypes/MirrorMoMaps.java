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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;

/**
 * MirrorMoMaps class.
 */
public class MirrorMoMaps {

    private static final List<String> complexMoTypes = Arrays.asList("EthernetPort");
    private static final Logger logger = LoggerFactory.getLogger(MirrorMoMaps.class);
    private final Map<String, ManagedObject> ethernetPortMap;

    public MirrorMoMaps(final List<Object> complexAttributes) {
        ethernetPortMap = new HashMap<>();

        logger.debug("Filling MirrorMoMaps with value contained in complexAttributes input parameter");

        for (final Object complexAttr : complexAttributes) {
            final ManagedObject complexAttrMo = (ManagedObject) complexAttr;
            final String complexAttrFdn = complexAttrMo.getFdn();

            if (complexAttrFdn.contains(complexMoTypes.get(0))) {
                ethernetPortMap.put(complexAttrFdn, complexAttrMo);
            } else {
                logger.warn("Object {} is SKIPPED. It not recognized as MO that should be managed by complex plugin. ", complexAttrFdn);
            }
        }
        logger.debug("MirrorMoMaps properly filled.");
    }

    public Map<String, ManagedObject> getEthernetPortMap() {
        return ethernetPortMap;
    }
}