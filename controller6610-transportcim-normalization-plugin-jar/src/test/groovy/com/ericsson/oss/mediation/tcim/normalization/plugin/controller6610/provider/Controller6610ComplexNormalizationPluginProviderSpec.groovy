package com.ericsson.oss.mediation.tcim.normalization.plugin.controller6610.provider

import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject
import com.ericsson.oss.itpf.datalayer.dps.stub.RuntimeConfigurableDps
import com.ericsson.oss.mediation.tcim.normalization.api.data.NormalizedObject
import com.ericsson.oss.mediation.tcim.normalization.api.data.TransportCimNormalizationContext
import com.ericsson.oss.mediation.tcim.normalization.api.data.TransportCimNormalizationProperties
import spock.lang.Unroll

class Controller6610ComplexNormalizationPluginProviderSpec extends CdiSpecification {
  
    @ObjectUnderTest
    CONTROLLER6610ComplexNormalizationPluginProvider Controller6610ComplexNormalizationPluginProvider
  
    protected RuntimeConfigurableDps runtimeDps

    def setup() {
        runtimeDps = cdiInjectorRule.getService(RuntimeConfigurableDps.class)
    }

    @Unroll
    def "Handler Test Controller6610"() {

        given: "createMos"
        createRootMos(nodeAddress, neType)

        and: "create Input Context"
        def ctx = initContext(nodeAddress, neType, OperOperatingMode, layerRate, duplex, speed)

        when: "execute onEvent method"
        Controller6610ComplexNormalizationPluginProvider.execute(ctx)

        then: "assert Context Result"
        NormalizedObject intEth1 = ctx.getTargetNormalizedObjectMap().get("Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-1")
        NormalizedObject eth1 = ctx.getTargetNormalizedObjectMap().get("Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-1,Ethernet=1")
        NormalizedObject intEth2 = ctx.getTargetNormalizedObjectMap().get("Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-2")
        NormalizedObject eth2 = ctx.getTargetNormalizedObjectMap().get("Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-2,Ethernet=1")

        assert intEth1.getAttribute('link-type').equals('NOT_AVAILABLE')
        assert intEth1.getAttribute('type').equals('ethernetCsmacd')
        assert intEth1.getAttribute('enabled').equals(true)
        assert eth1.getAttribute('auto-negotiate').equals(true)
        assert controller6610ComplexNormalizationPluginProvider.getName().contains("CONTROLLER6610")

        if ('ANY'.equals(OperOperatingMode)) {
            assert intEth1.getAttribute('layer-rate').equals('ETHERNET_1Gb')
            assert eth1.getAttribute('speed').equals(null)
            assert eth1.getAttribute('duplex').equals(null)
        } else if ('100M_HALF'.equals(OperOperatingMode)) {
			assert intEth1.getAttribute('layer-rate').equals('ETHERNET_100Mb')
			assert eth1.getAttribute('duplex').equals('HALF')
			assert eth1.getAttribute('speed').equals('100MB')
        } else if ('100M_FULL'.equals(OperOperatingMode)) {
			assert intEth1.getAttribute('layer-rate').equals('ETHERNET_100Mb')
			assert eth1.getAttribute('duplex').equals('FULL')
			assert eth1.getAttribute('speed').equals('100MB')
        } else if ('1G_FULL'.equals(OperOperatingMode)) {
			assert intEth1.getAttribute('layer-rate').equals('ETHERNET_1Gb')
			assert eth1.getAttribute('duplex').equals('FULL')
			assert eth1.getAttribute('speed').equals('1GB')
        } else if ('10G_FULL'.equals(OperOperatingMode)) {
			assert intEth1.getAttribute('layer-rate').equals('ETHERNET_10Gb')
			assert eth1.getAttribute('duplex').equals('FULL')
			assert eth1.getAttribute('speed').equals('10GB')
        }
          

        where: "used parameters for Mos and Eventcontext"
        nodeAddress           | neType           | OperOperatingMode  | layerRate          | duplex   | speed
        'Controller6610-V201' | 'Controller6610' | 'ANY'              | 'ETHERNET_1Gb'     | null     | null
        'Controller6610-V201' | 'Controller6610' | '100M_HALF'        | 'ETHERNET_100Mb'   | 'HALF'   | '100MB'
        'Controller6610-V201' | 'Controller6610' | '100M_FULL'        | 'ETHERNET_100Mb'   | 'FULL'   | '100MB'
        'Controller6610-V201' | 'Controller6610' | '1G_FULL'          | 'ETHERNET_1Gb'     | 'FULL'   | '1GB'
        'Controller6610-V201' | 'Controller6610' | '10G_FULL'         | 'ETHERNET_10Gb'    | 'FULL'   | '10GB'
         
    }
  
