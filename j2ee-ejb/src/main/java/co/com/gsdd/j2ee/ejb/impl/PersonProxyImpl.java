package co.com.gsdd.j2ee.ejb.impl;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import co.com.gsdd.j2ee.ejb.PersonProxy;
import co.com.gsdd.jaxrs.client.ProxyClient;
import co.com.gsdd.jeeservice.model.Person;
import co.com.gsdd.jeeservice.rest.api.PersonRest;

@Default
public class PersonProxyImpl implements PersonProxy {

    @Inject
    private ProxyClient proxyClient;

    @Override
    public Person getPersonFromProxy() {
        PersonRest personService = proxyClient.getProxy("http://localhost:8080/jeeservice", PersonRest.class);
        return personService.getPersonById("lol");
    }

}
