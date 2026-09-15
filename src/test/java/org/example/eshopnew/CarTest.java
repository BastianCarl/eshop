package org.example.eshopnew;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CarTest {
    Car car;
    @Test
    void doSth() {
        Engine engine = Mockito.mock(Engine.class);
        car = new Car(engine);

        when(engine.toString()).thenReturn("Engine is working");

        String result = car.doSth();

        Mockito.verify(engine, Mockito.atLeastOnce()).toString();
        assertEquals("Engine is working", result);
    }
}