    def initContext(nodeAddress, neType, OperOperatingMode, layerRate, duplex, speed) {

        def properties = setupProperties(nodeAddress, neType)
        def ctx = new TransportCimNormalizationContext(properties)
        def targetNormalizedMap = setupTargetNormalizedMap(nodeAddress, layerRate)
        def complexAttributes = setupComplexAttributes(nodeAddress, OperOperatingMode, duplex, speed)
        ctx.setTargetNormalizedObjectMap(targetNormalizedMap)
        ctx.setComplexAttributes(complexAttributes)
        return ctx
    }

    def setupProperties(nodeAddress, neType) {
        def properties = new TransportCimNormalizationProperties()
        properties.put("neType", neType)
        properties.put("nodeFdn", "Network=1,Node=" + nodeAddress)
        properties.put("nePlatformType", 'null')
        properties.put("moRootNormalization", 'ManagedElement=' + nodeAddress)
        properties.put("isComplexNormalizationPluginDefined", true)

        return properties
    }

    def setupTargetNormalizedMap(nodeAddress, layerRate) {
        LinkedHashMap<String, NormalizedObject> targetNormMap = new HashMap<String, NormalizedObject>()
        def interf1Fdn = "Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-1"
        NormalizedObject interf1 = new NormalizedObject(interf1Fdn, 'Eth-1', 'OSS_TCIM', 'Interface')
        interf1.setMirrorMoFdn("ManagedElement=" + nodeAddress + ",Transport=1" + ",EthernetPort=1")
        interf1.setAttribute("link-type", "NOT_AVAILABLE")
        interf1.setAttribute("type", "ethernetCsmacd")
        interf1.setAttribute("enabled", true)
        targetNormMap.put(interf1Fdn, interf1)

        def eth1Fdn = "Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-1,Ethernet=1"
        NormalizedObject eth1 = new NormalizedObject(eth1Fdn, '1', 'OSS_TCIM', 'Ethernet')
        eth1.setMirrorMoFdn("ManagedElement=" + nodeAddress + ",Transport=1" + ",EthernetPort=1")
		eth1.setAttribute("layer-rate", layerRate)
		eth1.setAttribute("auto-negotiate", true)
        targetNormMap.put(eth1Fdn, eth1)

        def interf2Fdn = "Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-2"
		NormalizedObject interf2 = new NormalizedObject(interf2Fdn, 'Eth-1', 'OSS_TCIM', 'Interface')
        interf2.setMirrorMoFdn("ManagedElement=" + nodeAddress + ",Transport=1" + ",EthernetPort=2")
		interf2.setAttribute("link-type", "NOT_AVAILABLE")
		interf2.setAttribute("type", "ethernetCsmacd")
		interf2.setAttribute("enabled", true)
        targetNormMap.put(interf2Fdn, interf2)

        def eth2Fdn = "Network=1,Node=" + nodeAddress + ",Interfaces=1,Interface=Eth-2,Ethernet=1"
        NormalizedObject eth2 = new NormalizedObject(eth2Fdn, '2', 'OSS_TCIM', 'Ethernet')
        eth2.setMirrorMoFdn("ManagedElement=" + nodeAddress + ",Transport=1" + ",EthernetPort=2")
		eth1.setAttribute("layer-rate", layerRate)
		eth2.setAttribute("auto-negotiate", true)
        targetNormMap.put(eth2Fdn, eth2)

        return targetNormMap
    }

