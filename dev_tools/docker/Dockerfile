FROM jboss/wildfly
ADD standalone.xml /opt/jboss/wildfly/standalone/configuration/
ADD org/postgres/main/* /opt/jboss/wildfly/modules/
ADD j2ee-ear.ear /opt/jboss/wildfly/standalone/deployments/
EXPOSE 8080/tcp
EXPOSE 9990/tcp