package com.gsdd.j2ee.ejb.impl;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import com.gsdd.j2ee.ejb.PersonProxy;
import com.gsdd.jaxrs.client.ProxyClient;
import com.gsdd.jeeservice.model.Person;
import com.gsdd.jeeservice.rest.api.PersonRest;

@Default
public class PersonProxyImpl implements PersonProxy {

    private static final String JEE_SERVICE_URL = "http://localhost:8080/jeeservice";
    @Inject
    private ProxyClient proxyClient;

    @Override
    public Person getPersonFromProxy() {
        PersonRest personService = proxyClient.getProxy(JEE_SERVICE_URL, PersonRest.class);
        return personService.getPersonById("lol");
    }

}
