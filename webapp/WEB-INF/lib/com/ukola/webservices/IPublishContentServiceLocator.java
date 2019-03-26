/**
 * IPublishContentServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ukola.webservices;

public class IPublishContentServiceLocator extends org.apache.axis.client.Service implements com.ukola.webservices.IPublishContentService {

    public IPublishContentServiceLocator() {
    }


    public IPublishContentServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IPublishContentServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ws_content_publish
    private java.lang.String ws_content_publish_address = "http://localhost:8080/services/ws_content_publish";

    public java.lang.String getws_content_publishAddress() {
        return ws_content_publish_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ws_content_publishWSDDServiceName = "ws_content_publish";

    public java.lang.String getws_content_publishWSDDServiceName() {
        return ws_content_publishWSDDServiceName;
    }

    public void setws_content_publishWSDDServiceName(java.lang.String name) {
        ws_content_publishWSDDServiceName = name;
    }

    public com.ukola.webservices.IPublishContent getws_content_publish() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ws_content_publish_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getws_content_publish(endpoint);
    }

    public com.ukola.webservices.IPublishContent getws_content_publish(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ukola.webservices.Ws_content_publishSoapBindingStub _stub = new com.ukola.webservices.Ws_content_publishSoapBindingStub(portAddress, this);
            _stub.setPortName(getws_content_publishWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setws_content_publishEndpointAddress(java.lang.String address) {
        ws_content_publish_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ukola.webservices.IPublishContent.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ukola.webservices.Ws_content_publishSoapBindingStub _stub = new com.ukola.webservices.Ws_content_publishSoapBindingStub(new java.net.URL(ws_content_publish_address), this);
                _stub.setPortName(getws_content_publishWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ws_content_publish".equals(inputPortName)) {
            return getws_content_publish();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.ukola.com", "IPublishContentService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.ukola.com", "ws_content_publish"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ws_content_publish".equals(portName)) {
            setws_content_publishEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