    def setupComplexAttributes(nodeAddress, OperOperatingMode, duplex, speed) {
        List<Object> complexAttributes = new ArrayList<Object>()

        def eth1Port = "ManagedElement=" + nodeAddress + ",Transport=1" + ",EthernetPort=1"
        def eth1PortAttributes = new HashMap<String, Object>()
        eth1PortAttributes.put('administrativeState', 'LOCKED')
        eth1PortAttributes.put('autoNegEnable', 'true')
        eth1PortAttributes.put('availabilityStatus', null)
        eth1PortAttributes.put('egressQosClassification', null)
        eth1PortAttributes.put('ethernetPortId', '1')
		eth1PortAttributes.put('operOperatingMode', OperOperatingMode)
        eth1PortAttributes.put('duplex', duplex)
		eth1PortAttributes.put('speed', speed)
        eth1PortAttributes.put('mtu', 120)
        ManagedObject ethPortMo = runtimeDps.addManagedObject().withFdn(eth1Port).type("EthernetPort").namespace("RtnL2EthernetPort_SSC").version('2016.3.9')
                .addAttributes(eth1PortAttributes).generateTree().build()
        complexAttributes.add(ethPortMo)

        def eth2Port = "ManagedElement=" + nodeAddress + ",Transport=1" + ",EthernetPort=2"
        def eth2PortAttributes = new HashMap<String, Object>()
        eth2PortAttributes.put('administrativeState', 'UNLOCKED')
        eth2PortAttributes.put('autoNegEnable', 'false')
        eth2PortAttributes.put('availabilityStatus', null)
        eth2PortAttributes.put('egressQosClassification', null)
        eth2PortAttributes.put('ethernetPortId', '2')
        eth2PortAttributes.put('operOperatingMode', OperOperatingMode)
        eth2PortAttributes.put('duplex', duplex)
		eth2PortAttributes.put('speed', speed)
        eth2PortAttributes.put('mtu', 100)
        ManagedObject eth2PortMo = runtimeDps.addManagedObject().withFdn(eth2Port).type("EthernetPort").namespace("RtnL2EthernetPort_SSC").version('2016.3.9')
                .addAttributes(eth2PortAttributes).generateTree().build()
        complexAttributes.add(eth2PortMo)  

        return complexAttributes
    }

    def createRootMos(nodeAddress, neType) {
        def networkElement = "NetworkElement=" + nodeAddress
        def networkElementAttributes = new HashMap<String, Object>()
        networkElementAttributes.put('networkElementId', nodeAddress)
        networkElementAttributes.put('neType', neType)
        networkElementAttributes.put('platformType', 'null')
        networkElementAttributes.put('ossModelIdentity', '21.Q4-R4A07')
        //networkElementAttributes.put('ossPrefix', 'MeContext=' + nodeAddress)

        ManagedObject networkElementMo = runtimeDps.addManagedObject().withFdn(networkElement).namespace("OSS_NE_DEF").version('2.0.0')
                .addAttributes(networkElementAttributes).generateTree().build()
        networkElementMo.setTarget(networkElementMo)


        def managedElement = "MeContext=" + nodeAddress + ",ManagedElement=" + nodeAddress
        def managedElementAttributes = new HashMap<String, Object>()
        managedElementAttributes.put('managedElementId', nodeAddress)
        managedElementAttributes.put('neType', neType)
        managedElementAttributes.put('platformType', 'null')

        def createManagedElement = runtimeDps.addManagedObject().withFdn(managedElement).namespace("Controller6610_NODE_MODEL").version('1.0.0')
                .addAttributes(managedElementAttributes).generateTree().build()
        createManagedElement

        def transport = "ManagedElement=" + nodeAddress + ",Transport=1"
        def transportAttributes = new HashMap<String, Object>()
        transportAttributes.put('transportId', "1")
        ManagedObject transportMo = runtimeDps.addManagedObject().withFdn(transport).type("Controller6610").namespace("ECIM_Top").version('2.3.0')
                .addAttributes(transportAttributes).generateTree().build()
        transportMo.getName()
    }
}
