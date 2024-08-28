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

package com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.provider;

import static com.ericsson.oss.mediation.tcim.normalization.api.data.TransportCimNormalizationProperties.NE_TYPE;
import static com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.netypes.Controller6610.manageController6610;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tcim.normalization.api.data.TransportCimNormalizationContext;
import com.ericsson.oss.mediation.tcim.normalization.api.exceptions.TransportCimException;
import com.ericsson.oss.mediation.tcim.normalization.plugin.ComplexNormalizationPluginProvider;
import com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.datatypes.ManagedController6610NeTypeEnum;
import com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.datatypes.MirrorMoMaps;

/**
 * Class implementing the Controller6610 Normalization Plugin. Implements the interface {@link CONTROLLER6610ComplexNormalizationPluginProvider}.
 */
public class CONTROLLER6610ComplexNormalizationPluginProvider implements ComplexNormalizationPluginProvider {

    private static final Logger logger = LoggerFactory.getLogger(CONTROLLER6610ComplexNormalizationPluginProvider.class);

    @Override
    public String getName() {
        return CONTROLLER6610ComplexNormalizationPluginProvider.class.getName();
    }

    @Override
    public void execute(final TransportCimNormalizationContext ctx) throws TransportCimException {
    	logger.info("Started execution of {} for node: {}",
    			CONTROLLER6610ComplexNormalizationPluginProvider.class.getName(), ctx.getNodeFdn());
        final String neType = ctx.getProperties().getProperty(NE_TYPE);
        if (ManagedController6610NeTypeEnum.contains(neType)) {
        	 final MirrorMoMaps mirrorMoMaps = new MirrorMoMaps(ctx.getComplexAttributes());
        	 manageController6610(ctx, mirrorMoMaps);
        } else {
        	logger.warn("Complex Plugin {} DOES NOT Manage {} neType", CONTROLLER6610ComplexNormalizationPluginProvider.class.getName(), neType);
        }
    }
}