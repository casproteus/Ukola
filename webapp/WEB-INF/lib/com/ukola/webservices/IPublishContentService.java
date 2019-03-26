/**
 * IPublishContentService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ukola.webservices;

public interface IPublishContentService extends javax.xml.rpc.Service {
    public java.lang.String getws_content_publishAddress();

    public com.ukola.webservices.IPublishContent getws_content_publish() throws javax.xml.rpc.ServiceException;

    public com.ukola.webservices.IPublishContent getws_content_publish(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
