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

package com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.netypes;

import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.ETHERNET_PORT_NAME;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.NORMALIZED_ATTRIBUTE_DUPLEX;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.NORMALIZED_ATTRIBUTE_LAYER_RATE;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.NORMALIZED_ATTRIBUTE_NAME;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.NORMALIZED_ATTRIBUTE_SPEED;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.NORMALIZED_ETHERNET_PORT_NAME_PREFIX;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.constants.Controller6610Constants.OPER_OP_MODE;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.mediation.tcim.normalization.api.data.NormalizedObject;
import com.ericsson.oss.mediation.tcim.normalization.api.data.TransportCimNormalizationContext;
import com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.datatypes.MirrorMoMaps;
import com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.datatypes.convertedenum.OperOperatingModeAssociatedEnum;

/**
 * Controller6610 NeType Management of mirrorMO
 */
public final class Controller6610 {
    private static final Logger logger = LoggerFactory.getLogger(Controller6610.class);

    private Controller6610() {
    }

    public static void manageController6610(final TransportCimNormalizationContext ctx, final MirrorMoMaps mirrorMoMaps) {
        for (final Map.Entry<String, ManagedObject> ethernetPort : mirrorMoMaps.getEthernetPortMap().entrySet()) {
            logger.debug("starting to manage the Ethernet Port ==> ctx = {}, ethernetPort.getValue() = {}", ctx,
                    ethernetPort.getValue());
            manageEthernetPort(ctx, ethernetPort.getValue());
        }
    }

    public static void manageEthernetPort(final TransportCimNormalizationContext ctx, final ManagedObject ethernetPortMo) {
        logger.debug("looking for the normalized class associated to the mirror class ==> {}, {} ",
                ethernetPortMo.getAttribute(ETHERNET_PORT_NAME), NORMALIZED_ETHERNET_PORT_NAME_PREFIX);
        NormalizedObject normalizedInterface = getAssociatedNormalizedInterface(ctx, ethernetPortMo.getAttribute(ETHERNET_PORT_NAME),
                NORMALIZED_ETHERNET_PORT_NAME_PREFIX);
        if (normalizedInterface != null) {
            updateNormInterfaceWithAttributes(normalizedInterface, ethernetPortMo);
            final String ethFdn = normalizedInterface.getFdn() + ",Ethernet=1";
            NormalizedObject normalizedEthernet = ctx.getTargetNormalizedObjectMap().get(ethFdn);
            updateNormEthernetWithAttr(normalizedEthernet, ethernetPortMo);
            logger.debug("normalizedEthernet update completed");
        }
    }

    private static NormalizedObject getAssociatedNormalizedInterface(final TransportCimNormalizationContext ctx, final Object attributeName,
	            final String normalizedNamePrefix) {
		for (final Map.Entry<String, NormalizedObject> normalizedObjectEntry : ctx.getTargetNormalizedObjectMap().entrySet()) {
			logger.debug("normalizedObjectEntry = {}", normalizedObjectEntry);

			if (normalizedObjectEntry.getValue().getName().equals(normalizedNamePrefix+attributeName)) {
			  return normalizedObjectEntry.getValue();
			}
		}
		logger.warn("Entry identified by name {} NOT FOUND in targetNormalized Map.", attributeName);
		return null;
	}

    private static NormalizedObject updateNormInterfaceWithAttributes(final NormalizedObject normalizedInterface,
                                                                      final ManagedObject ethernetPortMo) {

        normalizedInterface.setAttribute(NORMALIZED_ATTRIBUTE_NAME, normalizedInterface.getName());
    	final String operOperatingMode = ethernetPortMo.getAttribute(OPER_OP_MODE);
    	normalizedInterface.setAttribute(NORMALIZED_ATTRIBUTE_LAYER_RATE, OperOperatingModeAssociatedEnum.getNormalizedLayerRate(operOperatingMode));
        return normalizedInterface;
    }

    private static NormalizedObject updateNormEthernetWithAttr(final NormalizedObject normalizedEthernet,
                                                               final ManagedObject ethernetPortMo) {

    	final String operOperatingMode = ethernetPortMo.getAttribute(OPER_OP_MODE);
    	normalizedEthernet.setAttribute(NORMALIZED_ATTRIBUTE_DUPLEX, OperOperatingModeAssociatedEnum.getNormalizedDuplex(operOperatingMode));
    	normalizedEthernet.setAttribute(NORMALIZED_ATTRIBUTE_SPEED, OperOperatingModeAssociatedEnum.getNormalizedSpeed(operOperatingMode));

        logger.debug("updateNormEthernetWithAttr: duplex = {}, speed = {}", normalizedEthernet.getAttribute(NORMALIZED_ATTRIBUTE_DUPLEX), normalizedEthernet.getAttribute(NORMALIZED_ATTRIBUTE_SPEED));
        return normalizedEthernet;
    }
}